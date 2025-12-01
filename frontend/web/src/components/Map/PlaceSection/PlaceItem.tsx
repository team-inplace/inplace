import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { useCallback, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { FaMapMarkerAlt } from 'react-icons/fa';
import { Text } from '@/components/common/typography/Text';
import { PlaceData } from '@/types';
import { usePostPlaceLike } from '@/api/hooks/usePostPlaceLike';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import FallbackImage from '@/components/common/Items/FallbackImage';
import useExtractYoutubeVideoId from '@/libs/youtube/useExtractYoutube';

interface PlaceItemProps extends PlaceData {
  onClick: () => void;
  isSelected?: boolean;
}
const getFullAddress = (addr: PlaceData['address']) => {
  return [addr.address1, addr.address2, addr.address3].filter(Boolean).join(' ');
};

export default function PlaceItem({
  placeId,
  placeName,
  address,
  videos,
  likes,
  likedCount,
  onClick,
  isSelected = false,
}: PlaceItemProps) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const { mutate: postLike } = usePostPlaceLike();
  const isYoutubeUrl = videos[0].videoUrl?.includes('youtu');
  const extractedVideoId = useExtractYoutubeVideoId(videos[0].videoUrl || '');
  const imgSrc = isYoutubeUrl ? `https://img.youtube.com/vi/${extractedVideoId}/mqdefault.jpg` : '';

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      postLike({ placeId, likes: !likes });
    },
    [likes, placeId, postLike, isAuthenticated],
  );

  return (
    <>
      <PlaceCard key={placeId} onClick={onClick} $isSelected={isSelected}>
        <CardContent>
          <ImageContainer>
            <FallbackImage src={imgSrc} alt={placeName} />
          </ImageContainer>
          <TextContainer>
            <Text size="s" weight="bold">
              {placeName}
            </Text>
            <Text size="xxs" weight="normal" variant="#a09f9f">
              <FaMapMarkerAlt size={12} />
              {getFullAddress(address)}
            </Text>
            <InfluencerName>
              <Text size="xxs" weight="normal">
                {videos[0].influencerName}
              </Text>
            </InfluencerName>
          </TextContainer>
        </CardContent>
        <LikeContainer>
          <LikeIcon
            role="button"
            aria-label="장소 좋아요"
            onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}
          >
            {likes ? (
              <PiHeartFill color="#fe7373" size={30} data-testid="PiHeartFill" />
            ) : (
              <PiHeartLight size={30} data-testid="PiHeartLight" />
            )}
          </LikeIcon>
          <LikeCount>
            <Text size="xxs" weight="normal">
              {likedCount}
            </Text>
          </LikeCount>
        </LikeContainer>
      </PlaceCard>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}

const PlaceCard = styled.div<{ $isSelected: boolean }>`
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
  height: 102px;
  border-radius: 6px;
  cursor: pointer;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : '#333333')};
  background-color: ${({ $isSelected, theme }) => {
    if ($isSelected) return theme.backgroundColor === '#292929' ? '#1b1a1a' : '#d5ecec';
    return 'none';
  }};
  transition: background-color 0.1s ease;
  box-sizing: border-box;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1b1a1a' : '#daeeee')};
  }

  @media screen and (max-width: 768px) {
    height: 100%;
    gap: 12px;
  }
`;

const ImageContainer = styled.div`
  width: 40%;
  aspect-ratio: 16 / 9;
  border-radius: 6px;
  object-fit: cover;

  @media screen and (max-width: 768px) {
    width: 30%;
    height: auto;
  }

  @media screen and (max-width: 430px) {
    width: 50%;
  }
`;

const CardContent = styled.div`
  position: relative;
  width: 90%;
  display: flex;
  height: 100%;
  gap: 16px;

  @media screen and (max-width: 768px) {
    gap: 8px;
  }
`;

const TextContainer = styled.div`
  width: 50%;
  display: flex;
  flex-direction: column;
  gap: 6px;
  justify-content: center;
  svg {
    margin-right: 2px;
  }
`;

const InfluencerName = styled.div`
  padding-top: 6px;

  @media screen and (max-width: 768px) {
    padding-top: 2px;
  }
`;

const LikeContainer = styled.div`
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  right: 10px;
  top: 10px;
  z-index: 100;
`;

const LikeIcon = styled.div`
  width: 30px;
  height: 30px;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    width: 24px;
    height: 24px;

    svg {
      width: 24px;
      height: 24px;
    }
  }
`;

const LikeCount = styled.div`
  margin-top: 4px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : '#333333')};

  @media screen and (max-width: 768px) {
    margin-top: 2px;
  }
`;
