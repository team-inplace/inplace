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

  const handleClickLike = useCallback(
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
          <LikeIcon onClick={handleClickLike}>
            {isSelected ? <PiHeartFill color="#fe7373" size={32} /> : <PiHeartLight color="white" size={32} />}
          </LikeIcon>
          <FallbackImage src={influencerImgUrl} alt={influencerName} />
        </ImageContainer>
        <Paragraph size="m" weight="bold" variant="white">
          {influencerName}
        </Paragraph>
        <Paragraph size="xs" weight="normal" variant="white">
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
  height: 278px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  line-height: 30px;
`;

const ImageContainer = styled.div`
  width: 168px;
  height: 208px;
  position: relative;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: auto;
`;

const LikeIcon = styled.div`
  position: absolute;
  width: 30px;
  height: 30px;
  right: 10px;
  top: 12px;
  z-index: 100;
  cursor: pointer;
`;
