import { AiFillLike, AiFillDislike } from 'react-icons/ai';

import styled from 'styled-components';

import { useState } from 'react';
import { Paragraph } from '@/components/common/typography/Paragraph';

import BarGraph from './BarGraph';
import Review from './Review';
import { PlaceLikes } from '@/types';
import { useGetReview } from '@/api/hooks/useGetReview';
import Pagination from '@/components/common/Pagination';
import { Text } from '@/components/common/typography/Text';

export default function ReviewTap({ placeLikes, id: placeId }: { placeLikes: PlaceLikes; id: string }) {
  const [currentPage, setCurrentPage] = useState(1);

  const { data: reviews } = useGetReview({ page: currentPage - 1, size: 10, id: placeId });

  const handlePageChange = (pageNum: number) => {
    setCurrentPage(pageNum);
  };

  return (
    <Wrapper>
      <CountLike>
        <AiFillLike size={50} color="#fe7373" />
        <BarGraph like={placeLikes.like} dislike={placeLikes.dislike} />
        <AiFillDislike size={50} color="#6F6CFF" />
      </CountLike>
      <Paragraph size="m" weight="bold">
        <Text size="m" variant="mint" weight="bold">
          리뷰{' '}
        </Text>
        한마디
      </Paragraph>
      <Review items={reviews.content} />
      <Pagination
        currentPage={currentPage}
        totalPages={reviews.totalPages}
        totalItems={reviews.totalElements}
        onPageChange={handlePageChange}
        itemsPerPage={reviews.pageable.pageSize}
      />
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 40px;
  padding: 20px 30px 80px;

  @media screen and (max-width: 768px) {
    padding: 10px 10px;
    gap: 20px;
  }
`;
const CountLike = styled.div`
  display: flex;
  gap: 20px;
  margin-bottom: 20px;

  @media screen and (max-width: 768px) {
    gap: 10px;
    svg {
      width: 40px;
      height: 38px;
    }
  }
`;
