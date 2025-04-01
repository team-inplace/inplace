import { useEffect, useMemo } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import NoItem from '@/components/common/layouts/NoItem';
import SpotItem from '@/components/Main/SpotSection/SpotItem';
import Loading from '@/components/common/layouts/Loading';
import Pagination from '@/components/common/Pagination';
import { useGetInfluencerVideo } from '@/api/hooks/useGetInfluencerVideo';

type Props = {
  influencerId: string;
  sortOption: string;
  onSortChange?: (sort: string) => void;
};

export default function InfluencerVideoTap({ influencerId, sortOption, onSortChange }: Props) {
  const location = useLocation();
  const navigate = useNavigate();
  const PAGE_SIZE = 6;

  const searchParams = useMemo(() => new URLSearchParams(location.search), [location.search]);

  const currentPage = useMemo(() => {
    const pageParam = searchParams.get('page');
    return pageParam ? parseInt(pageParam, 10) : 1;
  }, [searchParams]);

  const currentSort = useMemo(() => {
    return searchParams.get('sort') || sortOption;
  }, [searchParams, sortOption]);

  useEffect(() => {
    if (currentSort !== sortOption && onSortChange) {
      onSortChange(currentSort);
    }
  }, [currentSort, sortOption, onSortChange]);

  // 부모 컴포넌트에서 전달받은 sortOption 변경되면 url 업데이트
  useEffect(() => {
    const urlSort = searchParams.get('sort');

    if (urlSort !== sortOption) {
      const newParams = new URLSearchParams(searchParams);
      newParams.set('sort', sortOption);

      if (!newParams.has('page')) {
        newParams.set('page', '1');
      }
      navigate(`${location.pathname}?${newParams.toString()}`, { replace: true });
    }
  }, [sortOption, searchParams, navigate, location.pathname]);

  const { data: videoData, isLoading } = useGetInfluencerVideo(influencerId, currentPage - 1, PAGE_SIZE, currentSort);

  const handlePageChange = (pageNum: number) => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('page', pageNum.toString());
    navigate(`${location.pathname}?${newParams.toString()}`);
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
            {videoData.content.map((item, index) => (
              <GridItem key={item.videoId} $column={(index % 2) + 1}>
                <SpotItem
                  videoId={item.videoId}
                  videoAlias={item.videoAlias}
                  videoUrl={item.videoUrl}
                  place={item.place}
                  isInfluencer
                />
              </GridItem>
            ))}
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
