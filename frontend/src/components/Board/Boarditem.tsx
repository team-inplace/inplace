import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { HiOutlineChatBubbleOvalLeft } from 'react-icons/hi2';
import { Link } from 'react-router-dom';
import { BoardListData } from '@/types';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';

export default function Boarditem({ item, activeCategory }: { item: BoardListData; activeCategory: string }) {
  return (
    <Wrapper to={`/board/${item.boardId}`} state={{ activeCategory }}>
      <LeftInfo>
        <UserInfo>
          <ProfileImg>
            <FallbackImage src={item.userImgUrl} alt="profile" />
          </ProfileImg>
          <Text size="s" weight="normal" variant="white">
            {item.userNickname}
          </Text>
        </UserInfo>
        <Content>
          <Paragraph size="m" weight="bold" variant="white">
            {item.title}
          </Paragraph>
          <Paragraph size="s" weight="normal" variant="#D4D4D4">
            {item.content}
          </Paragraph>
        </Content>
        <ItemInfo>
          <Count>
            {item.likes ? (
              <PiHeartFill color="#fe7373" size={18} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight color="#A9A9A9" size={18} data-testid="PiHeartLight" />
            )}
            <Text size="xs" weight="normal" variant="#A9A9A9">
              {item.like}
            </Text>
          </Count>
          <Count>
            <HiOutlineChatBubbleOvalLeft color="#A9A9A9" size={18} />
            <Text size="xs" weight="normal" variant="#A9A9A9">
              {item.comment}
            </Text>
          </Count>
          <Text size="xs" weight="normal" variant="#A9A9A9">
            {item.create}
          </Text>
        </ItemInfo>
      </LeftInfo>
      {/* todo - 배열로 수정 */}
      {item.contentImgUrls && <BoardImg src={item.contentImgUrls[0]} />}
    </Wrapper>
  );
}

const Wrapper = styled(Link)`
  display: flex;
  justify-content: space-between;
  padding: 20px 10px;
  align-items: center;
  max-height: 150px;
  border-radius: 16px;
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }
`;
const LeftInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;
const Content = styled.div`
  padding: 10px 0px;
  display: flex;
  flex-direction: column;
  gap: 10px;
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
const ItemInfo = styled.div`
  display: flex;
  gap: 8px;
  color: #a9a9a9;
  align-items: end;
`;
const Count = styled.div`
  align-items: end;
  display: flex;
  gap: 1px;
`;

const BoardImg = styled.img`
  border-radius: 16px;
  height: 130px;
  aspect-ratio: 1 / 1;
`;
