import styled from 'styled-components';

import { UserReviewData } from '@/types';
import { Paragraph } from '@/components/common/typography/Paragraph';
import UserReviewList from './UserReviewList';
import Loading from '@/components/common/layouts/Loading';

type Props = {
  mainText: string;
  items: UserReviewData[];
  loadMoreRef: React.RefCallback<HTMLDivElement>;
  sectionRef: React.RefObject<HTMLDivElement>;
  hasNextPage: boolean;
  isFetchingNextPage: boolean;
};

export default function MyReview({ mainText, items, loadMoreRef, sectionRef, hasNextPage, isFetchingNextPage }: Props) {
  return (
    <Container ref={sectionRef}>
      <TitleContainer>
        <Paragraph size="m" weight="bold">
          {mainText}
        </Paragraph>
      </TitleContainer>
      <UserReviewList items={items as UserReviewData[]} />
      {(hasNextPage || isFetchingNextPage) && (
        <LoadMoreTrigger ref={loadMoreRef}>
          <Loading size={30} />
        </LoadMoreTrigger>
      )}
    </Container>
  );
}
const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 30px;
  max-height: 500px;

  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 14px;
  }
`;
const TitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
`;
const LoadMoreTrigger = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;
