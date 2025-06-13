import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { useCallback, useEffect, useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { useLocation } from 'react-router-dom';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';
import { CommentData } from '@/types';
import { useDeleteComment } from '@/api/hooks/useDeleteComment';
import useAuth from '@/hooks/useAuth';
import { usePostCommentLike } from '@/api/hooks/usePostCommentLike';
import LoginModal from '../common/modals/LoginModal';
import { usePutComment } from '@/api/hooks/usePutComment';
import Button from '../common/Button';
import useAutoResizeTextarea from '@/hooks/Post/useAutoResizeTextarea';
import useTheme from '@/hooks/useTheme';
import EditMenu from './EditMenu';
import UserName from './UserName';

export default function CommentItem({
  item,
  postId,
  currentPage,
}: {
  item: CommentData;
  postId: string;
  currentPage: number;
}) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const queryClient = useQueryClient();
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';

  const [showLoginModal, setShowLoginModal] = useState(false);
  const [isLike, setIsLike] = useState(item.selfLike);
  const [isEditing, setIsEditing] = useState(false);
  const [editValue, setEditValue] = useState(item.content);
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const { mutate: deleteComment } = useDeleteComment();
  const { mutate: putComment } = usePutComment();
  const { mutate: postLike } = usePostCommentLike();

  const handleResizeHeight = useAutoResizeTextarea();

  const handleDeleteSubmit = () => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteComment(
      { postId, id: item.commentId.toString() },
      {
        onSuccess: () => {
          alert('삭제되었습니다.');
          queryClient.invalidateQueries({ queryKey: ['commentList', currentPage, 10, postId] });
        },
        onError: () => {
          alert('댓글 삭제에 실패했어요. 다시 시도해주세요!');
        },
      },
    );
  };

  const handleEditClick = () => {
    setIsEditing(true);
    setEditValue(item.content);
  };

  const handleEditSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (editValue.trim() === item.content.trim()) {
      setIsEditing(false);
      return;
    }
    if (!editValue.trim()) {
      alert('댓글을 입력해주세요.');
      return;
    }

    putComment(
      { postId, commentId: item.commentId.toString(), comment: editValue },
      {
        onSuccess: () => {
          queryClient.invalidateQueries({ queryKey: ['commentList', currentPage, 10, postId] });
          setIsEditing(false);
        },
        onError: () => {
          alert('댓글 수정에 실패했어요. 다시 시도해주세요!');
        },
      },
    );
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
        { commentId: Number(item.commentId), likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['commentList', currentPage, 10, postId] });
          },
          onError: () => {
            alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
          },
        },
      );
    },
    [isLike, item.commentId, postLike],
  );

  useEffect(() => {
    if (isEditing && textareaRef.current) {
      textareaRef.current.focus();
      const len = textareaRef.current.value.length;
      textareaRef.current.setSelectionRange(len, len);
      handleResizeHeight(textareaRef.current);
    }
  }, [isEditing, handleResizeHeight]);

  return (
    <Wrapper>
      <CommentTop>
        <UserInfo>
          <ProfileImg>
            <FallbackImage src={item.author.imgUrl} alt="profile" />
          </ProfileImg>
          <UserName
            userNickname={item.author.nickname}
            tierImageUrl="https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff"
          />
        </UserInfo>
        {!isEditing && (
          <EditMenu
            mine={item.isMine}
            onEdit={handleEditClick}
            onDelete={handleDeleteSubmit}
            onReport={() => alert('신고 기능 준비중')}
            ariaLabels="댓글"
          />
        )}
      </CommentTop>
      <Content>
        {isEditing ? (
          <form onSubmit={handleEditSubmit} style={{ width: '100%' }}>
            <TextArea
              ref={textareaRef}
              value={editValue}
              onChange={(e) => {
                setEditValue(e.target.value);
                handleResizeHeight(e.target);
              }}
              rows={1}
            />
            <SubmitBtnWrapper>
              <StyledButton
                type="button"
                variant={isDarkMode ? 'outline' : 'blackOutline'}
                size="small"
                aria-label="댓글 수정 취소"
                onClick={() => {
                  setIsEditing(false);
                  setEditValue(item.content);
                }}
              >
                취소
              </StyledButton>
              <StyledButton
                type="submit"
                aria-label="댓글 수정 완료"
                variant={isDarkMode ? 'white' : 'black'}
                size="small"
              >
                완료
              </StyledButton>
            </SubmitBtnWrapper>
          </form>
        ) : (
          <>
            <Paragraph size="xs" weight="normal">
              {item.content}
            </Paragraph>
            <CommentInfo>
              <StyledText size="xs" weight="normal">
                {item.createAt}
              </StyledText>
              <Count
                role="button"
                aria-label="댓글 좋아요 버튼"
                onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}
              >
                {isLike ? (
                  <PiHeartFill color="#fe7373" size={18} data-testid="PiHeartFill" />
                ) : (
                  <PiHeartLight size={18} data-testid="PiHeartLight" />
                )}
                <Text size="xs" weight="normal">
                  {item.totalLikeCount}
                </Text>
              </Count>
            </CommentInfo>
          </>
        )}
      </Content>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding: 14px;
  border-radius: 16px;
  background: none;
  border: none;
  justify-content: space-between;
  gap: 8px;
`;
const Content = styled.div`
  width: calc(100%-40px);
  display: flex;
  white-space: pre-line;
  flex-direction: column;
  padding-left: 40px;
  gap: 8px;
  p {
    line-height: 150%;
  }
`;
const CommentTop = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
`;
const CommentInfo = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
`;
const UserInfo = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
`;
const ProfileImg = styled.div`
  height: 34px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
`;

const StyledText = styled(Text)`
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#A9A9A9' : '#929292')};
`;

const Count = styled.div`
  display: flex;
  gap: 4px;
  align-items: end;
  cursor: pointer;
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#A9A9A9' : '#000000')};
  }
`;

const TextArea = styled.textarea`
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  font-size: 14px;
  line-height: 1.4;
  border: 1px solid #c9c9c9;
  border-radius: 10px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#eefbfb')};
  overflow-y: hidden;
  resize: none;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  margin-bottom: 4px;
  &:focus {
    outline-color: #00c4c4;
  }
`;

const StyledButton = styled(Button)`
  width: 60px;
  font-size: 14px;
  height: 32px;
  cursor: pointer;
`;

const SubmitBtnWrapper = styled.div`
  margin-top: 6px;
  display: flex;
  width: 100%;
  gap: 10px;
  justify-content: flex-end;
`;
