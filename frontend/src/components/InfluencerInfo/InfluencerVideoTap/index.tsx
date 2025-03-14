import { useState, useEffect } from 'react';
import styled from 'styled-components';
import NoItem from '@/components/common/layouts/NoItem';
import SpotItem from '@/components/Main/SpotSection/SpotItem';
import Loading from '@/components/common/layouts/Loading';
import Pagination from '@/components/common/Pagination';
import { useGetInfluencerVideo } from '@/api/hooks/useGetInfluencerVideo';

type Props = {
  influencerId: string;
  sortOption: string;
};

export default function InfluencerVideoTap({ influencerId, sortOption }: Props) {
  const [currentPage, setCurrentPage] = useState(1);
  const PAGE_SIZE = 6;

  const { data: videoData, isLoading } = useGetInfluencerVideo(influencerId, currentPage - 1, PAGE_SIZE, sortOption);

  useEffect(() => {
    setCurrentPage(1);
  }, [sortOption]);

  const handlePageChange = (pageNum: number) => {
    setCurrentPage(pageNum);
  };

  if (isLoading) {
    return (
      <Container>
        <LoadingWrapper>
          <Loading size={30} />
        </LoadingWrapper>
      </Container>
    );
  }

  return (
    <Container>
      {!videoData || videoData.content.length === 0 ? (
        <NoItem message="그곳 정보가 없어요!" alignItems="center" />
      ) : (
        <>
          <GridContainer>
            {videoData.content.map((item, index) => {
              const column = (index % 2) + 1;
              return (
                <GridItem key={item.videoId} $column={column}>
                  <SpotItem
                    key={item.videoId}
                    videoId={item.videoId}
                    videoAlias={item.videoAlias}
                    videoUrl={item.videoUrl}
                    place={item.place}
                    isInfluencer
                  />
                </GridItem>
              );
            })}
          </GridContainer>

          <Pagination
            currentPage={currentPage}
            totalPages={videoData.totalPages || 0}
            totalItems={videoData.totalElements}
            onPageChange={handlePageChange}
            itemsPerPage={PAGE_SIZE}
          />
        </>
      )}
    </Container>
  );
}

const GridContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-row-gap: 60px;
  margin-bottom: 50px;

  @media screen and (max-width: 768px) {
    grid-template-columns: repeat(1, 1fr);
    grid-row-gap: 40px;
    margin-bottom: 40px;
  }
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
`;

const LoadingWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;

const GridItem = styled.div<{ $column: number }>`
  justify-self: ${({ $column }) => ($column === 1 ? 'start' : 'end')};
  @media screen and (max-width: 768px) {
    justify-self: center;
  }
`;
