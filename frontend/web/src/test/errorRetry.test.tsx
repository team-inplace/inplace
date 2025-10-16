import DetailPage from '@/pages/Detail';
import { useGetPlaceInfo } from '@/api/hooks/useGetPlaceInfo';
import MainPage from '@/pages/Main';
import { useGetLogoutVideo } from '@/api/hooks/useGetLogoutVideo';
import { testErrorBoundaryBehavior } from '@/utils/test/testUtils';

jest.mock('@/libs/FCM/firebaseSetting', () => ({
  app: {},
  messaging: {},
}));

jest.mock('@/hooks/usePermissions', () => ({
  __esModule: true,
  default: () => ({
    permissions: { location: 'granted', notifications: 'granted' },
  }),
}));

beforeAll(() => {
  jest.clearAllMocks();
  jest.spyOn(console, 'error').mockImplementation(() => {});
});

afterAll(() => {
  (console.error as jest.Mock).mockRestore();
});

jest.mock('@/api/hooks/useGetLogoutVideo', () => ({
  useGetLogoutVideo: jest.fn(),
}));

const mockUseGetLogoutVideo = useGetLogoutVideo as jest.Mock;

describe('메인페이지 ErrorBoundary 테스트', () => {
  test('에러 컴포넌트에서 다시 시도 버튼 클릭시 api 재호출되는지 확인', async () => {
    await testErrorBoundaryBehavior({
      renderComponent: () => <MainPage />,
      mockFunction: mockUseGetLogoutVideo,
      mockSuccessData: [
        {
          data: [
            {
              videoId: 1,
              influencerName: 'Cool Eats Video',
              videoUrl: 'https://youtu.be/qbqquv_8wM0?si=j7LiU5DSfTVpKa1I',
              place: {
                placeId: 1,
                placeName: '이선장네',
                address: {
                  address1: '대구',
                  address2: '북구',
                  address3: '대현동 119-11',
                },
              },
            },
          ],
          error: null,
        },
        {
          data: [
            {
              videoId: 1,
              influencerName: 'Cool Plays Video',
              videoUrl: 'https://youtu.be/qbqquv_8wM0?si=j7LiU5DSfTVpKa1I',
              place: {
                placeId: 1,
                placeName: '이선장네',
                address: {
                  address1: '대구',
                  address2: '북구',
                  address3: '대현동 119-11',
                },
              },
            },
          ],
          error: null,
        },
        {
          data: [
            {
              videoId: 2,
              influencerName: 'New Video',
              videoUrl: 'https://youtu.be/qbqquv_8wM0?si=j7LiU5DSfTVpKa1I',
              place: {
                placeId: 1,
                placeName: '이선장네',
                address: {
                  address1: '대구',
                  address2: '북구',
                  address3: '대현동 119-11',
                },
              },
            },
          ],
          error: null,
        },
      ],
    });
  });
});

jest.mock('@/api/hooks/useGetPlaceInfo', () => ({
  useGetPlaceInfo: jest.fn(),
}));

const mockUseGetPlaceInfo = useGetPlaceInfo as jest.Mock;

describe('세부페이지 ErrorBoundary 테스트', () => {
  test('에러 컴포넌트에서 다시 시도 버튼 클릭시 api 재호출되는지 확인', async () => {
    await testErrorBoundaryBehavior({
      renderComponent: () => <DetailPage />,
      mockFunction: mockUseGetPlaceInfo,
      mockSuccessData: {
        data: {
          placeName: 'Test Place',
          placeId: 123,
          videos: [{ influencerName: '성시경', videoUrl: 'https://test.video' }],
          facility: {},
          kakaoPlaceUrl: 'https://google.com',
          naverPlaceUrl: 'https://naver.com',
          googlePlaceUrl: 'https://google.com',
          longitude: '126.978',
          latitude: '37.5665',
          openingHours: ['월~금 10:00 - 12:00', '월~금 10:00 - 12:00', '월~금 10:00 - 12:00'],
          rating: 4.1,
          reviewLikes: {
            like: 0,
            dislike: 0,
          },
          likedCount: 100,
          likes: true,
          googleReviews: [
            {
              like: true,
              text: '',
              name: '',
              publishTime: new Date(),
            },
          ],
          address: {
            address1: '',
            address2: '',
            address3: '',
          },
          category: '',
          surroundVideos: [],
        },
        error: null,
      },
    });
  });
});
