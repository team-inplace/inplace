import { Link, useLocation } from 'react-router-dom';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';

import styled from 'styled-components';

import { useCallback, useState } from 'react';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { InfluencerData } from '@/types';
import { usePostInfluencerLike } from '@/api/hooks/usePostInfluencerLike';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import FallbackImage from './FallbackImage';

export default function InfluencerItem({
  influencerId,
  influencerName,
  influencerImgUrl,
  influencerJob,
  likes,
}: InfluencerData) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const [showLoginModal, setShowLoginModal] = useState(false);

  const { mutate: postLike } = usePostInfluencerLike();

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      postLike({ influencerId, likes: !likes });
    },

    [likes, influencerId, isAuthenticated, postLike],
  );

  return (
    <>
      <Wrapper to={`/influencer/${influencerId}`}>
        <ImageContainer>
          <LikeIcon
            role="button"
            aria-label="인플루언서 좋아요"
            onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}
          >
            {likes ? (
              <PiHeartFill color="#fe7373" size={32} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight
                color="white"
                size={32}
                data-testid="PiHeartLight"
                style={{ filter: 'drop-shadow(0px 0px 1px rgba(0, 0, 0, 0.3))' }}
              />
            )}
          </LikeIcon>
          <FallbackImage src={influencerImgUrl} alt={influencerName} />
        </ImageContainer>
        <TextWrapper>
          <Paragraph size="m" weight="bold">
            {influencerName}
          </Paragraph>
          <Paragraph size="xs" weight="normal">
            {influencerJob}
          </Paragraph>
        </TextWrapper>
      </Wrapper>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}

const Wrapper = styled(Link)`
  width: 170px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  text-decoration: none;
  gap: 10px;
  color: inherit;

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

  &:hover img {
    transform: scale(1.06);
  }

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
  z-index: 20;
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

const TextWrapper = styled.div`
  > *:not(:first-child) {
    margin-top: 6px;
    @media screen and (max-width: 768px) {
      margin-top: 4px;
    }
  }
`;
