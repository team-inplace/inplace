import { FaMapMarkerAlt } from 'react-icons/fa';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { Paragraph } from '@/components/common/typography/Paragraph';

import useExtractYoutubeVideoId from '@/libs/youtube/useExtractYoutube';
import { AddressInfo, SpotData } from '@/types';
import FallbackImage from '@/components/common/Items/FallbackImage';
import BasicImage from '@/assets/images/basic-image.webp';

interface SpotItemProps extends SpotData {
  isInfluencer?: boolean;
}

const getFullAddress = (addr: AddressInfo) => {
  return [addr.address1, addr.address2, addr.address3].filter(Boolean).join(' ');
};

export default function SpotItem({ videoId, influencerName, videoUrl, place, isInfluencer = false }: SpotItemProps) {
  const extractedVideoId = useExtractYoutubeVideoId(videoUrl || '');
  const thumbnailUrl = videoUrl ? `https://img.youtube.com/vi/${extractedVideoId}/hqdefault.jpg` : BasicImage;

  return (
    <Wrapper to={`/detail/${place.placeId}`} $isInfluencer={isInfluencer}>
      <ImageWrapper $isInfluencer={isInfluencer}>
        <FallbackImage src={thumbnailUrl} alt={String(videoId)} />
      </ImageWrapper>
      <Paragraph size="m" weight="bold">
        {influencerName} | {place.placeName}
      </Paragraph>
      <Paragraph size="xs" weight="normal">
        <FaMapMarkerAlt size={20} color="#47c8d9" />
        {getFullAddress(place.address)}
      </Paragraph>
    </Wrapper>
  );
}
const Wrapper = styled(Link)<{ $isInfluencer: boolean }>`
  width: ${({ $isInfluencer }) => ($isInfluencer ? `440px` : '340px')};
  display: flex;
  flex-direction: column;
  align-content: end;
  line-height: 30px;
  gap: 4px;
  color: inherit;

  svg {
    margin-right: 2px;
  }

  @media screen and (max-width: 768px) {
    line-height: 24px;
    gap: 4px;
    svg {
      height: 14px;
      margin: 0px;
    }
    width: ${({ $isInfluencer }) => ($isInfluencer ? '100%' : '300px')};
  }
`;

const ImageWrapper = styled.div<{ $isInfluencer?: boolean }>`
  width: ${({ $isInfluencer }) => ($isInfluencer ? `440px` : '340px')};
  aspect-ratio: 16 / 9;
  margin-bottom: 10px;
  border-radius: 6px;
  overflow: hidden;

  &:hover img {
    transform: scale(1.06);
  }

  @media screen and (max-width: 768px) {
    width: ${({ $isInfluencer }) => ($isInfluencer ? '100%' : '300px')};
  }
`;
