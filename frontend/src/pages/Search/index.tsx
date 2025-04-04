import { useSearchParams, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';
import SearchBar from '@/components/common/SearchBarB';
import BaseLayout from '@/components/common/BaseLayout';
import { useGetSearchData } from '@/api/hooks/useGetSearchData';
import { useABTest } from '@/provider/ABTest';
import { sendGAEvent } from '@/utils/test/googleTestUtils';

export default function SearchPage() {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('query') || '';
  const testGroup = useABTest('map_ui_test');
  const navigate = useNavigate();

  const [{ data: influencersData }, { data: VideoData }, { data: places }] = useGetSearchData(query);

  // 지도 페이지로 이동 시 이벤트 추적 함수
  const handleMapNavigation = () => {
    // 검색 페이지에서 지도 페이지로의 이동 추적
    sendGAEvent('map_navigation_click_search', {
      test_name: 'map_ui_test',
      variation: testGroup,
      from_path: window.location.pathname,
      to_path: '/map',
      search_query: query, // 검색 쿼리도 함께 전송하면 분석에 유용
    });

    // 페이지 이동
    navigate('/map');
  };

  return (
    <Wrapper>
      <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" isSearchPage width="960px" />
      <Title>
        <Paragraph weight="normal" size="m">
          <Text weight="bold" size="m" variant="mint">
            {`${query} `}
          </Text>
          검색 결과
        </Paragraph>
      </Title>
      <Container>
        {influencersData && influencersData.length > 0 && (
          <>
            <SplitLine />
            <BaseLayout type="influencer" mainText="" SubText="인플루언서" items={influencersData || []} />
          </>
        )}
        {places && places.length > 0 && (
          <>
            <SplitLine />
            <BaseLayout type="place" mainText="" SubText="관련 장소" items={places || []} />
          </>
        )}
        {VideoData && VideoData.length > 0 && (
          <>
            <SplitLine />
            <BaseLayout type="spot" mainText="" SubText="바로 그곳" items={VideoData || []} />
          </>
        )}
      </Container>
      {testGroup === 'B' && (
        <ButtonWrapper>
          <Text weight="normal" size="s" variant="grey">
            찾는 결과가 없다면
          </Text>
          <MapButton onClick={handleMapNavigation}>
            <Text weight="bold" size="s" variant="white">
              지도에서 검색하기
            </Text>
          </MapButton>
          <Text weight="normal" size="s" variant="grey">
            에서 확인해보세요!
          </Text>
        </ButtonWrapper>
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 16px 0px;

  @media screen and (max-width: 768px) {
    gap: 20px;
    align-items: center;
  }
`;

const SplitLine = styled.div`
  width: 100%;
  border-bottom: 1px solid #595959;
  border-bottom: 1px solid ${({ theme }) => (theme.backgroundColor === '#292929' ? '#595959' : '#bababa')};
  @media screen and (max-width: 768px) {
    border: none;
  }
`;

const Title = styled.div`
  width: 90%;
`;

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 40px;
  align-items: center;
  margin-top: 10px;
  @media screen and (max-width: 768px) {
    gap: 20px;
  }
`;

const ButtonWrapper = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
  margin-top: 30px;

  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 6px;
  }
`;

const MapButton = styled.button`
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  position: relative;
  display: inline-block;
  bottom: 1px;
  color: inherit;
  font: inherit;

  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: -1px;
    width: 100%;
    height: 1px;
    background-color: white;
  }
`;
