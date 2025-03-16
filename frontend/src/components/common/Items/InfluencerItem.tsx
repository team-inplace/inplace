import { Link, useLocation } from 'react-router-dom';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';

import styled from 'styled-components';

import { MdLocationOn } from 'react-icons/md';
import { useCallback, useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Paragraph } from '@/components/common/typography/Paragraph';
import backCard from '@/assets/images/back-card.webp';
import { InfluencerData } from '@/types';
import { usePostInfluencerLike } from '@/api/hooks/usePostInfluencerLike';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import FallbackImage from './FallbackImage';

interface InfluencerItemProps extends InfluencerData {
  useBackCard?: boolean;
  useNav?: boolean;
}

export default function InfluencerItem({
  influencerId,
  influencerName,
  influencerImgUrl,
  influencerJob,
  likes,
  useBackCard = false,
  useNav = false,
}: InfluencerItemProps) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const [isLike, setIsLike] = useState(likes);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const { mutate: postLike } = usePostInfluencerLike();
  const queryClient = useQueryClient();

  const handleClickLike = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      const newLikeStatus = !isLike;
      postLike(
        { influencerId, likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['myInfluencerVideo'] }); // 내 인플루언서가 좋아한 그곳
            queryClient.invalidateQueries({ queryKey: ['UserInfluencer'] }); // 내가 좋아요 한 인플루언서
          },
          onError: () => {
            alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
          },
        },
      );
    },
    [isLike, influencerId, postLike],
  );

  useEffect(() => {
    setIsLike(likes);
  }, [likes]);

  return (
    <>
      <Wrapper to={`/influencer/${influencerId}`}>
        <ImageContainer>
          <LikeIcon
            role="button"
            aria-label="like_btn"
            onClick={(e: React.MouseEvent<HTMLDivElement>) => handleClickLike(e)}
          >
            {isLike ? (
              <PiHeartFill color="#fe7373" size={32} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight color="white" size={32} data-testid="PiHeartLight" />
            )}
          </LikeIcon>
          <FallbackImage src={influencerImgUrl} alt={influencerName} />
          {useBackCard && useNav && (
            <BackImageWrapper>
              <MdLocationOn size={50} color="#55EBFF" />
              <Paragraph size="m" weight="bold" variant="white">
                지도 보기
              </Paragraph>
            </BackImageWrapper>
          )}
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

  &:hover {
    & > div:nth-child(2) {
      opacity: 0;
    }

    & > div:last-child {
      opacity: 1;
    }
  }

  @media screen and (max-width: 768px) {
    width: 100%;
  }
`;

const BackImageWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-image: url(${backCard});
  background-size: cover;
  background-position: center;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
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

const TextWrapper = styled.div`
  > *:not(:first-child) {
    margin-top: 6px;
    @media screen and (max-width: 768px) {
      margin-top: 4px;
    }
  }
`;
