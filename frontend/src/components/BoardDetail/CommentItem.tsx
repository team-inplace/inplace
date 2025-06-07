import styled from 'styled-components';
// import { PiHeartLight } from 'react-icons/pi';
import { RxDotsVertical } from 'react-icons/rx';
import { useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';
import { CommentData } from '@/types';
import { useDeleteComment } from '@/api/hooks/useDeleteComment';
import useClickOutside from '@/hooks/useClickOutside';

export default function CommentItem({ item, boardId }: { item: CommentData; boardId: string }) {
  const [showEditOptions, setShowEditOptions] = useState(false);
  const queryClient = useQueryClient();
  const editRef = useRef<HTMLDivElement>(null);
  const { mutate: deleteComment } = useDeleteComment();

  const handleDeleteComment = (id: number) => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteComment(id.toString(), {
      onSuccess: () => {
        alert('삭제되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['infiniteCommenList', 10, boardId] });
      },
      onError: () => {
        alert('댓글 삭제에 실패했어요. 다시 시도해주세요!');
      },
    });
  };

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
          <UserInfo>
            <Text size="s" weight="normal">
              {item.userNickname}
            </Text>
            <Text size="xs" weight="normal" variant="#A9A9A9">
              {item.create}
            </Text>
          </UserInfo>
          <Paragraph size="xs" weight="normal">
            {item.content}
          </Paragraph>
        </Content>
      </LeftInfo>
      {/* <ItemInfo>
          <PiHeartLight color="#A9A9A9" size={18} data-testid="PiHeartLight" />
          <Text size="xs" weight="normal" variant="#A9A9A9">
            {item.like}
          </Text>
        </ItemInfo> */}
      <EditMenu aria-label="댓글 편집 버튼" onClick={() => setShowEditOptions(!showEditOptions)}>
        <RxDotsVertical size={22} />
        {showEditOptions && (
          <EditDropdown>
            <EditItem onClick={() => handleDeleteComment(item.commentId)}>삭제</EditItem>
            <EditItem>신고</EditItem>
          </EditDropdown>
        )}
      </EditMenu>
    </Wrapper>
  );
}

const Wrapper = styled.button`
  display: flex;
  padding: 14px;
  border-radius: 16px;
  background: none;
  border: none;
  justify-content: space-between;
  gap: 8px;
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};
`;
const Content = styled.div`
  display: flex;
  flex-direction: column;
  text-align: start;
  gap: 10px;
  p {
    line-height: 150%;
  }
`;
const LeftInfo = styled.div`
  display: flex;
  gap: 8px;
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
const EditMenu = styled.button`
  height: fit-content;
  position: relative;
  background: none;
  border: none;
  cursor: pointer;
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  }
`;
const EditDropdown = styled.div`
  position: absolute;
  top: 100%;
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
