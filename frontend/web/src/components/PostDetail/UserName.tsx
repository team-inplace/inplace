import styled from 'styled-components';
import { Text } from '../common/typography/Text';

interface UserTitleProps {
  userNickname: string;
  tierImageUrl?: string;
  badgeImageUrl?: string;
}

export default function UserName({ userNickname, tierImageUrl, badgeImageUrl }: UserTitleProps) {
  return (
    <UserTitleWrapper>
      <MobileUserUI>
        {tierImageUrl && <UserTier src={tierImageUrl} alt={`${userNickname} Tier`} />}
        <Text size="s" weight="normal">
          {userNickname}
        </Text>
      </MobileUserUI>
      {badgeImageUrl && <UserTitle src={badgeImageUrl} alt={`${userNickname} Title`} />}
    </UserTitleWrapper>
  );
}

const UserTitleWrapper = styled.div`
  display: flex;
  gap: 6px;
  align-items: center;

  @media screen and (max-width: 768px) {
    flex-wrap: wrap;
    gap: 4px;
    align-items: start;
  }
`;

const UserTier = styled.img`
  height: 20px;
  width: auto;
  vertical-align: middle;
`;
const UserTitle = styled.img`
  height: 20px;
  width: auto;
  object-fit: contain;
  vertical-align: middle;

  @media screen and (max-width: 768px) {
    margin-left: 4px;
  }
`;

const MobileUserUI = styled.div``;
