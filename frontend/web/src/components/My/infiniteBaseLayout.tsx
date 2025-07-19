import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';
import { InfluencerData, UserPlaceData } from '@/types';
import InfiniteScrollSection from './InfiniteScrollSection';
import InfluencerItem from '@/components/common/Items/InfluencerItem';
import UserPlaceItem from './UserPlaceSection/UserPlaceItem';

type Props = {
  type: 'influencer' | 'place';
  prevSubText?: string;
  mainText: string;
  SubText: string;
  items: InfluencerData[] | UserPlaceData[];
  loadMoreRef: React.RefCallback<HTMLDivElement>;
  sectionRef: React.RefObject<HTMLDivElement>;
  fetchNextPage: () => void;
  hasNextPage: boolean;
  isFetchingNextPage: boolean;
};

export default function InfiniteBaseLayout({
  type,
  prevSubText = '',
  mainText = '',
  SubText,
  items,
  loadMoreRef,
  sectionRef,
  fetchNextPage,
  hasNextPage,
  isFetchingNextPage,
}: Props) {
  const renderItem = (item: InfluencerData | UserPlaceData) => {
    if (type === 'influencer') {
      const influencer = item as InfluencerData;
      return (
        <InfluencerItem
          key={influencer.influencerId}
          influencerId={influencer.influencerId}
          influencerName={influencer.influencerName}
          influencerImgUrl={influencer.influencerImgUrl}
          influencerJob={influencer.influencerJob}
          likes={influencer.likes}
          data-influencer-item
        />
      );
    }
    const place = item as UserPlaceData;
    return (
      <UserPlaceItem
        key={place.placeId}
        placeId={place.placeId}
        placeName={place.placeName}
        imageUrl={place.imageUrl}
        videoUrl={place.videoUrl}
        influencerName={place.influencerName}
        likes={place.likes}
        address={place.address}
        data-place-item
      />
    );
  };

  return (
    <Container ref={sectionRef}>
      <TitleContainer>
        <Text size="m" weight="bold">
          {prevSubText || ''}
          <Text size="28px" weight="bold" variant="mint">
            {mainText || ''}
          </Text>
          {SubText}
        </Text>
      </TitleContainer>
      <InfiniteScrollSection<InfluencerData | UserPlaceData>
        items={items}
        fetchNextPage={fetchNextPage}
        hasNextPage={hasNextPage}
        loadMoreRef={loadMoreRef}
        isFetchingNextPage={isFetchingNextPage}
        renderItem={renderItem}
        noItemMessage={type === 'influencer' ? '인플루언서 정보가 없어요!' : '장소 정보가 없어요!'}
        dataAttr={type === 'influencer' ? 'data-influencer-item' : 'data-place-item'}
        type={type}
      />
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
    align-items: start;
  }
`;

const TitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
`;
