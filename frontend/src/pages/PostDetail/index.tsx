import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { RxDotsVertical } from 'react-icons/rx';
import { IoIosArrowForward, IoMdClose } from 'react-icons/io';
import { useCallback, useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Text } from '@/components/common/typography/Text';
import FallbackImage from '@/components/common/Items/FallbackImage';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { useGetPostData } from '@/api/hooks/useGetPostData';
import Button from '@/components/common/Button';
import Comment from '@/components/PostDetail/Comment';
import useClickOutside from '@/hooks/useClickOutside';
import useAuth from '@/hooks/useAuth';
import { usePostPostLike } from '@/api/hooks/usePostPostLike';
import LoginModal from '@/components/common/modals/LoginModal';
import { useDeletePost } from '@/api/hooks/useDeletePost';

export default function PostDetailPage() {
  const { isAuthenticated } = useAuth();
  const { id } = useParams() as { id: string };
  const location = useLocation();
  const navigate = useNavigate();
  const editRef = useRef<HTMLDivElement>(null);
  const { activeCategory } = location.state;
  const queryClient = useQueryClient();

  const { data: postData } = useGetPostData(id);
  const { mutate: deletePost } = useDeletePost();
  const { mutate: postLike } = usePostPostLike();
  const [showEditOptions, setShowEditOptions] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [isLike, setIsLike] = useState(postData.selfLike);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedImage, setSelectedImage] = useState<string>('');

  const handleEditPost = (postId: string, formData: object) => {
    navigate('/posting', { state: { postId, prevformData: formData, type: 'update' } });
  };

  const handleDeletePost = (postId: string) => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deletePost(postId, {
      onSuccess: () => {
        alert('삭제되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['infinitePostList'] });
        navigate('/post');
      },
      onError: () => {
        alert('게시글 삭제에 실패했어요. 다시 시도해주세요!');
      },
    });
  };

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      const newLikeStatus = !isLike;
      postLike(
        { postId: Number(id), likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['PostData', id] });
          },
          onError: () => {
            alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
          },
        },
      );
    },
    [isLike, id, postLike],
  );
  const formData = {
    title: postData.title,
    content: postData.content,
    imageUrls: postData.imageUrls,
  };

  useClickOutside([editRef], () => {
    setShowEditOptions(false);
  });

  return (
    <Wrapper>
      <CategoryName to="/post">
        <Text size="s" weight="bold">
          {activeCategory}
        </Text>
        <IoIosArrowForward size={20} />
      </CategoryName>
      <PostContainer>
        <PostTop>
          <UserInfo>
            <ProfileImg>
              <FallbackImage src={postData.userImgUrl} alt="profile" />
            </ProfileImg>
            <ProfileText>
              <Text size="s" weight="normal">
                {postData.userNickname}
              </Text>
              {/* todo - 칭호 */}
              <StyledText size="xs" weight="normal">
                {postData.create}
              </StyledText>
            </ProfileText>
          </UserInfo>
          <EditMenu ref={editRef}>
            <EditBtn aria-label="게시글 편집 버튼" onClick={() => setShowEditOptions(!showEditOptions)}>
              <RxDotsVertical size={22} />
            </EditBtn>
            {showEditOptions && (
              <EditDropdown>
                {postData.mine ? (
                  <>
                    <EditItem onClick={() => handleEditPost(id, formData)}>수정</EditItem>
                    <EditItem onClick={() => handleDeletePost(id)}>삭제</EditItem>
                  </>
                ) : null}
                <EditItem>신고</EditItem>
              </EditDropdown>
            )}
          </EditMenu>
        </PostTop>
        <Content>
          <Paragraph size="m" weight="bold">
            {postData.title}
          </Paragraph>
          <StyledText size="xs" weight="normal">
            {postData.content}
          </StyledText>
        </Content>
        {postData.imageUrls && (
          <ImageList>
            {postData.imageUrls.map((imgUrl, index) => (
              <PostImg
                key={imgUrl.hash}
                src={imgUrl.imageUrl}
                alt={`게시글 이미지 ${index}`}
                onClick={() => {
                  setSelectedImage(imgUrl.imageUrl);
                  setIsModalOpen(true);
                }}
              />
            ))}
          </ImageList>
        )}
        <Count
          role="button"
          aria-label="게시글 좋아요 버튼"
          onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}
        >
          {isLike ? (
            <PiHeartFill color="#fe7373" size={18} data-testid="PiHeartFill" />
          ) : (
            <PiHeartLight size={18} data-testid="PiHeartLight" />
          )}
          <Text size="xs" weight="normal">
            {postData.totalLikeCount}
          </Text>
        </Count>
      </PostContainer>
      <CommentTitle>
        <Text size="xs" weight="normal">
          댓글 {postData.commentCount}건
        </Text>
      </CommentTitle>
      <Separator />
      <Comment id={id} />
      <StyledButton size="small" variant="outline" onClick={() => navigate('/post')}>
        목록보기
      </StyledButton>
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
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </Wrapper>
  );
}
const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding-top: 20px;
`;
const CategoryName = styled(Link)`
  display: flex;
  gap: 4px;
  align-items: end;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
`;
const PostContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 26px;
  padding: 10px 0px 30px;
`;
const PostTop = styled.div`
  display: flex;
  justify-content: space-between;
`;
const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 14px;
`;
const UserInfo = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: center;
`;
const ProfileImg = styled.div`
  height: 56px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
`;

const ProfileText = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6px;
`;
const Count = styled.div`
  width: 70px;
  border: 0.5px solid #838383;
  border-radius: 6px;
  align-items: end;
  justify-content: center;
  padding: 8px 2px;
  display: flex;
  gap: 4px;
  cursor: pointer;
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#A9A9A9' : '#000000')};
  }
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#232323' : '#deeeee')};
  }
`;
const ImageList = styled.div`
  display: flex;
  gap: 10px;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;
const PostImg = styled.img`
  border-radius: 16px;
  max-width: 30%;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  scroll-snap-align: start;
  flex-shrink: 0;
  cursor: pointer;
`;

const EditMenu = styled.div`
  position: relative;
`;

const EditBtn = styled.button`
  background: none;
  border: none;
  cursor: pointer;

  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  }
`;

const StyledButton = styled(Button)`
  width: 90px;
  margin-left: 90%;
  cursor: pointer;
`;

const EditDropdown = styled.div`
  position: absolute;
  top: 50%;
  right: 10px;
  z-index: 2;
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 90px;
`;

const EditItem = styled.button`
  width: 100%;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  background: none;
  border: none;
  cursor: pointer;

  &:hover {
    background-color: #e9e9e9;
    border-radius: 4px;
  }
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
const StyledText = styled(Text)`
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#c5c5c5' : '#606060')};
`;

const Separator = styled.div`
  height: 1px;
  width: 100%;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#6d6d6d' : '#d4d4d4')};
`;

const CommentTitle = styled.div`
  width: 70px;
  padding: 0px 0px xpx 8px;
  text-align: center;
`;
