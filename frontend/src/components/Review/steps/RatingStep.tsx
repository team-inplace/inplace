import styled from 'styled-components';
import { AiFillLike, AiFillDislike } from 'react-icons/ai';
import { ReviewInfo } from '@/types';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';
import Logo from '@/assets/images/InplaceLogo.png';

interface RatingStepProps {
  onSubmit: (isLiked: boolean) => void;
  placeInfo: ReviewInfo;
}

interface RatingButtonProps {
  buttonType: 'like' | 'dislike';
}

export default function RatingStep({ onSubmit, placeInfo }: RatingStepProps) {
  return (
    <StepContainer>
      <TitleSection>
        <Paragraph size="l" weight="normal">
          방문하신&nbsp;
        </Paragraph>
        <Paragraph size="xl" weight="bold" variant="mint">
          {placeInfo.placeName}
        </Paragraph>
        <Paragraph size="l" weight="normal">
          , 어떠셨나요?
        </Paragraph>
      </TitleSection>

      <PlaceSection>
        <ImageFrame>
          <LogoImage src={Logo} alt="인플레이스 로고" />
          <PlaceInfo>
            <TextWrapper className="name">
              <Text size="l" weight="bold" style={{ color: '#c6c6c6' }}>
                {placeInfo.placeName}
              </Text>
            </TextWrapper>
            <TextWrapper className="address">
              <Text size="s" weight="normal" style={{ color: '#c6c6c6' }}>
                {placeInfo.placeAddress}
              </Text>
            </TextWrapper>
            <TextWrapper className="influencer">
              <Text size="s" weight="normal">
                {placeInfo.influencerName}
              </Text>
            </TextWrapper>
          </PlaceInfo>
        </ImageFrame>
      </PlaceSection>

      <RatingSection>
        <RatingButton aria-label="like_btn" onClick={() => onSubmit(true)} buttonType="like">
          <AiFillLike size={50} />
          <ButtonText>좋았어요</ButtonText>
        </RatingButton>

        <RatingButton aria-label="dislike_btn" onClick={() => onSubmit(false)} buttonType="dislike">
          <AiFillDislike size={50} />
          <ButtonText>아쉬워요</ButtonText>
        </RatingButton>
      </RatingSection>
    </StepContainer>
  );
}

const StepContainer = styled.div`
  animation: fadeIn 0.3s ease-in-out;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 7rem;
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
`;

const TitleSection = styled.div`
  display: flex;
  align-items: flex-end;
  justify-content: center;
  margin-top: 6%;
`;

const PlaceSection = styled.div`
  display: flex;
  width: 100%;
  justify-content: center;
`;
const ImageFrame = styled.div`
  position: relative;
  display: flex;
  gap: 10px;

  &::before {
    content: '';
    position: absolute;
    top: -100%;
    left: -20%;
    width: 3.75rem;
    height: 3.75rem;
    border-left: 1.5px solid white;
    border-top: 1.5px solid white;
  }

  &::after {
    content: '';
    position: absolute;
    bottom: -100%;
    right: -20%;
    width: 3.75rem;
    height: 3.75rem;
    border-right: 1.5px solid white;
    border-bottom: 1.5px solid white;
  }
`;

const PlaceInfo = styled.div``;

const TextWrapper = styled.div`
  &.name {
    margin-bottom: 0.2rem;
  }

  &.address {
    margin-bottom: 0.5rem;
  }
`;

const RatingSection = styled.div`
  display: flex;
  justify-content: center;
  gap: 6rem;
`;

const RatingButton = styled.button<RatingButtonProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.55rem;
  color: ${(props) => (props.buttonType === 'like' ? '#D85B53' : '#595BD4')};
  background-color: transparent;
  transition: all 0.2s ease;
  padding: 0px 1rem;
  box-shadow: none;
  border: none;
  border-radius: 0.5rem;
`;

const ButtonText = styled.span`
  font-size: 0.95rem;
  color: white;
`;

const LogoImage = styled.img`
  height: 80px;
  width: 70px;

  @media screen and (max-width: 768px) {
    height: 60px;
    width: 50px;
  }
`;
