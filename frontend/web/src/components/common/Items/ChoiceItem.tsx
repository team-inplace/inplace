import { useLocation } from 'react-router-dom';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import styled from 'styled-components';
import { useCallback, useState } from 'react';
import { InfluencerData } from '@/types';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import { Paragraph } from '@/components/common/typography/Paragraph';
import FallbackImage from './FallbackImage';

interface ChoiceItemProps
  extends Pick<InfluencerData, 'influencerId' | 'influencerName' | 'influencerImgUrl' | 'influencerJob'> {
  onToggleLike: (influencerId: number, isLiked: boolean) => void;
  isSelected: boolean;
}

export default function ChoiceItem({
  influencerId,
  influencerName,
  influencerImgUrl,
  influencerJob,
  onToggleLike,
  isSelected = false,
}: ChoiceItemProps) {
  const { isAuthenticated } = useAuth();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const location = useLocation();

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();

      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }

      onToggleLike(influencerId, !isSelected);
    },
    [influencerId, isSelected, onToggleLike, isAuthenticated],
  );

  return (
    <>
      <Wrapper>
        <ImageContainer>
          <LikeIcon onClick={handleLikeClick}>
            {isSelected ? (
              <PiHeartFill color="#fe7373" size={32} />
            ) : (
              <PiHeartLight size={32} color="white" style={{ filter: 'drop-shadow(0px 0px 1px rgba(0, 0, 0, 0.3))' }} />
            )}
          </LikeIcon>
          <FallbackImage src={influencerImgUrl} alt={influencerName} />
        </ImageContainer>
        <Paragraph size="m" weight="bold">
          {influencerName}
        </Paragraph>
        <Paragraph size="xs" weight="normal">
          {influencerJob}
        </Paragraph>
      </Wrapper>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}

const Wrapper = styled.div`
  width: 170px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  text-decoration: none;
  gap: 10px;

  @media screen and (max-width: 768px) {
    height: 100%;
    aspect-ratio: 1.2 / 2;
    width: auto;
  }
`;

const ImageContainer = styled.div`
  width: 168px;
  height: 208px;
  position: relative;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 4px;

  @media screen and (max-width: 768px) {
    width: 100%;
  }
`;

const LikeIcon = styled.div`
  position: absolute;
  width: 30px;
  height: 30px;
  right: 10px;
  top: 12px;
  z-index: 100;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    top: 8px;
    right: 6px;
    svg {
      width: 24px;
      height: 24px;
    }
  }
`;
