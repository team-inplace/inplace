import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { RxDotsVertical } from 'react-icons/rx';
import { useCallback, useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { useLocation } from 'react-router-dom';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';
import { CommentData } from '@/types';
import { useDeleteComment } from '@/api/hooks/useDeleteComment';
import useClickOutside from '@/hooks/useClickOutside';
import useAuth from '@/hooks/useAuth';
import { usePostCommentLike } from '@/api/hooks/usePostCommentLike';
import LoginModal from '../common/modals/LoginModal';

export default function CommentItem({ item, postId }: { item: CommentData; postId: string }) {
  const { isAuthenticated } = useAuth();
  const [showEditOptions, setShowEditOptions] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [isLike, setIsLike] = useState(item.likes);
  const location = useLocation();
  const queryClient = useQueryClient();
  const editRef = useRef<HTMLDivElement>(null);
  const { mutate: deleteComment } = useDeleteComment();
  const { mutate: postLike } = usePostCommentLike();

  const handleDeleteComment = (id: number) => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteComment(id.toString(), {
      onSuccess: () => {
        alert('삭제되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['infiniteCommentList', 10, postId] });
      },
      onError: () => {
        alert('댓글 삭제에 실패했어요. 다시 시도해주세요!');
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
        { commentId: Number(item.commentId), likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['infiniteCommentList', postId] });
          },
          onError: () => {
            alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
          },
        },
      );
    },
    [isLike, item.commentId, postLike],
  );

  useClickOutside([editRef], () => {
    setShowEditOptions(false);
  });

  return (
    <Wrapper>
      <LeftInfo>
        <ProfileImg>
          <FallbackImage src={item.userImgUrl} alt="profile" />
        </ProfileImg>
        <Content>
          <Text size="s" weight="normal">
            {item.userNickname}
          </Text>
          <Paragraph size="xs" weight="normal">
            {item.content}
          </Paragraph>
          <CommentInfo>
            <StyledText size="xs" weight="normal">
              {item.create}
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
                {item.like}
              </Text>
            </Count>
          </CommentInfo>
        </Content>
      </LeftInfo>
      <EditMenu ref={editRef}>
        <EditBtn aria-label="게시글 편집 버튼" onClick={() => setShowEditOptions(!showEditOptions)}>
          <RxDotsVertical size={22} />
        </EditBtn>
        {showEditOptions && (
          <EditDropdown>
            {item.mine ? (
              <EditItem aria-label="댓글 삭제 버튼" onClick={() => handleDeleteComment(item.commentId)}>
                삭제
              </EditItem>
            ) : null}
            <EditItem>신고</EditItem>
          </EditDropdown>
        )}
      </EditMenu>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  padding: 14px;
  border-radius: 16px;
  background: none;
  border: none;
  justify-content: space-between;
  gap: 8px;
`;
const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  p {
    line-height: 150%;
  }
`;
const LeftInfo = styled.div`
  display: flex;
  gap: 8px;
`;
const CommentInfo = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
`;
const ProfileImg = styled.div`
  height: 34px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
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
const EditDropdown = styled.div`
  position: absolute;
  top: 40%;
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
