import styled from 'styled-components';
import { useRef } from 'react';
import InfiniteBaseLayout from './infiniteBaseLayout';
import { useGetUserPlace } from '@/api/hooks/useGetUserPlace';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';
import { useGetUserInfluencer } from '@/api/hooks/useGetUserInfluencer';

export default function LikesContainer() {
  const influencerRef = useRef<HTMLDivElement>(null);
  const {
    data: influencers,
    fetchNextPage: influencerFetchNextPage,
    hasNextPage: influencerHasNextPage,
    isFetchingNextPage: influencerIsFetchingNextPage,
  } = useGetUserInfluencer(10);
  const influencerLoadMoreRef = useInfiniteScroll({
    fetchNextPage: influencerFetchNextPage,
    hasNextPage: influencerHasNextPage,
    isFetchingNextPage: influencerIsFetchingNextPage,
  });

  const placeRef = useRef<HTMLDivElement>(null);
  const {
    data: places,
    fetchNextPage: placeFetchNextPage,
    hasNextPage: placeHasNextPage,
    isFetchingNextPage: placeIsFetchingNextPage,
  } = useGetUserPlace(10);
  const placeLoadMoreRef = useInfiniteScroll({
    fetchNextPage: placeFetchNextPage,
    hasNextPage: placeHasNextPage,
    isFetchingNextPage: placeIsFetchingNextPage,
  });

  return (
    <Wrapper>
      <InfiniteBaseLayout
        type="influencer"
        mainText=""
        SubText="나의 인플루언서"
        items={influencers.pages.flatMap((page) => page.content)}
        loadMoreRef={influencerLoadMoreRef}
        sectionRef={influencerRef}
        hasNextPage={influencerHasNextPage}
        fetchNextPage={influencerFetchNextPage}
        isFetchingNextPage={influencerIsFetchingNextPage}
      />
      <InfiniteBaseLayout
        type="place"
        mainText=""
        SubText="나의 관심 장소"
        items={places.pages.flatMap((page) => page.content)}
        loadMoreRef={placeLoadMoreRef}
        sectionRef={placeRef}
        hasNextPage={placeHasNextPage}
        fetchNextPage={placeFetchNextPage}
        isFetchingNextPage={placeIsFetchingNextPage}
      />
      {/* 게시글 좋아요 */}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 60px;

  @media screen and (max-width: 768px) {
    align-items: center;
    width: 100%;
  }
`;
