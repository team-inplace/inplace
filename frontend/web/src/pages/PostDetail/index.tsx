import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { IoIosArrowBack, IoIosArrowForward, IoMdClose } from 'react-icons/io';
import { useCallback, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Text } from '@/components/common/typography/Text';
import FallbackImage from '@/components/common/Items/FallbackImage';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { useGetPostData } from '@/api/hooks/useGetPostData';
import Button from '@/components/common/Button';
import Comment from '@/components/PostDetail/Comment';
import useAuth from '@/hooks/useAuth';
import { usePostPostLike } from '@/api/hooks/usePostPostLike';
import LoginModal from '@/components/common/modals/LoginModal';
import { useDeletePost } from '@/api/hooks/useDeletePost';
import EditMenu from '@/components/PostDetail/EditMenu';
import UserName from '@/components/PostDetail/UserName';
import useIsMobile from '@/hooks/useIsMobile';
import ReportModal from '@/components/common/modals/ReportModal';
import { usePostReportPost } from '@/api/hooks/usePostReportPost';

export default function PostDetailPage() {
  const { isAuthenticated } = useAuth();
  const { id } = useParams() as { id: string };
  const location = useLocation();
  const navigate = useNavigate();
  const { activeCategory = '전체 게시글' } = location.state || {};
  const queryClient = useQueryClient();
  const isMobile = useIsMobile();

  const { data: postData } = useGetPostData(id);
  const { mutate: deletePost } = useDeletePost();
  const { mutate: reportPost } = usePostReportPost();
  const { mutate: postLike } = usePostPostLike();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [reportStatus, setReportStatus] = useState<'idle' | 'success' | 'error'>('idle');
  const [selectedImageIndex, setSelectedImageIndex] = useState<number>(0);

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

  const handleReportSubmit = async (type: string, content: string) => {
    const reason = content ? `${type} : ${content}` : type;
    try {
      await new Promise<void>((resolve, reject) => {
        reportPost(
          { id: Number(id), reason },
          {
            onSuccess: () => {
              setReportStatus('success');
              resolve();
            },
            onError: (err) => {
              setReportStatus('error');
              reject(err);
            },
          },
        );
      });
    } catch (e) {
      console.error('신고 실패');
    }
  };
  const handleReportClose = () => {
    setIsReportModalOpen(false);
    setReportStatus('idle');
  };
  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      postLike({ postId: Number(id), likes: !postData.selfLike });
    },
    [postData.selfLike, id, postLike, isAuthenticated],
  );
  const formData = {
    title: postData.title,
    content: postData.content,
  };

  const imageUrls = postData.imageUrls ?? [];

  const openImageModal = (idx: number) => {
    setSelectedImageIndex(idx);
    setIsModalOpen(true);
  };

  const showPrevImage = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    setSelectedImageIndex((prev) => (prev === 0 ? imageUrls.length - 1 : prev - 1));
  };

  const showNextImage = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    setSelectedImageIndex((prev) => (prev === imageUrls.length - 1 ? 0 : prev + 1));
  };

  return (
    <Wrapper>
      <CategoryName to="/post">
        <Text size="s" weight="bold">
          {activeCategory}
        </Text>
        <IoIosArrowForward size={isMobile ? 16 : 20} />
      </CategoryName>
      <PostContainer>
        <PostTop>
          <UserInfo>
            <ProfileImg>
              <FallbackImage src={postData.author.imgUrl} alt="profile" />
            </ProfileImg>
            <UserTitleTop>
              <UserName
                userNickname={postData.author.nickname}
                tierImageUrl={postData.author.tierImageUrl}
                badgeImageUrl={postData.author.badgeImageUrl}
              />
              <StyledText size="s" weight="normal">
                {postData.createdAt}
              </StyledText>
            </UserTitleTop>
          </UserInfo>
          <EditMenu
            mine={postData.isMine}
            onEdit={() => handleEditPost(id, formData)}
            onDelete={() => handleDeletePost(id)}
            onReport={() => setIsReportModalOpen(true)}
            ariaLabels="게시글"
          />
        </PostTop>
        <Content>
          <Paragraph size="m" weight="bold">
            {postData.title}
          </Paragraph>
          <StyledText size="s" weight="normal">
            {postData.content}
          </StyledText>
        </Content>
        {postData.imageUrls && (
          <ImageList>
            {postData.imageUrls.map((imgUrl, index) => (
              <PostImg
                key={imgUrl.imageUrl}
                src={imgUrl.imageUrl}
                alt={`게시글 이미지 ${index}`}
                onClick={() => {
                  openImageModal(index);
                }}
              />
            ))}
          </ImageList>
        )}
        <CountContainer
          role="button"
          aria-label="게시글 좋아요 버튼"
          onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}
        >
          <Count>
            {postData.selfLike ? (
              <PiHeartFill color="#fe7373" size={isMobile ? 14 : 18} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight size={isMobile ? 14 : 18} data-testid="PiHeartLight" />
            )}
            <Text size="s" weight="normal">
              {postData.totalLikeCount ?? 0}
            </Text>
          </Count>
        </CountContainer>
      </PostContainer>
      <CommentTitle>
        <Text size="s" weight="normal">
          댓글 {postData.totalCommentCount ?? 0}건
        </Text>
      </CommentTitle>
      <Separator />
      <Comment id={id} />
      <StyledButton size="small" variant="outline" onClick={() => navigate('/post')}>
        {isMobile ? '목록' : '목록보기'}
      </StyledButton>
      {isModalOpen && (
        <ModalOverlay onClick={() => setIsModalOpen(false)}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            <ImageIndex>
              {selectedImageIndex + 1} / {imageUrls.length}
            </ImageIndex>
            {imageUrls.length > 1 && (
              <>
                <ArrowBtnLeft type="button" onClick={showPrevImage} aria-label="이전 이미지">
                  <IoIosArrowBack size={36} />
                </ArrowBtnLeft>
                <ArrowBtnRight type="button" onClick={showNextImage} aria-label="다음 이미지">
                  <IoIosArrowForward size={36} />
                </ArrowBtnRight>
              </>
            )}
            <img src={imageUrls[selectedImageIndex]?.imageUrl} alt="확대 이미지" />
            <CloseBtn type="button" onClick={() => setIsModalOpen(false)}>
              <IoMdClose size={isMobile ? 20 : 24} />
            </CloseBtn>
          </ModalContent>
        </ModalOverlay>
      )}
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
      {isReportModalOpen && (
        <ReportModal onClose={handleReportClose} onSubmit={handleReportSubmit} status={reportStatus} />
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
  @media screen and (max-width: 768px) {
    width: 90%;
  }
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
  @media screen and (max-width: 768px) {
    gap: 18px;
    padding: 10px 0px;
  }
`;
const PostTop = styled.div`
  display: flex;
  justify-content: space-between;
`;
const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 14px;
  @media screen and (max-width: 768px) {
    gap: 10px;
  }
`;

const UserTitleTop = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  @media screen and (max-width: 768px) {
    gap: 4px;
  }
`;
const UserInfo = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: center;
  @media screen and (max-width: 768px) {
    gap: 6px;
  }
`;
const ProfileImg = styled.div`
  height: 56px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;

  @media screen and (max-width: 768px) {
    height: 50px;
  }
`;

const CountContainer = styled.div`
  width: 70px;
  border: 0.5px solid #838383;
  border-radius: 6px;
  padding: 8px 2px 6px 2px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#232323' : '#deeeee')};
  }
  @media screen and (max-width: 768px) {
    width: 50px;
    border: 1px solid #515151;
  }
`;

const Count = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 4px;
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : '#505050')};
  }
  span {
    line-height: 120%;
  }
  @media screen and (max-width: 768px) {
    gap: 2px;
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
  @media screen and (max-width: 768px) {
    gap: 6px;
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

  @media screen and (max-width: 768px) {
    border-radius: 10px;
    max-width: 90%;
  }
`;

const StyledButton = styled(Button)`
  width: 90px;
  margin-left: 90%;
  cursor: pointer;
  @media screen and (max-width: 768px) {
    width: 60px;
    margin-left: 80%;
  }
`;

const StyledText = styled(Text)`
  line-height: 120%;
  white-space: pre-line;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#c5c5c5' : '#606060')};
  @media screen and (max-width: 768px) {
    line-height: 140%;
  }
`;

const Separator = styled.div`
  height: 1px;
  width: 100%;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#6d6d6d' : '#d4d4d4')};
`;

const CommentTitle = styled.div`
  width: 100%;
  padding: 0px 0px 8px 0px;
  text-align: left;

  @media screen and (max-width: 768px) {
    padding: 0;
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
    width: 100%;
    object-fit: contain;
  }
  @media screen and (max-width: 768px) {
    width: 100%;
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
const ArrowBtnLeft = styled.button`
  position: absolute;
  left: -50px;
  top: 50%;
  transform: translateY(-50%);
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  z-index: 2;
  @media screen and (max-width: 768px) {
    left: 0;
  }
`;

const ArrowBtnRight = styled.button`
  position: absolute;
  right: -50px;
  top: 50%;
  transform: translateY(-50%);
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  z-index: 2;
  @media screen and (max-width: 768px) {
    right: 0;
  }
`;

const ImageIndex = styled.div`
  position: absolute;
  top: -14%;
  left: 50%;
  transform: translateX(-50%);
  color: #fff;
  font-size: 16px;
  z-index: 3;
  pointer-events: none;
`;
