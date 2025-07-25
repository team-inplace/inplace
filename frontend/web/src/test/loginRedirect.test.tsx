import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { AuthContext } from '@/provider/Auth';
import InfluencerItem from '@/components/common/Items/InfluencerItem';

const queryClient = new QueryClient();

test('비로그인 시 좋아요 클릭하면 로그인 모달창이 뜨는지 확인', async () => {
  render(
    <AuthContext.Provider
      value={{
        isAuthenticated: false,
        handleLoginSuccess: jest.fn(),
        handleLogout: jest.fn(),
      }}
    >
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
          <InfluencerItem
            influencerId={2}
            influencerName="풍자"
            influencerImgUrl="https://via.placeholder.com/100"
            influencerJob="배우"
            likes={false}
          />
          ,
        </QueryClientProvider>
      </MemoryRouter>
    </AuthContext.Provider>,
  );

  const likeButton = screen.getByRole('button', { hidden: true });
  fireEvent.click(likeButton);

  await waitFor(() => {
    expect(screen.getByText('카카오 로그인')).toBeInTheDocument();
  });
});
