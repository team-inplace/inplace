import { PiHeartFill, PiHeartLight } from 'react-icons/pi';

import styled from 'styled-components';
import { useLocation, useParams } from 'react-router-dom';
import { Suspense, useCallback, useEffect, useState } from 'react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
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
import { useGetInfluencerVideo } from '@/api/hooks/useGetInfluencerVideo';

export default function InfluencerInfoPage() {
  const { id } = useParams() as { id: string };
  const { data: influencerInfoData } = useGetInfluencerInfo(id);
  const influencerId = Number(id);

  const { isAuthenticated } = useAuth();
  const location = useLocation();

  const [activeTab, setActiveTab] = useState<'video' | 'map'>('video');
  const [isLike, setIsLike] = useState(influencerInfoData.likes);
  const [showLoginModal, setShowLoginModal] = useState(false);

  const { mutate: postLike } = usePostInfluencerLike();
  const {
    data: videos,
    fetchNextPage: videoFetchNextPage,
    hasNextPage: videoHasNextPage,
    isFetchingNextPage: videoIsFetchingNextPage,
  } = useGetInfluencerVideo(id, 6);

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
    setIsLike(influencerInfoData.likes);
  }, [influencerInfoData.likes]);

  return (
    <PageContainer>
      <InfluencerInfoSection>
        <Image>
          <FallbackImage src={influencerInfoData.influencerImgUrl} alt={influencerInfoData.influencerName} />
        </Image>
        <TextInfo>
          <Text size="xl" weight="bold" variant="white">
            {influencerInfoData.influencerName}
          </Text>
          <Text size="s" weight="normal" variant="white">
            좋아요 수 {influencerInfoData.follower}명 • 쿨 플레이스 {influencerInfoData.placeCount}곳
          </Text>
          <Text size="xs" weight="normal" variant="white">
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
            <PiHeartLight color="white" size={32} data-testid="PiHeartLight" />
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
        {activeTab === 'video' ? (
          <InfluencerVideoTap
            items={videos.pages.flatMap((page) => page.content)}
            hasNextPage={videoHasNextPage}
            fetchNextPage={videoFetchNextPage}
            isFetchingNextPage={videoIsFetchingNextPage}
          />
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
  color: ${({ $active }) => ($active ? '#55ebff' : 'white')};
  border: none;
  border-bottom: 3px solid ${({ $active }) => ($active ? '#55ebff' : 'white')};
  background: none;
  cursor: pointer;
  transition:
    color 0.3s ease,
    border-bottom 0.3s ease;

  @media screen and (max-width: 768px) {
    height: 50px;
    font-size: 16px;
    border-bottom: 2px solid ${({ $active }) => ($active ? '#55ebff' : 'white')};
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
