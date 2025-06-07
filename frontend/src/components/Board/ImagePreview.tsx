import { TfiPlus } from 'react-icons/tfi';
import { IoMdClose } from 'react-icons/io';
import styled from 'styled-components';

type ImagePreviewProps = {
  images: { thumbnail: string }[];
  onRemove?: (index: number) => void;
  onPreview?: (src: string) => void;
  onAddClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
};

export default function ImagePreview({ images, onRemove, onPreview, onAddClick }: ImagePreviewProps) {
  return (
    <ImageWrapper>
      <BlankCamera onClick={onAddClick}>
        <TfiPlus size={70} strokeWidth={0.1} color="#b7b7b7" />
      </BlankCamera>
      {images.map((image, index) => (
        <ShowFileWapper key={image.thumbnail} onClick={() => onPreview?.(image.thumbnail)}>
          <ShowFileImage src={image.thumbnail} alt={`image-${index}`} />
          <ShowFileDeleteBtn
            type="button"
            onClick={(e) => {
              e.stopPropagation();
              onRemove?.(index);
            }}
          >
            <IoMdClose size={14} />
          </ShowFileDeleteBtn>
        </ShowFileWapper>
      ))}
    </ImageWrapper>
  );
}

const ImageWrapper = styled.div`
  width: 100%;
  display: flex;
  overflow-x: auto;
  overflow-y: hidden;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
  gap: 20px;
  scroll-padding-left: 200px;
  &::-webkit-scrollbar {
    display: none;
  }
`;
const ShowFileWapper = styled.div`
  position: relative;
  width: 200px;
  height: 200px;
  flex-shrink: 0;
  flex-grow: 0;
`;
const ShowFileImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
  cursor: pointer;
`;

const ShowFileDeleteBtn = styled.button`
  position: absolute;
  background: #191919;
  border: none;
  border-radius: 50%;
  aspect-ratio: 1/1;
  display: flex;
  align-items: center;
  color: white;
  cursor: pointer;
  right: 2px;
  top: 6px;
  z-index: 2;
`;

const BlankCamera = styled.button`
  width: 200px;
  height: 200px;
  box-sizing: border-box;
  background-color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#242424' : '#e7e7e7')};
  border: ${({ theme }) => (theme.textColor === '#ffffff' ? '1px solid #b7b7b7' : 'none')};
  align-content: center;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 12px;
  cursor: pointer;
  flex-shrink: 0;
  flex-grow: 0;
  padding: 0;
`;
