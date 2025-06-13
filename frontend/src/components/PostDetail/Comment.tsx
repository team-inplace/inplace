import styled from 'styled-components';
import { useRef, useState } from 'react';
import { IoIosSend } from 'react-icons/io';
import { useQueryClient } from '@tanstack/react-query';
import { useLocation } from 'react-router-dom';
import CommentItem from './CommentItem';
import { useGetCommentList } from '@/api/hooks/useGetCommentList';
import { usePostComment } from '@/api/hooks/usePostComment';
import useAuth from '@/hooks/useAuth';
import LoginModal from '../common/modals/LoginModal';
import FallbackImage from '../common/Items/FallbackImage';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';
import { Text } from '../common/typography/Text';
import useAutoResizeTextarea from '@/hooks/Post/useAutoResizeTextarea';
import Pagination from '../common/Pagination';
import Loading from '../common/layouts/Loading';
import NoItem from '../common/layouts/NoItem';

export default function Comment({ id }: { id: string }) {
  const location = useLocation();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const queryClient = useQueryClient();

  const { isAuthenticated } = useAuth();
  const { data: userInfo } = useGetUserInfo();
  const { mutate: postComment } = usePostComment();
  const { data: commentList, isLoading } = useGetCommentList(id, currentPage - 1, 10);

  const handleResizeHeight = useAutoResizeTextarea();

  const handleCommentSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!textareaRef.current) return;
    if (textareaRef.current.value === '') return;

    if (!isAuthenticated) {
      setShowLoginModal(true);
      return;
    }
    postComment(
      { postId: id, comment: textareaRef.current.value },
      {
        onSuccess: () => {
          if (!textareaRef.current) return;
          queryClient.invalidateQueries({ queryKey: ['commentList', currentPage, 10, id] }); // 좋아요 리스트 초기화
          textareaRef.current.value = '';
          textareaRef.current.style.height = 'auto';
        },
        onError: () => {
          alert('댓글 등록에 실패했어요. 다시 시도해주세요!');
        },
      },
    );
  };
  const handlePageChange = (pageNum: number) => {
    setCurrentPage(pageNum);
  };
  const placeholder = isAuthenticated ? '의견을 남겨주세요.' : '댓글을 작성하려면 로그인이 필요해요.';

  if (isLoading) {
    return (
      <Wrapper>
        <LoadingWrapper>
          <Loading size={30} />
        </LoadingWrapper>
      </Wrapper>
    );
  }

  return (
    <>
      <Wrapper>
        {!commentList || commentList.content.length === 0 ? (
          <NoItem message="댓글이 없습니다." alignItems="center" />
        ) : (
          <>
            {commentList.content.map((item) => (
              <CommentItem key={item.commentId} item={item} postId={id} currentPage={currentPage} />
            ))}
            <Pagination
              currentPage={currentPage}
              totalPages={commentList.totalPages}
              totalItems={commentList.totalElements}
              onPageChange={handlePageChange}
              itemsPerPage={commentList.pageable.pageSize}
            />
          </>
        )}
        {/* todo - 언급기능 */}
        <CommentContainer>
          <ProfileImg>
            <FallbackImage src={isAuthenticated ? userInfo?.imgUrl : ''} alt="profile" />
          </ProfileImg>
          <Content>
            <UserInfo>
              <Text size="s" weight="normal">
                {isAuthenticated ? userInfo?.nickname : `사용자`}
              </Text>
            </UserInfo>
            <CommentInputWrapper onSubmit={handleCommentSubmit}>
              <TextArea
                ref={textareaRef}
                placeholder={placeholder}
                rows={1}
                onChange={(e) => handleResizeHeight(e.target)}
              />
              <SendButton type="submit">
                <IoIosSend size={20} />
              </SendButton>
            </CommentInputWrapper>
          </Content>
        </CommentContainer>
      </Wrapper>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}

const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
`;
const CommentInputWrapper = styled.form`
  position: relative;
`;
const TextArea = styled.textarea`
  width: 100%;
  padding: 10px 50px 10px 10px;
  box-sizing: border-box;
  font-size: 14px;
  line-height: 1.4;
  display: flex;
  border: 1px solid #c9c9c9;
  border-radius: 10px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#eefbfb')};
  overflow-y: hidden;
  resize: none;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};

  &::placeholder {
    color: #8b8b8b;
  }

  &:focus {
    outline-color: #00c4c4;
  }
`;

const SendButton = styled.button`
  position: absolute;
  right: 10px;
  top: 50%;
  display: flex;
  transform: translateY(-50%);
  background: transparent;
  border: none;

  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  cursor: pointer;
`;

const CommentContainer = styled.div`
  display: flex;
  width: 100%;
  gap: 8px;
  padding: 14px;
  box-sizing: border-box;
`;
const Content = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: start;
  gap: 14px;
  p {
    line-height: 150%;
  }
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

const LoadingWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;
