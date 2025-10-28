import styled from 'styled-components';
import { useRef } from 'react';
import MyReview from './UserReview';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';
import { useGetUserReview } from '@/api/hooks/useGetUserReview';

export default function ActiveContainer() {
  const reviewRef = useRef<HTMLDivElement>(null);
  const { data: reviews, fetchNextPage, hasNextPage, isFetchingNextPage } = useGetUserReview(10);
  const reviewLoadMoreRef = useInfiniteScroll({
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  });
  return (
    <Wrapper>
      <MyReview
        mainText="나의 리뷰"
        items={reviews.pages.flatMap((page) => page.content)}
        loadMoreRef={reviewLoadMoreRef}
        sectionRef={reviewRef}
        hasNextPage={hasNextPage}
        isFetchingNextPage={isFetchingNextPage}
      />
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
