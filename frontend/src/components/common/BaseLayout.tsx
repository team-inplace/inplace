import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';

import InfluencerSection from '@/components/Main/InfluencerSection';
import InfluencerList from '@/components/Influencer/InfluencerList';
import SpotSection from '@/components/Main/SpotSection';
import { InfluencerData, SpotData, UserPlaceData } from '@/types';
import ChoiceList from '@/components/Choice/ChoiceList';
import { Paragraph } from './typography/Paragraph';
import useTheme from '@/hooks/useTheme';
import UserPlaceSection from '../My/UserPlaceSection';

type Props = {
  type: string;
  prevSubText?: string;
  mainText: string;
  SubText: string;
  items: InfluencerData[] | SpotData[] | UserPlaceData[];
  showMoreButton?: boolean;
  isChoice?: boolean;
} & (
  | {
      isChoice: true;
      onToggleLike: (influencerId: number, isLiked: boolean) => void;
      selectedInfluencers: Set<number>;
    }
  | {
      isChoice?: false;
      onToggleLike?: never;
      selectedInfluencers?: never;
    }
);

export default function BaseLayout({
  type,
  prevSubText = '',
  mainText = '',
  SubText,
  items,
  showMoreButton = true,
  isChoice = false,
  onToggleLike,
  selectedInfluencers,
}: Props) {
  const navigate = useNavigate();
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';

  const renderSection = () => {
    if (type === 'influencer' && showMoreButton === false) {
      if (isChoice && onToggleLike && selectedInfluencers) {
        return (
          <ChoiceList
            items={items as InfluencerData[]}
            onToggleLike={onToggleLike}
            selectedInfluencers={selectedInfluencers}
          />
        );
      }
      return <InfluencerList items={items as InfluencerData[]} useBackCard={false} />;
    }
    if (type === 'influencer') {
      return <InfluencerSection items={items as InfluencerData[]} />;
    }
    if (type === 'spot') {
      return <SpotSection items={items as SpotData[]} />;
    }
    return <UserPlaceSection items={items as UserPlaceData[]} />;
  };

  return (
    <Container>
      <TitleContainer>
        <Paragraph size="m" weight="bold">
          {prevSubText || ''}
          <Text size="ll" weight="bold" style={isDarkMode ? { color: '#55EBFF' } : { color: '#47c8d9' }}>
            {mainText || ''}
          </Text>
          {SubText}
        </Paragraph>
        {type === 'influencer' && showMoreButton ? (
          <MoreBtn aria-label="more_btn" onClick={() => navigate('/influencer')}>
            더보기
          </MoreBtn>
        ) : null}
      </TitleContainer>
      {renderSection()}
    </Container>
  );
}

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 30px;

  @media screen and (max-width: 768px) {
    width: 90%;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
`;

const TitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
  width: 100%;
`;

const MoreBtn = styled.button`
  font-size: 14px;
  color: #b0b0b0;
  background: none;
  border: none;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    font-size: 12px;
    text-align: end;
    padding-block: 0;
    padding-inline: 0;
  }
`;
