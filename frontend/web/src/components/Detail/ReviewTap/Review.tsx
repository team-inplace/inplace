import styled from 'styled-components';
import ReviewItem from './ReviewItem';
import { ReviewData } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';

export default function Review({ items }: { items: ReviewData[] }) {
  return (
    <ListContainer>
      {items.length === 0 ? (
        <NoItem message="아직 리뷰가 없어요!" height={300} />
      ) : (
        <>
          {items.map((review) => {
            return (
              <ReviewItem
                key={review.reviewId}
                reviewId={review.reviewId}
                likes={review.likes}
                comment={review.comment}
                userNickname={review.userNickname}
                createdDate={review.createdDate}
                mine={review.mine}
              />
            );
          })}
        </>
      )}
    </ListContainer>
  );
}

const ListContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 38px;

  @media screen and (max-width: 768px) {
    gap: 24px;
  }
`;
