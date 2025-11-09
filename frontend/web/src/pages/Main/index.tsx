import { useEffect, useState } from 'react';
import styled from 'styled-components';
import BaseLayout from '@/components/common/BaseLayout';
import MainBanner from '@/components/Main/MainBanner';
import PermissionModal from '@/components/common/modals/PermissionModal';
import ResearchModal from '@/components/common/modals/ResearchModal';

import { useGetMain } from '@/api/hooks/useGetMain';
import SearchBar from '@/components/common/SearchBarA';
import useAuth from '@/hooks/useAuth';
import { useGetLogoutVideo } from '@/api/hooks/useGetLogoutVideo';
import { useGetMyInfluencerVideo } from '@/api/hooks/useGetMyInfluencerVideo';
import useGetLocation from '@/hooks/useGetLocation';
import { useGetAroundVideo } from '@/api/hooks/useGetAroundVideo';
import MapSection from '@/components/Main/MapSection';
import { useABTest } from '@/provider/ABTest';
import { requestNotificationPermission } from '@/libs/FCM';
import usePermissions from '@/hooks/usePermissions';

export default function MainPage() {
  const { isAuthenticated } = useAuth();
  const testGroup = useABTest('map_ui_test');
  const { permissions, hasUndecidedPermissions, isLoading } = usePermissions();

  const [showPermissionModal, setShowPermissionModal] = useState(false);
  // 세션 동안 모달 표시 상태를 관리하는 state
  const [permissionModalShown, setPermissionModalShown] = useState(false);
  const [permissionModalClosed, setPermissionModalClosed] = useState(false);
  const location = useGetLocation(!isLoading && (permissions.location === 'granted' || permissionModalClosed));
  const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

  const [{ data: bannerData }, { data: influencersData }] = useGetMain();
  const [{ data: coolEatsVideoData }, { data: coolPlaysVideoData }, { data: newVideoData }] =
    useGetLogoutVideo(!isAuthenticated);
  const { data: myInfluencerVideoData } = useGetMyInfluencerVideo(!!isAuthenticated);
  const { data: aroundVideoData } = useGetAroundVideo(
    location?.lat ?? 37.5665,
    location?.lng ?? 126.978,
    !!isAuthenticated && !!location?.lat && !!location?.lng,
  );

  useEffect(() => {
    if (isReactNativeWebView) {
      // 앱에선 네이티브 권한을 사용하니까 일단 PermissionModal 안 띄움
      return;
    }
    if (!isLoading && !permissionModalShown) {
      if (hasUndecidedPermissions()) {
        setShowPermissionModal(true);
        setPermissionModalShown(true);
      }
    }
  }, [isLoading, hasUndecidedPermissions, permissionModalShown, isReactNativeWebView]);

  useEffect(() => {
    if (isReactNativeWebView) {
      requestNotificationPermission();
    }
  }, [isReactNativeWebView]);

  const handlePermissionModalClose = () => {
    setShowPermissionModal(false);
    setPermissionModalClosed(true);

    requestNotificationPermission().catch((error) => {
      console.error('알림 권한 요청 실패:', error);
    });
  };

  const shouldShowResearchModal = !showPermissionModal && !permissionModalShown && !permissionModalClosed;

  return (
    <>
      {showPermissionModal && <PermissionModal onClose={handlePermissionModalClose} />}
      {shouldShowResearchModal && <ResearchModal />}
      <Wrapper>
        {testGroup === 'A' && <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" />}
        <MainBanner items={bannerData} />
        <BaseLayout
          type="influencer"
          mainText="인플루언서"
          SubText=" 가 방문한 장소를 찾아볼까요?"
          items={influencersData.content}
        />
        {testGroup === 'B' && <MapSection highlightText="내 주변" />}
        {isAuthenticated ? (
          <>
            <BaseLayout
              type="spot"
              prevSubText="내 "
              mainText="인플루언서"
              SubText="가 방문한 그곳!"
              items={myInfluencerVideoData || []}
            />
            {location?.lat && location?.lng && (
              <BaseLayout
                type="spot"
                prevSubText="내 "
                mainText="주변"
                SubText="에 있는 그곳!"
                items={aroundVideoData || []}
              />
            )}
          </>
        ) : (
          <>
            <BaseLayout
              type="spot"
              prevSubText="지금 "
              mainText="쿨 "
              SubText="한 맛집!"
              items={coolEatsVideoData || []}
            />
            <BaseLayout
              type="spot"
              prevSubText="지금 "
              mainText="쿨 "
              SubText="한 놀거리!"
              items={coolPlaysVideoData || []}
            />
            <BaseLayout type="spot" mainText="새로" SubText=" 등록된 그곳!" items={newVideoData || []} />
          </>
        )}
      </Wrapper>
    </>
  );
}
const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 50px;
  padding: 16px 0;

  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    gap: 40px;
    align-items: center;
  }
`;
