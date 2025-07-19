import styled from 'styled-components';
import Skeleton from '@/components/ui/Skeleton';

export default function DetailSkeleton() {
  return (
    <Wrapper>
      <MainWrapper>
        <Skeleton width="960px" height="320px" borderRadius="0px" />
      </MainWrapper>
      <SectionWrapper>
        <Skeleton width="960px" height="50px" borderRadius="0px" />
      </SectionWrapper>
      <Skeleton width="200px" height="2rem" />
      <SectionWrapper>
        <Skeleton width="80px" height="100px" />
        <Skeleton width="80px" height="100px" />
        <Skeleton width="80px" height="100px" />
        <Skeleton width="80px" height="100px" />
      </SectionWrapper>
      <Skeleton width="200px" height="2rem" />
      <SectionWrapper>
        <Skeleton width="400px" height="100px" />
      </SectionWrapper>
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 40px;
`;
const MainWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;
`;

const SectionWrapper = styled.div`
  display: flex;
  gap: 20px;
`;
