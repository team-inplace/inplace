import { useRef, useState, useEffect } from 'react';
import { IoMdClose, IoIosArrowBack } from 'react-icons/io';
import { useLocation, useNavigate } from 'react-router-dom';
import { styled } from 'styled-components';
import { useQueryClient } from '@tanstack/react-query';
import { v4 as uuidv4 } from 'uuid';
import { Text } from '@/components/common/typography/Text';
import { usePostBoard } from '@/api/hooks/usePostBoard';
import { usePutBoard } from '@/api/hooks/usePutBoard';
import { hashImage } from '@/utils/s3/s3Utils';
import handleImageUpload from '@/libs/s3/handleImageUpload';
import handleDeleteImages from '@/libs/s3/handleImageDelete';
import ImagePreview from '@/components/Board/ImagePreview';
import { UploadedImageObj, UploadImage } from '@/types';

interface FormDataType {
  title: string;
  content: string;
  imgUrls: UploadImage[];
}
export default function BoardPostPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const queryClient = useQueryClient();

  const [formData, setFormData] = useState<FormDataType>({
    title: '',
    content: '',
    imgUrls: [],
  });
  const [existingHashes, setExistingHashes] = useState<string[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedImage, setSelectedImage] = useState<string>('');

  const { boardId, prevformData, type } = location.state || {};
  const { mutate: postBoard } = usePostBoard();
  const { mutate: editBoard } = usePutBoard();

  useEffect(() => {
    if (type === 'update' && prevformData) {
      setFormData({
        title: prevformData.title,
        content: prevformData.content,
        imgUrls:
          prevformData.imgUrls?.map((obj: UploadedImageObj) => ({
            thumbnail: obj.imgUrl,
            isExisting: true,
            hash: obj.hash,
          })) || [],
      });
      setExistingHashes(prevformData.imgUrls?.map((obj: UploadedImageObj) => obj.hash) || []);
    }
  }, [prevformData, type]);

  const handleClickFileInput = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    fileInputRef.current?.click();
  };

  const handleImgRemove = (index: number) => {
    const removedImage = formData.imgUrls[index];
    setFormData((prev) => ({
      ...prev,
      imgUrls: prev.imgUrls.filter((_, i) => i !== index),
    }));
    setExistingHashes((prev) => prev.filter((hash) => hash !== removedImage.hash));
  };

  const uploadProfile = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileList = e.target.files;
    if (!fileList) return;

    if (formData.imgUrls.length + fileList.length > 10) {
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
      imgUrls: [...prev.imgUrls, ...newImages],
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    let uploadedObjs: UploadedImageObj[] = [];

    try {
      const newImages = formData.imgUrls.filter((img) => !img.isExisting);
      uploadedObjs = newImages.length > 0 ? await handleImageUpload(newImages) : [];
    } catch (error) {
      alert('이미지 업로드에 실패했습니다. 다시 시도해주세요.');
      console.error(error);
      return;
    }

    const existingObjs: UploadedImageObj[] = formData.imgUrls
      .filter((img) => img.isExisting)
      .map((img) => ({
        imgUrl: img.thumbnail,
        hash: img.hash,
      }));

    // 삭제된 이미지 판별
    const existingLinks = existingObjs.map((obj) => obj.imgUrl);
    const prevLinks = prevformData?.imgUrls?.map((obj: UploadedImageObj) => obj.imgUrl) || [];
    const removedLinks = prevLinks.filter((link: string) => !existingLinks.includes(link));
    await handleDeleteImages(removedLinks);

    // 최종 업로드할 이미지 객체 배열
    const allImageObjs = [...existingObjs, ...uploadedObjs];

    const formDataWithURL = {
      title: formData.title,
      content: formData.content,
      imgUrls: allImageObjs,
    };
    if (type === 'create') {
      postBoard(formDataWithURL, {
        onSuccess: () => {
          queryClient.invalidateQueries({ queryKey: ['infiniteBoardList'] });
          navigate('/board');
        },
        onError: () => alert('게시글 등록을 실패했습니다. 다시 시도해주세요!'),
      });
    } else {
      editBoard(
        { boardId, formData: formDataWithURL },
        {
          onSuccess: () => {
            navigate(`/detail/${boardId}`);
            queryClient.invalidateQueries({ queryKey: ['infiniteBoardList'] });
          },
          onError: () => alert('게시글 수정을 실패했습니다. 다시 시도해주세요!'),
        },
      );
    }
  };

  return (
    <PostContainer>
      <Form onSubmit={handleSubmit}>
        <DetailHeader>
          <BackBtn type="button" onClick={() => navigate(-1)}>
            <IoIosArrowBack size={24} />
          </BackBtn>
          <Text size="s" weight="bold">
            글 쓰기
          </Text>
          <SubmitButton type="submit">등록</SubmitButton>
        </DetailHeader>
        <FileInput
          type="file"
          accept="image/jpg, image/jpeg, image/png"
          ref={fileInputRef}
          onChange={uploadProfile}
          multiple
        />
        <InputField
          type="text"
          placeholder="제목을 입력해주세요"
          value={formData.title}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => setFormData({ ...formData, title: e.target.value })}
          required
        />
        <Separator />
        <TextArea
          placeholder={`자유롭게 입력하세요.

욕설, 비방, 차별, 혐오, 근거 없는 악의적 후기 등 타인의 권리를 침해하는 행위 시 게시물이 삭제되고 서비스 이용이 제한될 수 있습니다.`}
          rows={1}
          value={formData.content}
          onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) =>
            setFormData({ ...formData, content: e.target.value })
          }
        />
        <ImagePreview
          images={formData.imgUrls}
          onRemove={handleImgRemove}
          onPreview={(src) => {
            setSelectedImage(src);
            setIsModalOpen(true);
          }}
          onAddClick={handleClickFileInput}
        />
      </Form>
      {isModalOpen && (
        <ModalOverlay onClick={() => setIsModalOpen(false)}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            <img src={selectedImage} alt="확대 이미지" style={{ maxWidth: '90vw', maxHeight: '90vh' }} />
            <CloseBtn type="button" onClick={() => setIsModalOpen(false)}>
              <IoMdClose size={30} />
            </CloseBtn>
          </ModalContent>
        </ModalOverlay>
      )}
    </PostContainer>
  );
}

const PostContainer = styled.div`
  width: 100%;
  margin-top: 20px;
`;
const DetailHeader = styled.div`
  z-index: 10;
  width: 100%;
  padding: 10px 0px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const BackBtn = styled.button`
  background: none;
  border: none;
  cursor: pointer;

  svg {
    color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : 'black')};
  }
`;
const Form = styled.form`
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 20px;
  align-items: center;
`;
const FileInput = styled.input`
  display: none;
`;
const InputField = styled.input`
  width: 100%;
  padding: 10px;
  border: none;
  box-sizing: border-box;
  background: transparent;
  border-radius: 5px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : 'black')};
  font-size: 20px;

  &:focus {
    outline: none;
    border: none;
  }
  &::placeholder {
    color: #979797;
  }
`;

const TextArea = styled.textarea`
  width: 100%;
  height: 300px;
  padding: 10px;
  box-sizing: border-box;
  font-size: 14px;
  line-height: 1.4;
  display: flex;
  border: none;
  background: transparent;
  overflow-y: hidden;
  resize: none;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : 'black')};

  &::placeholder {
    color: #979797;
  }

  &:focus {
    outline: none;
    border: none;
  }
`;

const SubmitButton = styled.button`
  border: none;
  background: none;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#55ebff' : '#47c8d9')};
  font-weight: bold;
  font-size: 16px;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
`;

const Separator = styled.div`
  height: 1px;
  width: 100%;
  background-color: #6d6d6d;
`;

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;
const ModalContent = styled.div`
  position: relative;
  background: transparent;
  img {
    object-fit: contain;
  }
`;
const CloseBtn = styled.button`
  position: absolute;
  background: transparent;
  border: none;
  aspect-ratio: 1/1;
  display: flex;
  align-items: center;
  color: white;
  cursor: pointer;
  right: 2px;
  top: 6px;
`;
