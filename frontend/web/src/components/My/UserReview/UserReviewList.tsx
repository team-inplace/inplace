import styled from 'styled-components';
import { UserReviewData } from '@/types';
import UserReviewItem from './UserReviewItem';
import NoItem from '@/components/common/layouts/NoItem';

export default function UserReviewList({ items = [] }: { items: UserReviewData[] }) {
  return (
    <ListContainer>
      {items.length === 0 ? (
        <NoItem message="아직 리뷰가 없어요!" height={180} />
      ) : (
        <>
          {items.map((review) => {
            return (
              <UserReviewItem
                key={review.reviewId}
                reviewId={review.reviewId}
                place={review.place}
                likes={review.likes}
                comment={review.comment}
                createdDate={review.createdDate}
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
  width: 100%;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  overflow-y: auto;
  padding-right: 10px;
  box-sizing: content-box;
  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#8e8e8e')};
    border-radius: 4px;
    border: none;
  }

  &::-webkit-scrollbar-track {
    background-color: transparent;
  }

  @media screen and (max-width: 768px) {
    gap: 20px;
  }
`;
