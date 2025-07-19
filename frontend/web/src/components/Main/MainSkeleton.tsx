import styled from 'styled-components';
import Skeleton from '@/components/ui/Skeleton';

export default function MainSkeleton() {
  return (
    <Wrapper>
      <MainWrapper>
        <Skeleton width="960px" height="44px" borderRadius="16px" />
        <Skeleton width="960px" height="400px" borderRadius="0px" />
      </MainWrapper>
      <SectionWrapper>
        <InfluencerWrapper>
          <Skeleton width="170px" height="208px" borderRadius="6px" />
          <Skeleton width="130px" height="1rem" />
          <Skeleton width="100px" height="1rem" />
        </InfluencerWrapper>
        <InfluencerWrapper>
          <Skeleton width="170px" height="208px" borderRadius="6px" />
          <Skeleton width="100px" height="1rem" />
          <Skeleton width="100px" height="1rem" />
        </InfluencerWrapper>
        <InfluencerWrapper>
          <Skeleton width="170px" height="208px" borderRadius="6px" />
          <Skeleton width="100px" height="1rem" />
          <Skeleton width="100px" height="1rem" />
        </InfluencerWrapper>
        <InfluencerWrapper>
          <Skeleton width="170px" height="208px" borderRadius="6px" />
          <Skeleton width="100px" height="1rem" />
          <Skeleton width="100px" height="1rem" />
        </InfluencerWrapper>
        <InfluencerWrapper>
          <Skeleton width="120px" height="208px" borderRadius="6px" />
          <Skeleton width="100px" height="1rem" />
          <Skeleton width="100px" height="1rem" />
        </InfluencerWrapper>
      </SectionWrapper>
      <SectionWrapper>
        <VideoWrapper>
          <Skeleton width="340px" height="180px" borderRadius="6px" />
          <Skeleton width="340px" height="1.5rem" />
          <Skeleton width="200px" height="1rem" />
        </VideoWrapper>
        <VideoWrapper>
          <Skeleton width="340px" height="180px" borderRadius="6px" />
          <Skeleton width="340px" height="1.5rem" />
          <Skeleton width="200px" height="1rem" />
        </VideoWrapper>
        <VideoWrapper>
          <Skeleton width="200px" height="180px" borderRadius="6px" />
          <Skeleton width="200px" height="1.5rem" />
          <Skeleton width="200px" height="1rem" />
        </VideoWrapper>
      </SectionWrapper>
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 110px;
`;
const MainWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;
`;
const InfluencerWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
`;
const VideoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;
const SectionWrapper = styled.div`
  display: flex;
  gap: 40px;
`;
