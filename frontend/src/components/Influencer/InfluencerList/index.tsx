import styled from 'styled-components';
import InfluencerItem from '@/components/common/Items/InfluencerItem';
import { InfluencerData } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';
import useIsMobile from '@/hooks/useIsMobile';

interface InfluencerListProps {
  items: InfluencerData[];
}

export default function InfluencerList({ items }: InfluencerListProps) {
  const MOBILE_INFLUENCER_LIMIT = 9;
  const isMobile = useIsMobile();

  const displayItems = isMobile ? items.slice(0, MOBILE_INFLUENCER_LIMIT) : items;

  return displayItems.length === 0 ? (
    <NoItem message="인플루언서 정보가 없어요!" alignItems="center" />
  ) : (
    <GridContainer>
      {displayItems.map((influencer) => {
        return (
          <InfluencerItem
            key={influencer.influencerId}
            influencerId={influencer.influencerId}
            influencerName={influencer.influencerName}
            influencerImgUrl={influencer.influencerImgUrl}
            influencerJob={influencer.influencerJob}
            likes={influencer.likes}
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
  width: 100%;

  @media screen and (max-width: 768px) {
    margin: 0 auto;
    grid-template-columns: repeat(auto-fit, minmax(110px, 1fr));
    gap: 20px;
    justify-content: center;
  }

  @media screen and (max-width: 425px) {
    grid-template-columns: repeat(3, 1fr);
    gap: 0.8rem;
  }
`;
