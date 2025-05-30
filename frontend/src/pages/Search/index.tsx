import { useSearchParams } from 'react-router-dom';
import styled from 'styled-components';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';
import SearchBar from '@/components/common/SearchBarB';
import BaseLayout from '@/components/common/BaseLayout';
import { useGetSearchData } from '@/api/hooks/useGetSearchData';
import { useABTest } from '@/provider/ABTest';
import MapSection from '@/components/Main/MapSection';

export default function SearchPage() {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('query') || '';
  const testGroup = useABTest('map_ui_test');

  const [{ data: influencersData }, { data: VideoData }, { data: places }] = useGetSearchData(query);

  return (
    <Wrapper>
      <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" isSearchPage width="960px" onClose={() => {}} />
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
        <MapSection highlightText="찾으시는 결과" mainText="가 없다면 지도페이지를 확인해보세요!" />
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
