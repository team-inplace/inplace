import { useLocation, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { PiHeartLight } from 'react-icons/pi';
import { RxDotsVertical } from 'react-icons/rx';
import { HiOutlineChatBubbleOvalLeft } from 'react-icons/hi2';
import { IoIosArrowForward } from 'react-icons/io';
import { useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Text } from '@/components/common/typography/Text';
import FallbackImage from '@/components/common/Items/FallbackImage';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { useGetBoardData } from '@/api/hooks/useGetBoardData';
import Button from '@/components/common/Button';
import Comment from '@/components/BoardDetail/Comment';
import { useDeleteBoard } from '@/api/hooks/useDeleteBoard';
import useClickOutside from '@/hooks/useClickOutside';

export default function BoardDetailPage() {
  const { id } = useParams() as { id: string };
  const location = useLocation();
  const navigate = useNavigate();
  const editRef = useRef<HTMLDivElement>(null);
  const { activeCategory } = location.state;
  const queryClient = useQueryClient();

  const { data: boardData } = useGetBoardData(id);
  const { mutate: deleteBoard } = useDeleteBoard();
  const [showEditOptions, setShowEditOptions] = useState(false);

  const handleEditBoard = (boardId: string, formData: object) => {
    navigate('/board/post', { state: { boardId, prevformData: formData, type: 'update' } });
  };

  const handleDeleteBoard = (boardId: string) => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteBoard(boardId, {
      onSuccess: () => {
        alert('삭제되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['infiniteBoardList'] });
        navigate('/board');
      },
      onError: () => {
        alert('게시글 삭제에 실패했어요. 다시 시도해주세요!');
      },
    });
  };

  const formData = {
    title: boardData.title,
    content: boardData.content,
    contentImgUrls: boardData.contentImgUrls,
  };

  useClickOutside([editRef], () => {
    setShowEditOptions(false);
  });

  return (
    <Wrapper>
      <CategoryName>
        <Text size="s" weight="bold">
          {activeCategory}
        </Text>
        <IoIosArrowForward size={20} />
      </CategoryName>
      <BoardContainer>
        <BoardTop>
          <UserInfo>
            <ProfileImg>
              <FallbackImage src={boardData.userImgUrl} alt="profile" />
            </ProfileImg>
            <ProfileText>
              <Text size="s" weight="normal">
                {boardData.userNickname}
              </Text>
              {/* todo - 칭호 */}
              <Text size="xs" weight="normal" variant="#A9A9A9">
                {boardData.create}
              </Text>
            </ProfileText>
          </UserInfo>
          <EditMenu ref={editRef}>
            <EditBtn aria-label="게시글 편집 버튼" onClick={() => setShowEditOptions(!showEditOptions)}>
              <RxDotsVertical size={22} />
            </EditBtn>
            {showEditOptions && (
              <EditDropdown>
                <EditItem onClick={() => handleEditBoard(id, formData)}>수정</EditItem>
                <EditItem onClick={() => handleDeleteBoard(id)}>삭제</EditItem>
                <EditItem>신고</EditItem>
              </EditDropdown>
            )}
          </EditMenu>
        </BoardTop>
        <Content>
          <Paragraph size="m" weight="bold">
            {boardData.title}
          </Paragraph>
          <Paragraph size="xs" weight="normal" variant="#D4D4D4">
            {boardData.content}
          </Paragraph>
        </Content>
        {boardData.contentImgUrls && (
          <ImageList>
            {boardData.contentImgUrls.map((imgUrl, index) => (
              <BoardImg key={imgUrl} src={imgUrl} alt={`게시글 이미지 ${index}`} />
            ))}
          </ImageList>
        )}

        {/* todo - 이미지 여러개 나열 */}
        <ItemInfo>
          <Count>
            <PiHeartLight color="white" size={22} data-testid="PiHeartLight" />
            {/* todo - 좋아요 클릭 */}
            <Text size="s" weight="normal">
              {boardData.like}
            </Text>
          </Count>
          <Count>
            <HiOutlineChatBubbleOvalLeft color="white" size={22} />
            <Text size="s" weight="normal">
              {boardData.comment}
            </Text>
          </Count>
        </ItemInfo>
      </BoardContainer>
      <Comment id={id} />
      <StyledButton size="small" variant="outline" onClick={() => navigate('/board')}>
        목록보기
      </StyledButton>
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
const CategoryName = styled.div`
  display: flex;
  gap: 4px;
  align-items: end;
`;
const BoardContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 26px;
  border-bottom: 1px solid #6d6d6d;
  padding: 10px 0px 30px;
`;
const BoardTop = styled.div`
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
const ItemInfo = styled.div`
  display: flex;
  gap: 14px;
  align-items: end;
`;
const ProfileText = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6px;
`;
const Count = styled.div`
  align-items: end;
  display: flex;
  gap: 4px;
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
const BoardImg = styled.img`
  border-radius: 16px;
  max-width: 30%;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  scroll-snap-align: start;
  flex-shrink: 0;
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

  &:hover {
    background-color: #e9e9e9;
  }
`;
