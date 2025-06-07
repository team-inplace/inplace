import styled from 'styled-components';
// import { PiHeartLight } from 'react-icons/pi';
import { RxDotsVertical } from 'react-icons/rx';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';
import { CommentData } from '@/types';

export default function CommentItem({ item }: { item: CommentData }) {
  return (
    <Wrapper>
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
      {/* <ItemInfo>
          <PiHeartLight color="#A9A9A9" size={18} data-testid="PiHeartLight" />
          <Text size="xs" weight="normal" variant="#A9A9A9">
            {item.like}
          </Text>
        </ItemInfo> */}
      <EditMenu>
        <RxDotsVertical size={22} />
      </EditMenu>
    </Wrapper>
  );
}

const Wrapper = styled.button`
  position: relative;
  display: flex;
  padding: 14px;
  max-height: 150px;
  border-radius: 16px;
  background: none;
  border: none;
  align-items: start;
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
  position: absolute;
  right: 0px;
  background: none;
  border: none;

  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  }
`;
