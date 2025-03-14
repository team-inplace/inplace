import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { IoIosArrowDown } from 'react-icons/io';

import styled from 'styled-components';
import { useLocation, useParams } from 'react-router-dom';
import { Suspense, useCallback, useEffect, useState } from 'react';
import { QueryErrorResetBoundary, useQueryClient } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import Error from '@/components/common/layouts/Error';
import { Text } from '@/components/common/typography/Text';
import FallbackImage from '@/components/common/Items/FallbackImage';
import useAuth from '@/hooks/useAuth';
import { usePostInfluencerLike } from '@/api/hooks/usePostInfluencerLike';
import LoginModal from '@/components/common/modals/LoginModal';
import { useGetInfluencerInfo } from '@/api/hooks/useGetInfluencerInfo';
import Loading from '@/components/common/layouts/Loading';
import InfluencerVideoTap from '@/components/InfluencerInfo/InfluencerVideoTap';
import InfluencerMapTap from '@/components/InfluencerInfo/InfluencerMapTap';
import Button from '@/components/common/Button';

export default function InfluencerInfoPage() {
  const { id } = useParams() as { id: string };
  const { data: influencerInfoData } = useGetInfluencerInfo(id);
  const [sortOption, setSortOption] = useState('publishTime');

  const sortLabel: Record<string, string> = {
    publishTime: '최신순',
    popularity: '인기순',
    likes: '좋아요순',
  };

  const influencerId = Number(id);

  const { isAuthenticated } = useAuth();
  const location = useLocation();

  const [activeTab, setActiveTab] = useState<'video' | 'map'>('video');
  const [isLike, setIsLike] = useState(influencerInfoData.likes);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showSortOptions, setShowSortOptions] = useState(false);
  const queryClient = useQueryClient();

  const { mutate: postLike } = usePostInfluencerLike();

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
            queryClient.invalidateQueries({ queryKey: ['influencerInfo'] });
          },
          onError: () => {
            alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
          },
        },
      );
    },
    [isLike, influencerId, postLike],
  );

  const handleSortChange = (option: string) => {
    setSortOption(option);
    setShowSortOptions(false);
  };

  useEffect(() => {
    setIsLike(influencerInfoData.likes);
  }, [influencerInfoData.likes]);

  return (
    <PageContainer>
      <InfluencerInfoSection>
        <Image>
          <FallbackImage src={influencerInfoData.influencerImgUrl} alt={influencerInfoData.influencerName} />
        </Image>
        <TextInfo>
          <Text size="xl" weight="bold">
            {influencerInfoData.influencerName}
          </Text>
          <Text size="s" weight="normal">
            좋아요 수 {influencerInfoData.follower}명 • 쿨 플레이스 {influencerInfoData.placeCount}곳
          </Text>
          <Text size="xs" weight="normal">
            {influencerInfoData.influencerJob}
          </Text>
        </TextInfo>
        <LikeIcon
          aria-label="like_btn"
          role="button"
          onClick={(e: React.MouseEvent<HTMLDivElement>) => handleClickLike(e)}
        >
          {isLike ? (
            <PiHeartFill color="#fe7373" size={32} data-testid="PiHeartFill" />
          ) : (
            <PiHeartLight size={32} data-testid="PiHeartLight" />
          )}
        </LikeIcon>
      </InfluencerInfoSection>
      <TapContainer>
        <Tap aria-label="spot_tap" $active={activeTab === 'video'} onClick={() => setActiveTab('video')}>
          쿨한 그곳
        </Tap>
        <Tap aria-label="place_tap" $active={activeTab === 'map'} onClick={() => setActiveTab('map')}>
          쿨 플레이스
        </Tap>
      </TapContainer>
      <InfoContainer>
        {activeTab === 'video' && (
          <SortSection>
            <StyledButton
              aria-label="sort_btn"
              variant="white"
              size="small"
              onClick={() => setShowSortOptions(!showSortOptions)}
            >
              <span>{sortLabel[sortOption]}</span>
              <IoIosArrowDown size={16} />
            </StyledButton>
            {showSortOptions && (
              <SortDropdown>
                <SortItem onClick={() => handleSortChange('publishTime')}>
                  최신순 {sortOption === 'publishTime'}
                </SortItem>
                <SortItem onClick={() => handleSortChange('popularity')}>인기순 {sortOption === 'popularity'}</SortItem>
                <SortItem onClick={() => handleSortChange('likes')}>좋아요순 {sortOption === 'likes'}</SortItem>
              </SortDropdown>
            )}
          </SortSection>
        )}
        {activeTab === 'video' ? (
          <InfluencerVideoTap influencerId={id} sortOption={sortOption} />
        ) : (
          <QueryErrorResetBoundary>
            {({ reset }) => (
              <ErrorBoundary FallbackComponent={Error} onReset={reset}>
                <Suspense fallback={<Loading size={50} />}>
                  <InfluencerMapTap
                    influencerImg={influencerInfoData.influencerImgUrl}
                    influencerName={influencerInfoData.influencerName}
                  />
                </Suspense>
              </ErrorBoundary>
            )}
          </QueryErrorResetBoundary>
        )}
      </InfoContainer>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </PageContainer>
  );
}

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin-top: 30px;
  gap: 50px;

  @media screen and (max-width: 768px) {
    width: 100%;
    gap: 20px;
    margin-top: 20px;
    align-items: center;
  }
`;
const InfluencerInfoSection = styled.div`
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;
const Image = styled.div`
  width: 200px;
  border-radius: 50%;
  aspect-ratio: 1;

  @media screen and (max-width: 768px) {
    width: 100px;
  }
`;
const TextInfo = styled.div`
  width: 100%;
  padding-left: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 20px;

  @media screen and (max-width: 768px) {
    padding-left: 20px;
    gap: 8px;
  }
`;
const LikeIcon = styled.div`
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    top: 0px;
    right: 0px;
    svg {
      width: 30px;
      height: 26px;
    }
  }
`;
const Tap = styled.button<{ $active: boolean }>`
  width: 100%;
  height: 60px;
  font-size: 18px;
  font-weight: bold;
  color: ${(props) => {
    if (!props.$active) return props.theme.textColor === '#ffffff' ? 'white' : '#8c8c8c';
    return '#55ebff';
  }};
  border: none;
  border-bottom: 3px solid
    ${(props) => {
      if (!props.$active) return props.theme.textColor === '#ffffff' ? 'white' : '#8c8c8c';
      return '#55ebff';
    }};
  background: none;
  cursor: pointer;
  transition:
    color 0.3s ease,
    border-bottom 0.3s ease;

  @media screen and (max-width: 768px) {
    height: 50px;
    font-size: 16px;
    border-bottom: 2px solid;
  }
`;
const TapContainer = styled.div`
  display: flex;
  justify-content: space-between;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;
const InfoContainer = styled.div`
  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;

const SortSection = styled.div`
  position: relative;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
  margin-top: -40px;

  @media screen and (max-width: 768px) {
    margin-bottom: 6px;
    margin-top: -14px;
  }
`;

const StyledButton = styled(Button)`
  justify-content: space-between;
  gap: 8px;
  padding: 6px 10px;
  width: 90px;
  cursor: pointer;
  font-size: 14px;
  margin-left: auto;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};

  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }

  @media screen and (max-width: 768px) {
    width: 80px;
    font-size: 12px;
    padding: 4px 8px;
  }
`;

const SortDropdown = styled.div`
  position: absolute;
  top: 100%;
  z-index: 2;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 90px;
  margin-top: 4px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};

  @media screen and (max-width: 768px) {
    width: 80px;
  }
`;

const SortItem = styled.div`
  padding: 10px 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }

  @media screen and (max-width: 768px) {
    font-size: 12px;
    padding: 8px 10px;
  }
`;
