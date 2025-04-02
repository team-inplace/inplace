import { useSearchParams, Link } from 'react-router-dom';
import styled from 'styled-components';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';
import SearchBar from '@/components/common/SearchBar';
import BaseLayout from '@/components/common/BaseLayout';
import { useGetSearchData } from '@/api/hooks/useGetSearchData';

export default function SearchPage() {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('query') || '';

  const [{ data: influencersData }, { data: VideoData }, { data: places }] = useGetSearchData(query);

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
      <ButtonWrapper>
        <Text weight="normal" size="s" variant="grey">
          찾는 결과가 없다면
        </Text>
        <StyledLink to="/map">
          <Text weight="bold" size="s" variant="white">
            지도에서 검색하기
          </Text>
        </StyledLink>
        <Text weight="normal" size="s" variant="grey">
          에서 확인해보세요!
        </Text>
      </ButtonWrapper>
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

const StyledLink = styled(Link)`
  text-decoration: none;
  cursor: pointer;
  position: relative;
  display: inline-block;
  bottom: 1px;

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
