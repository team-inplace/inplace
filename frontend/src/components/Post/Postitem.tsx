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
        {item.photoUrls && isMobile && <PostImg src={item.photoUrls} />}
        <ItemInfo>
          <Count>
            {item.selfLike ? (
              <PiHeartFill color="#fe7373" size={18} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight size={18} data-testid="PiHeartLight" />
            )}
            <StyledText size="s" weight="normal">
              {item.totalLikeCount ?? 0}
            </StyledText>
          </Count>
          <Count>
            <HiOutlineChatBubbleOvalLeft size={18} />
            <StyledText size="s" weight="normal">
              {item.totalCommentCount ?? 0}
            </StyledText>
          </Count>
          <StyledText size="s" weight="normal">
            {item.createAt}
          </StyledText>
        </ItemInfo>
      </LeftInfo>
      {item.photoUrls && !isMobile && <PostImg src={item.photoUrls} />}
    </Wrapper>
  );
}

const Wrapper = styled(Link)`
  width: 100%;
  display: flex;
  justify-content: space-between;
  box-sizing: border-box;
  padding: 20px 10px;
  align-items: center;
  border-radius: 16px;
  color: ${(props) => props.theme.textColor};
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#eaf5f5')};
  }

  @media screen and (max-width: 768px) {
    padding: 10px 0px;
    flex-direction: column;
    &:hover {
      background: none;
    }
  }
`;
const LeftInfo = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  gap: 10px;
  padding: 0px 10px;

  @media screen and (max-width: 768px) {
    padding: 10px 0px;
  }
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

  @media screen and (max-width: 768px) {
    margin-top: 2px;
  }
`;
const Count = styled.div`
  align-items: flex-start;
  display: flex;
  gap: 4px;
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : '#505050')};
  }
  @media screen and (max-width: 768px) {
    gap: 2px;
    align-items: center;
  }
`;

const PostImg = styled.img`
  border-radius: 16px;
  height: 130px;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  background-color: black;

  @media screen and (max-width: 768px) {
    width: 100%;
    height: auto;
    aspect-ratio: 1 / 1;
  }
`;
const StyledText = styled(Text)`
  line-height: 120%;
  white-space: pre-line;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#D4D4D4' : '#505050')};
`;
