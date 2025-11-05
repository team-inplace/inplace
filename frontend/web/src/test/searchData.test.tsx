import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import * as api from '@/api/hooks/useGetSearchData';
import * as completeApi from '@/api/hooks/useGetSearchComplete';
import SearchPage from '@/pages/Search';
import { AuthContext } from '@/provider/Auth';
import ABTestProvider from '@/provider/ABTest';

jest.mock('@inplace-frontend-monorepo/shared/api/config', () => ({
  initializeConfig: jest.fn(),
  getConfig: jest.fn(() => ({
    baseURL: 'https://a7b2c3d4-dev.inplace.my:444',
    environment: 'development',
  })),
}));

jest.mock('@/api/hooks/useGetSearchComplete');
(completeApi.useGetSearchComplete as jest.Mock).mockReturnValue({
  data: [
    { result: 'Example 1', score: 0, searchType: 'Type1' },
    { result: 'Example 2', score: 0, searchType: 'Type2' },
  ],
});
jest.mock('@/api/hooks/useGetSearchData', () => ({
  ...jest.requireActual('@/api/hooks/useGetSearchData'),
  useGetSearchData: jest.fn(),
}));

Object.defineProperty(window, 'dataLayer', {
  writable: true,
  value: [],
});

(api.useGetSearchData as jest.Mock).mockImplementation(() => [
  {
    data: [{ influencerId: 1, influencerName: 'Test Influencer', influencerImgUrl: '', influencerJob: 'Test Infl2' }],
    isLoading: false,
    isError: false,
  },
  {
    data: [
      {
        videoId: 1,
        influencerName: '풍자 이(가) Video',
        videoUrl: '',
        place: {
          placeId: 1,
          placeName: '풍쟈',
          address: {
            address1: '대구',
            address2: '북구',
            address3: '대현동 119-11',
          },
        },
      },
    ],
    isLoading: false,
    isError: false,
  },
  {
    data: [
      {
        placeId: 1,
        placeName: '풍자또가',
        imageUrl: null,
        influencerName: '풍자',
        likes: true,
        address: {
          address1: '대구광역시',
          address2: '북구',
          address3: '대현로',
        },
      },
    ],
    isLoading: false,
    isError: false,
  },
]);

const queryClient = new QueryClient();

test('특정 키워드 검색 시 검색 결과가 잘 나오는 지 확인', async () => {
  render(
    <ABTestProvider>
      <AuthContext.Provider
        value={{
          isAuthenticated: false,
          handleLoginSuccess: jest.fn(),
          handleLogout: jest.fn(),
        }}
      >
        <MemoryRouter>
          <QueryClientProvider client={queryClient}>
            <SearchPage />
          </QueryClientProvider>
        </MemoryRouter>
      </AuthContext.Provider>
    </ABTestProvider>,
  );
  const searchInput = screen.getByPlaceholderText('인플루언서, 장소를 검색해주세요!');
  fireEvent.change(searchInput, { target: { value: 'Influencer' } });
  fireEvent.keyDown(searchInput, { key: 'Enter', code: 'Enter' });

  await waitFor(() => {
    expect(screen.getByText('검색 결과')).toBeInTheDocument();
    expect(screen.getByText('Test Influencer')).toBeInTheDocument();
  });
});
