import styled from 'styled-components';
import InfluencerItem from '@/components/common/Items/InfluencerItem';
import { InfluencerData } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';

interface InfluencerListProps {
  items: InfluencerData[];
  useBackCard?: boolean;
  useNav?: boolean;
}

export default function InfluencerList({ items, useBackCard = true, useNav = true }: InfluencerListProps) {
  return items.length === 0 ? (
    <NoItem message="인플루언서 정보가 없어요!" alignItems="center" />
  ) : (
    <GridContainer>
      {items.map((influencer) => {
        return (
          <InfluencerItem
            key={influencer.influencerId}
            influencerId={influencer.influencerId}
            influencerName={influencer.influencerName}
            influencerImgUrl={influencer.influencerImgUrl}
            influencerJob={influencer.influencerJob}
            likes={influencer.likes}
            useBackCard={useBackCard}
            useNav={useNav}
          />
        );
      })}
    </GridContainer>
  );
}
const GridContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 26px;
`;
