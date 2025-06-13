import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { HiOutlineChatBubbleOvalLeft } from 'react-icons/hi2';
import { Link } from 'react-router-dom';
import { PostListData } from '@/types';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';
import UserName from '../PostDetail/UserName';
import useIsMobile from '@/hooks/useIsMobile';

const REVIEW_TRUNCATE_LENGTH = {
  MOBILE: 60,
  DESKTOP: 120,
};

export default function Postitem({ item, activeCategory }: { item: PostListData; activeCategory: string }) {
  const isMobile = useIsMobile();
  const slice = isMobile ? REVIEW_TRUNCATE_LENGTH.MOBILE : REVIEW_TRUNCATE_LENGTH.DESKTOP;
  const truncatedText = item.content.length > slice ? `${item.content.slice(0, slice)}...` : item.content;

  return (
    <Wrapper to={`/post/${item.postId}`} state={{ activeCategory }}>
      <LeftInfo>
        <UserInfo>
          <ProfileImg>
            <FallbackImage src={item.author.imgUrl} alt="profile" />
          </ProfileImg>
          <UserName
            userNickname={item.author.nickname}
            tierImageUrl={item.author.tierImageUrl}
            badgeImageUrl={item.author.badgeImageUrl}
          />
        </UserInfo>
        <Content>
          <Paragraph size="m" weight="bold">
            {item.title}
          </Paragraph>
          <StyledText size="s" weight="normal">
            {truncatedText}
          </StyledText>
        </Content>
        <ItemInfo>
          <Count>
            {item.selfLike ? (
              <PiHeartFill color="#fe7373" size={18} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight size={18} data-testid="PiHeartLight" />
            )}
            <StyledText size="xs" weight="normal">
              {item.totalLikeCount}
            </StyledText>
          </Count>
          <Count>
            <HiOutlineChatBubbleOvalLeft size={18} />
            <StyledText size="xs" weight="normal">
              {item.totalCommenCount}
            </StyledText>
          </Count>
          <StyledText size="xs" weight="normal">
            {item.createAt}
          </StyledText>
        </ItemInfo>
      </LeftInfo>
      {item.photoUrls && <PostImg src={item.photoUrls} />}
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

const PostImg = styled.img`
  border-radius: 16px;
  height: 130px;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  background-color: black;
`;
const StyledText = styled(Text)`
  line-height: 120%;
  white-space: pre-line;
  display: -webkit-box;
  -webkit-line-clamp: 2; // 2줄까지만 보여줌
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#D4D4D4' : '#505050')};
`;
