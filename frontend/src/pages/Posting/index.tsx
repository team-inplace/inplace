import { IoMdClose } from 'react-icons/io';
import { styled } from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { usePostPost } from '@/api/hooks/usePostPost';
import { usePutPost } from '@/api/hooks/usePutPost';
import handleImageUpload from '@/libs/s3/handleImageUpload';
import handleDeleteImages from '@/libs/s3/handleImageDelete';
import ImagePreview from '@/components/Post/ImagePreview';
import PostFormHeader from '@/components/Posting/PostFormHeader';
import PostTitleInput from '@/components/Posting/PostTitleInput';
import PostContentTextArea from '@/components/Posting/PostContentTextArea';
import usePostForm from '@/hooks/Post/usePostForm';

export default function PostingPage() {
  const navigate = useNavigate();

  const {
    formData,
    fileInputRef,
    textareaRef,
    handleResizeHeight,
    isModalOpen,
    setIsModalOpen,
    selectedImage,
    setSelectedImage,
    handleClickFileInput,
    handleImgRemove,
    uploadProfile,
    handleSubmit,
    handleChange,
  } = usePostForm({
    initialFormData: { title: '', content: '', imageUrls: [] },
    postPost: usePostPost().mutate,
    editPost: usePutPost().mutate,
    handleImageUpload,
    handleDeleteImages,
  });

  return (
    <PostContainer>
      <Form onSubmit={handleSubmit}>
        <PostFormHeader onBack={() => navigate(-1)} />
        <FileInput
          type="file"
          accept="image/jpg, image/jpeg, image/png"
          ref={fileInputRef}
          onChange={uploadProfile}
          multiple
        />
        <PostTitleInput value={formData.title} onChange={handleChange('title')} />
        <Separator />
        <PostContentTextArea
          value={formData.content}
          onChange={handleChange('content')}
          textareaRef={textareaRef}
          handleResizeHeight={handleResizeHeight}
        />
        <ImagePreview
          images={formData.imageUrls}
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
