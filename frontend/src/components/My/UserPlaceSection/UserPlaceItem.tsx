import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { Link, useLocation } from 'react-router-dom';

import styled from 'styled-components';

import { useCallback, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Text } from '@/components/common/typography/Text';
import { Paragraph } from '@/components/common/typography/Paragraph';

import { UserPlaceData } from '@/types';
import { usePostPlaceLike } from '@/api/hooks/usePostPlaceLike';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import useExtractYoutubeVideoId from '@/libs/youtube/useExtractYoutube';
import FallbackImage from '@/components/common/Items/FallbackImage';
import BasicImage from '@/assets/images/basic-image.webp';

const getFullAddress = (addr: UserPlaceData['address']) => {
  return [addr.address1, addr.address2, addr.address3].filter(Boolean).join(' ');
};

export default function UserPlaceItem({ placeId, placeName, videoUrl, influencerName, likes, address }: UserPlaceData) {
  const extractedVideoId = useExtractYoutubeVideoId(videoUrl || '');
  const thumbnailUrl = videoUrl ? `https://img.youtube.com/vi/${extractedVideoId}/hqdefault.jpg` : BasicImage;

  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const [isLike, setIsLike] = useState(likes);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const { mutate: postLike } = usePostPlaceLike();
  const queryClient = useQueryClient();

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      const newLikeStatus = !isLike;
      postLike(
        { placeId, likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['UserPlace'] }); // 내가 좋아요 한 장소
            queryClient.invalidateQueries({ queryKey: ['placeInfo', placeId] });
          },
          onError: (error) => {
            console.error('Error:', error);
          },
        },
      );
    },
    [isLike, placeId, postLike],
  );
  return (
    <>
      <Wrapper to={`/detail/${placeId}`}>
        <ImageContainer>
          <ImageWrapper>
            <FallbackImage src={thumbnailUrl} alt={String(placeId)} />
          </ImageWrapper>
          <LikeIcon onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}>
            {isLike ? (
              <PiHeartFill color="#fe7373" size={32} />
            ) : (
              <PiHeartLight
                color="white"
                size={32}
                data-testid="PiHeartLight"
                style={{ filter: 'drop-shadow(0px 0px 1px rgba(0, 0, 0, 0.3))' }}
              />
            )}
          </LikeIcon>
        </ImageContainer>
        <TextWrapper>
          <Text className="overflow" size="m" weight="bold">
            {placeName}
          </Text>
          <Text className="overflow" size="xs" weight="normal">
            {getFullAddress(address)}
          </Text>
          <Paragraph size="xs" weight="normal">
            {influencerName}
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
  display: flex;
  flex-direction: column;
  text-decoration: none;
  gap: 4px;
  width: 340px;
  border-radius: 4px;
  padding: 10px 0px;
  justify-content: space-between;
  color: inherit;

  @media screen and (max-width: 768px) {
    width: 240px;
  }
`;

const ImageContainer = styled.div`
  position: relative;
  width: 100%;
`;

const LikeIcon = styled.div`
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 100;
  cursor: pointer;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;

  @media screen and (max-width: 768px) {
    width: 30px;
    height: 30px;

    svg {
      width: 20px;
      height: 20px;
    }
  }
`;

const TextWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  line-height: 130%;
  padding: 8px 0px;
  width: 100%;
  .overflow {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  gap: 4px;
  > *:nth-child(3) {
    margin-top: 12px;
  }
  @media screen and (max-width: 768px) {
    padding: 0px;
    gap: 0px;
    > *:nth-child(3) {
      margin-top: 4px;
    }
  }
`;

const ImageWrapper = styled.div`
  width: 100%;
  aspect-ratio: 16 / 9;
  margin-bottom: 10px;
  border-radius: 6px;
  overflow: hidden;

  &:hover img {
    transform: scale(1.06);
  }

  @media screen and (max-width: 768px) {
    width: 240px;
  }
`;
