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
          <Text size="s" weight="normal">
            {item.userNickname}
          </Text>
        </UserInfo>
        <Content>
          <Paragraph size="m" weight="bold">
            {item.title}
          </Paragraph>
          <StyledText size="s" weight="normal">
            {item.content}
          </StyledText>
        </Content>
        <ItemInfo>
          <Count>
            {item.likes ? (
              <PiHeartFill color="#fe7373" size={18} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight size={18} data-testid="PiHeartLight" />
            )}
            <StyledText size="xs" weight="normal">
              {item.like}
            </StyledText>
          </Count>
          <Count>
            <HiOutlineChatBubbleOvalLeft size={18} />
            <StyledText size="xs" weight="normal">
              {item.comment}
            </StyledText>
          </Count>
          <StyledText size="xs" weight="normal">
            {item.create}
          </StyledText>
        </ItemInfo>
      </LeftInfo>
      {item.imgUrls && <BoardImg src={item.imgUrls.imgUrl} />}
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
  color: ${(props) => props.theme.textColor};
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#eaf5f5')};
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
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : '#505050')};
  }
`;

const BoardImg = styled.img`
  border-radius: 16px;
  height: 130px;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  background-color: black;
`;
const StyledText = styled(Text)`
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#D4D4D4' : '#505050')};
`;
