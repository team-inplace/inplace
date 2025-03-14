import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import InfluencerItem from '@/components/common/Items/InfluencerItem';
import { AuthContext } from '@/provider/Auth';
import PlaceItem from '@/components/Map/PlaceSection/PlaceItem';

describe('좋아요/취소 기능 테스트', () => {
  let queryClient: QueryClient;

  beforeEach(() => {
    queryClient = new QueryClient();
  });

  const renderWithProviders = (ui: React.ReactNode) => {
    return render(
      <AuthContext.Provider
        value={{
          isAuthenticated: true,
          handleLoginSuccess: jest.fn(),
          handleLogout: jest.fn(),
        }}
      >
        <MemoryRouter>
          <QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>
        </MemoryRouter>
      </AuthContext.Provider>,
    );
  };
  const testLikeButtonFunctionality = async (likeButton: HTMLElement) => {
    expect(likeButton).toBeInTheDocument();
    expect(screen.queryByTestId('PiHeartFill')).not.toBeInTheDocument();

    fireEvent.click(likeButton);

    await waitFor(() => {
      expect(screen.getByTestId('PiHeartFill')).toBeInTheDocument();
    });

    fireEvent.click(likeButton);

    await waitFor(() => {
      expect(screen.queryByTestId('PiHeartFill')).not.toBeInTheDocument();
    });
  };

  test('인플루언서 좋아요/좋아요 취소가 반영되는지 확인', async () => {
    renderWithProviders(
      <InfluencerItem
        influencerId={2}
        influencerName="풍자"
        influencerImgUrl="https://via.placeholder.com/100"
        influencerJob="배우"
        likes={false}
      />,
    );

    const likeButton = screen.getByRole('button', { hidden: true });
    await testLikeButtonFunctionality(likeButton);
  });

  test('장소 좋아요/좋아요 취소가 반영되는지 확인', async () => {
    renderWithProviders(
      <PlaceItem
        placeId={2}
        placeName="료코"
        address={{
          address1: '대구',
          address2: '북구',
          address3: '대학로',
        }}
        category="맛집"
        videos={[{ influencerName: '성시경', videoUrl: '' }]}
        longitude="126.570667"
        latitude="33.450701"
        likes={false}
        onClick={() => {}}
      />,
    );

    const likeButton = screen.getByRole('button', { hidden: true });
    await testLikeButtonFunctionality(likeButton);
  });
});
