import { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useGetAllInfluencers } from '@/api/hooks/useGetAllInfluencers';
import { Text } from '@/components/common/typography/Text';
import BaseLayout from '@/components/common/BaseLayout';
import Pagination from '@/components/common/Pagination';
import InfluencerSearchBar from '@/components/common/InfluencerSearchBar';
import { useGetSearchInfluencers } from '@/api/hooks/useGetSearchInfluencers';
import useDebounce from '@/hooks/useDebounce';

export default function InfluencerPage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [inputValue, setInputValue] = useState('');
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
    const checkMobile = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    checkMobile();
    window.addEventListener('resize', checkMobile);
    return () => window.removeEventListener('resize', checkMobile);
  }, []);

  const debouncedInputValue = useDebounce(inputValue, 300);

  const { data: allInfluencersData } = useGetAllInfluencers({
    page: currentPage - 1,
    size: isMobile ? 9 : 10,
  });

  const { data: filteredData } = useGetSearchInfluencers({
    value: debouncedInputValue,
    page: currentPage - 1,
    size: 10,
  });

  const pageableData = debouncedInputValue ? filteredData : allInfluencersData;

  useEffect(() => {
    setCurrentPage(1);
  }, [debouncedInputValue]);

  const handlePageChange = (pageNum: number) => {
    setCurrentPage(pageNum);
  };

  return (
    <PageContainer>
      <Title>
        <Text size="l" weight="bold" variant="white">
          인플루언서
        </Text>
      </Title>
      <InfluencerSearchBar inputValue={inputValue} setInputValue={setInputValue} />
      <LayoutWrapper>
        <BaseLayout
          type="influencer"
          mainText=""
          SubText=""
          items={pageableData?.content || []}
          showMoreButton={false}
        />
      </LayoutWrapper>
      <Pagination
        currentPage={currentPage}
        totalPages={pageableData?.totalPages || 0}
        totalItems={pageableData?.totalElements}
        onPageChange={handlePageChange}
        itemsPerPage={pageableData?.pageable.pageSize}
      />
    </PageContainer>
  );
}

const PageContainer = styled.div`
  padding: 6px 0;
  display: flex;
  flex-direction: column;
  gap: 30px;

  @media screen and (max-width: 768px) {
    width: 100%;
    gap: 20px;
    align-items: center;
  }
`;

const LayoutWrapper = styled.div`
  margin-bottom: 80px;

  @media screen and (max-width: 768px) {
    width: 100%;
    margin-bottom: 60px;
    display: flex;
    justify-content: center;
  }
`;

const Title = styled.div`
  width: 90%;
`;
