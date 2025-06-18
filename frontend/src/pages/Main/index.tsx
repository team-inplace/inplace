import styled from 'styled-components';
import BaseLayout from '@/components/common/BaseLayout';
import MainBanner from '@/components/Main/MainBanner';
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

export default function MainPage() {
  const { isAuthenticated } = useAuth();
  const location = useGetLocation();
  const testGroup = useABTest('map_ui_test');

  const [{ data: bannerData }, { data: influencersData }] = useGetMain();
  const [{ data: coolEatsVideoData }, { data: coolPlaysVideoData }, { data: newVideoData }] =
    useGetLogoutVideo(!isAuthenticated);
  const { data: myInfluencerVideoData } = useGetMyInfluencerVideo(!!isAuthenticated);
  const { data: aroundVideoData } = useGetAroundVideo(
    location?.lat ?? 37.5665,
    location?.lng ?? 126.978,
    !!isAuthenticated && !!location?.lat && !!location?.lng,
  );

  return (
    <>
      <ResearchModal />
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
