import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { FaMapMarkerAlt } from 'react-icons/fa';
import { ImSpoonKnife } from 'react-icons/im';
import { useState } from 'react';
import { GrPrevious, GrNext } from 'react-icons/gr';
import { Text } from '@/components/common/typography/Text';
import { AddressInfo, MarkerInfo, PlaceData } from '@/types';
import FallbackImage from '@/components/common/Items/FallbackImage';

type Props = {
  data: MarkerInfo | PlaceData;
};

const getFullAddress = (addr: AddressInfo) => {
  return [addr.address1, addr.address2, addr.address3].filter(Boolean).join(' ');
};

const extractYoutubeId = (url: string) => {
  const match = url?.match(/(?:https?:\/\/)?(?:www\.)?youtu(?:be\.com\/watch\?v=|\.be\/)([\w-]*)(&(amp;)?[\w?=]*)?/);
  const youtubeId = match && match[1] ? match[1] : null;
  return `https://img.youtube.com/vi/${youtubeId}/mqdefault.jpg`;
};

export default function InfoWindow({ data }: Props) {
  const navigate = useNavigate();
  const [currentIndex, setCurrentIndex] = useState(0);
  const videos = data.videos || [];
  const totalVideos = videos.length;

  const handleClickInfo = (event: React.MouseEvent<HTMLDivElement>) => {
    event.stopPropagation();
    navigate(`/detail/${data.placeId}`);
  };

  const nextImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    setCurrentIndex((prevIndex) => (prevIndex + 1) % totalVideos);
  };

  const prevImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    setCurrentIndex((prevIndex) => (prevIndex - 1 + totalVideos) % totalVideos);
  };

  return (
    <Wrapper>
      <Info>
        <ImageSection>
          {totalVideos > 1 && (
            <NavButton left onClick={prevImage} disabled={currentIndex === 0}>
              <GrPrevious size={20} />
            </NavButton>
          )}
          <ImageContainer>
            <ImgWrapper currentIndex={currentIndex}>
              {videos.map((video) => {
                const isYoutubeUrl = video?.videoUrl?.includes('youtu');
                const currentImageUrl = isYoutubeUrl ? extractYoutubeId(video?.videoUrl) : '';
                return (
                  <Img key={video.influencerName}>
                    <FallbackImage src={currentImageUrl} alt={data.placeName} />
                  </Img>
                );
              })}
            </ImgWrapper>
          </ImageContainer>
          {totalVideos > 1 && (
            <NavButton right onClick={nextImage} disabled={currentIndex === videos.length - 1}>
              <GrNext size={20} />
            </NavButton>
          )}
          <InfluencerOverlay />
          <Title>
            <Text size="xxs" weight="bold" variant="#ffffff">
              {totalVideos > 0 ? videos[currentIndex]?.influencerName : ''}
            </Text>
          </Title>
        </ImageSection>
        {totalVideos > 1 && (
          <ImageIndicator>
            {videos.map((video, index) => (
              <Dot key={video.influencerName} active={index === currentIndex} />
            ))}
          </ImageIndicator>
        )}
        <TextInfo>
          <Text size="xs" weight="bold">
            {data.placeName}
          </Text>
          <Text size="xxs" weight="normal" variant="#869c9d">
            <FaMapMarkerAlt size={12} />
            {data.address ? getFullAddress(data.address) : '주소 정보가 없습니다'}
          </Text>
          <Text size="xxs" weight="normal" variant="#869c9d">
            <ImSpoonKnife size={12} />
            {data.category}
          </Text>
        </TextInfo>
      </Info>
      <DetailBtn onClick={(e: React.MouseEvent<HTMLDivElement>) => handleClickInfo(e)}>상세보기</DetailBtn>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  position: absolute;
  bottom: 50px;
  left: 30px;
  margin-left: -130px;
  width: 200px;
  overflow: hidden;
  background-color: #ffffff;
  border-radius: 14px;
  display: flex;
  flex-direction: column;
  box-shadow: 1px 1px 1px #b3b3b3;

  @media screen and (max-width: 768px) {
    width: 160px;
    margin-left: -110px;
    bottom: 40px;
  }
`;

const Info = styled.div`
  width: 100%;
  display: flex;
  box-sizing: border-box;
  align-items: center;
  flex-direction: column;
`;

const ImageSection = styled.div`
  position: relative;
  width: 100%;
`;

const ImageContainer = styled.div`
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  display: flex;
  overflow: hidden;
`;

const InfluencerOverlay = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0) 50%,
    rgba(0, 0, 0, 0.2) 70%,
    rgba(0, 0, 0, 0.5) 80%,
    rgba(0, 0, 0, 0.8) 100%
  );
  z-index: 0;
  pointer-events: none;
`;

const ImgWrapper = styled.div<{ currentIndex: number }>`
  display: flex;
  width: 100%;
  height: 100%;
  transition: transform 0.5s ease-in-out;
  transform: translateX(${(props) => -props.currentIndex * 100}%);
`;

const Img = styled.div`
  min-width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const NavButton = styled.button<{ left?: boolean; right?: boolean }>`
  ${(props) => (props.left ? 'left: 0px;' : '')}
  ${(props) => (props.right ? 'right: 0px;' : '')}
  background: none;
  border: none;
  cursor: pointer;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  color: #ffffff;
`;

const ImageIndicator = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  padding-top: 8px;
  gap: 5px;
  z-index: 2;
`;

const Dot = styled.div<{ active: boolean }>`
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: ${(props) => (props.active ? '#bce4e8' : 'rgb(221, 221, 221)')};

  @media screen and (max-width: 768px) {
    width: 4px;
    height: 4px;
  }
`;

const Title = styled.div`
  position: absolute;
  width: 90%;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  align-items: center;
  @media screen and (max-width: 768px) {
    bottom: 4px;
  }
`;
const TextInfo = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  padding-top: 14px;
  box-sizing: border-box;

  color: #4d4d4d;
  overflow: hidden;
  span {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  svg {
    margin-right: 4px;
  }

  @media screen and (max-width: 768px) {
    padding: 12px;
    padding-top: 10px;
    gap: 6px;
  }
`;

const DetailBtn = styled.div`
  width: 100%;
  height: 34px;
  background-color: #ecfdff;
  color: black;
  text-align: center;
  align-content: center;
  font-size: 12px;
  cursor: pointer;
  &:visited {
    color: black;
  }
  &:hover {
    background-color: #d0ecf0;
    transition: 0.3s;
  }
  @media screen and (max-width: 768px) {
    height: 26px;
    font-size: 10px;
  }
`;
