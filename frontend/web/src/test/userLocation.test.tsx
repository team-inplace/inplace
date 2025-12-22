import { render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { AuthContext } from '@/provider/Auth';
import MainPage from '@/pages/Main';
import * as locationHook from '@/hooks/useGetLocation';
import * as api from '@/api/hooks/useGetAroundVideo';

jest.mock('@inplace-frontend-monorepo/shared/api/config', () => ({
  initializeConfig: jest.fn(),
  getConfig: jest.fn(() => ({
    baseURL: 'https://a7b2c3d4-dev.inplace.my:444',
    environment: 'development',
  })),
}));

jest.mock('@/api/hooks/useGetAroundVideo');
jest.mock('@/hooks/useGetLocation');
jest.mock('@/libs/FCM/firebaseSetting', () => ({
  app: {},
  messaging: {},
}));

jest.mock('@/libs/FCM/fcmTokenManager', () => ({
  requestNotificationPermission: jest.fn(),
  setupFCMToken: jest.fn(),
}));

const queryClient = new QueryClient();

test('사용자 위치 기반 내주변 비디오 호출 확인', async () => {
  const mockLocation = { lat: 37.5665, lng: 126.978 };
  (locationHook.default as jest.Mock).mockReturnValue(mockLocation);

  (api.useGetAroundVideo as jest.Mock).mockReturnValue({
    data: [
      {
        videoId: 1,
        influencerName: 'Test Video',
        videoUrl: 'https://example.com',
        place: {
          placeId: 0,
          placeName: 'Test Place',
          address: {
            address1: '대구',
            address2: '북구',
            address3: '대현동 119-11',
          },
        },
      },
    ],
    isLoading: false,
    error: null,
    isError: false,
    isSuccess: true,
  });

  render(
    <AuthContext.Provider
      value={{
        isAuthenticated: true,
        handleLoginSuccess: jest.fn(),
        handleLogout: jest.fn(),
      }}
    >
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
          <MainPage />
        </QueryClientProvider>
      </MemoryRouter>
    </AuthContext.Provider>,
  );

  await waitFor(() => {
    expect(screen.getByText('주변')).toBeInTheDocument();
    expect(screen.getByText(/Test Video/)).toBeInTheDocument();
  });

  expect(api.useGetAroundVideo).toHaveBeenCalledWith(mockLocation.lat, mockLocation.lng, true);
});
