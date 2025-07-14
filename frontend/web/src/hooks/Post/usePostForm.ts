import { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { UseMutateFunction, useQueryClient } from '@tanstack/react-query';
import { v4 as uuidv4 } from 'uuid';
import { PostingData, UploadedImageObj, UploadImage } from '@/types';
import useAutoResizeTextarea from './useAutoResizeTextarea';
import { hashImage } from '@/utils/s3/s3Utils';
import validatePostForm from '@/utils/validatePostForm';

type FormDataType = {
  title: string;
  content: string;
  imageUrls: UploadImage[];
};

type PutPostProps = {
  postId: string;
  formData: PostingData;
};

type UsePostFormProps = {
  initialFormData: FormDataType;
  postPost: UseMutateFunction<unknown, Error, PostingData, unknown>;
  editPost: UseMutateFunction<unknown, Error, PutPostProps, unknown>;
  handleImageUpload: (images: UploadImage[]) => Promise<UploadedImageObj[]>;
  handleDeleteImages: (urls: string[]) => Promise<void>;
};

const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/jpg'];

export default function usePostForm({
  initialFormData,
  postPost,
  editPost,
  handleImageUpload,
  handleDeleteImages,
}: UsePostFormProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const handleResizeHeight = useAutoResizeTextarea();
  const queryClient = useQueryClient();

  const [formData, setFormData] = useState<FormDataType>(initialFormData);
  const [existingHashes, setExistingHashes] = useState<string[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedImage, setSelectedImage] = useState<string>('');

  const { postId, prevformData, type } = location.state || {};

  useEffect(() => {
    if (type === 'update' && prevformData) {
      setFormData({
        title: prevformData.title,
        content: prevformData.content,
        imageUrls:
          prevformData.imageUrls?.map((obj: UploadedImageObj) => ({
            thumbnail: obj.imageUrl,
            isExisting: true,
            hash: obj.hash,
          })) || [],
      });
      setExistingHashes(prevformData.imageUrls?.map((obj: UploadedImageObj) => obj.hash) || []);
    }
  }, [prevformData, type]);

  // 파일 선택 버튼 클릭
  const handleClickFileInput = useCallback((e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    fileInputRef.current?.click();
  }, []);

  // 이미지 삭제
  const handleImgRemove = useCallback(
    (index: number) => {
      const removedImage = formData.imageUrls[index];
      setFormData((prev) => ({
        ...prev,
        imageUrls: prev.imageUrls.filter((_, i) => i !== index),
      }));
      setExistingHashes((prev) => prev.filter((hash) => hash !== removedImage.hash));
    },
    [formData.imageUrls],
  );

  // 이미지 업로드
  const uploadProfile = useCallback(
    async (e: React.ChangeEvent<HTMLInputElement>) => {
      const fileList = e.target.files;
      if (!fileList) return;

      const invalidFiles = Array.from(fileList).filter((file) => !ALLOWED_TYPES.includes(file.type));
      if (invalidFiles.length) {
        invalidFiles.forEach((file) => alert(`${file.name}: 지원하지 않는 파일 형식입니다.`));
        if (fileInputRef.current) fileInputRef.current.value = '';
        return;
      }

      if (formData.imageUrls.length + fileList.length > 10) {
        alert('사진은 최대 10장까지 첨부 가능합니다.');
        return;
      }

      // 이미지 해시로 중복 검사
      const results = await Promise.all(
        Array.from(fileList).map(async (file) => ({
          file,
          fileHash: await hashImage(file),
        })),
      );

      const duplicates = results.filter(({ fileHash }) => existingHashes.includes(fileHash));
      if (duplicates.length) {
        duplicates.forEach(({ file }) => alert(`${file.name}: 이미 같은 이미지가 존재합니다.`));
        if (fileInputRef.current) fileInputRef.current.value = '';
      }

      // 중복이 아닌 이미지만 추가
      const newImages: UploadImage[] = results
        .filter(({ fileHash }) => !existingHashes.includes(fileHash))
        .map(({ file, fileHash }) => {
          setExistingHashes((prev) => [...prev, fileHash]);
          // 파일명에 UUID 붙이기
          const newFileName = `${uuidv4()}.${file.name.split('.').pop()}`;
          const newFile = new File([file], newFileName, { type: file.type });
          return {
            file: newFile,
            thumbnail: URL.createObjectURL(file),
            isExisting: false,
            hash: fileHash,
          };
        });
      setFormData((prev) => ({
        ...prev,
        imageUrls: [...prev.imageUrls, ...newImages],
      }));
    },
    [formData.imageUrls, existingHashes],
  );

  // 폼 제출
  const handleSubmit = useCallback(
    async (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      let uploadedObjs: UploadedImageObj[] = [];

      const errorMsg = validatePostForm(formData);
      if (errorMsg) {
        alert(errorMsg);
        return;
      }

      try {
        const newImages = formData.imageUrls.filter((img) => !img.isExisting);
        uploadedObjs = newImages.length > 0 ? await handleImageUpload(newImages) : [];
      } catch (error) {
        alert('이미지 업로드에 실패했습니다. 다시 시도해주세요.');
        console.error(error);
        return;
      }

      const existingObjs: UploadedImageObj[] = formData.imageUrls
        .filter((img) => img.isExisting)
        .map((img) => ({
          imageUrl: img.thumbnail,
          hash: img.hash,
        }));

      // 삭제된 이미지 판별
      const existingLinks = existingObjs.map((obj) => obj.imageUrl);
      const prevLinks = prevformData?.imageUrls?.map((obj: UploadedImageObj) => obj.imageUrl) || [];
      const removedLinks = prevLinks.filter((link: string) => !existingLinks.includes(link));
      await handleDeleteImages(removedLinks);

      // 최종 업로드할 이미지 객체 배열
      const allImageObjs = [...existingObjs, ...uploadedObjs];

      const formDataWithURL = {
        title: formData.title,
        content: formData.content,
        imageUrls: allImageObjs,
      };
      if (type === 'create') {
        postPost(formDataWithURL, {
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['infinitePostList'] });
            navigate('/post');
          },
          onError: () => alert('게시글 등록을 실패했습니다. 다시 시도해주세요!'),
        });
      } else {
        editPost(
          { postId, formData: formDataWithURL },
          {
            onSuccess: () => {
              navigate(`/detail/${postId}`);
              queryClient.invalidateQueries({ queryKey: ['infinitePostList'] });
            },
            onError: () => alert('게시글 수정을 실패했습니다. 다시 시도해주세요!'),
          },
        );
      }
    },
    [
      formData,
      validatePostForm,
      handleImageUpload,
      handleDeleteImages,
      postPost,
      editPost,
      queryClient,
      navigate,
      type,
      prevformData,
      postId,
    ],
  );

  const handleChange = useCallback(
    (field: keyof FormDataType) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      setFormData((prev) => ({
        ...prev,
        [field]: e.target.value,
      }));
    },
    [],
  );

  return {
    formData,
    setFormData,
    fileInputRef,
    textareaRef,
    handleResizeHeight,
    existingHashes,
    setExistingHashes,
    isModalOpen,
    setIsModalOpen,
    selectedImage,
    setSelectedImage,
    handleClickFileInput,
    handleImgRemove,
    uploadProfile,
    handleSubmit,
    handleChange,
  };
}
