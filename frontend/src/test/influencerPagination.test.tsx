import { render, screen, fireEvent } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { useGetAllInfluencers } from '@/api/hooks/useGetAllInfluencers';
import InfluencerPage from '@/pages/Influencer';
import { AuthContext } from '@/provider/Auth';
import * as api from '@/api/hooks/useGetSearchData';

jest.mock('@/api/hooks/useGetAllInfluencers', () => ({
  useGetAllInfluencers: jest.fn(),
}));
jest.mock('@/api/hooks/useGetSearchData', () => {
  return {
    ...jest.requireActual('@/api/hooks/useGetSearchData'),
    useGetSearchData: jest.fn(),
  };
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
  { data: [{ placeId: 1, placeName: '풍자또가', imageUrl: '', likes: true }], isLoading: false, isError: false },
]);

const queryClient = new QueryClient();

describe('인플루언서 페이지 페이지네이션 기능 테스트', () => {
  test('다음 페이지 클릭 시 다음 페이지의 값을 잘 받아오는지 확인', async () => {
    const mockDataPage1 = {
      content: [
        {
          influencerId: 1,
          influencerName: 'Influencer 1',
          influencerImgUrl: 'https://via.placeholder.com/100',
          influencerJob: '배우',
          likes: false,
        },
        {
          influencerId: 2,
          influencerName: 'Influencer 2',
          influencerImgUrl: 'https://via.placeholder.com/100',
          influencerJob: '배우',
          likes: false,
        },
      ],
      totalPages: 2,
      totalElements: 4,
      size: 2,
      number: 0,
      sort: {
        empty: true,
        sorted: true,
        unsorted: true,
      },
      numberOfElements: 0,
      pageable: {
        offset: 0,
        sort: {
          empty: true,
          sorted: true,
          unsorted: true,
        },
        paged: true,
        pageNumber: 0,
        pageSize: 2,
        unpaged: false,
      },
      first: true,
      lase: true,
      empty: true,
    };

    const mockDataPage2 = {
      content: [
        {
          influencerId: 11,
          influencerName: 'Influencer 11',
          influencerImgUrl: 'https://via.placeholder.com/100',
          influencerJob: '배우',
          likes: false,
        },
        {
          influencerId: 12,
          influencerName: 'Influencer 12',
          influencerImgUrl: 'https://via.placeholder.com/100',
          influencerJob: '배우',
          likes: false,
        },
      ],
      totalElements: 4,
      size: 2,
      number: 1,
      sort: {
        empty: true,
        sorted: true,
        unsorted: true,
      },
      numberOfElements: 0,
      pageable: {
        offset: 0,
        sort: {
          empty: true,
          sorted: true,
          unsorted: true,
        },
        paged: true,
        pageNumber: 0,
        pageSize: 2,
        unpaged: false,
      },
      first: true,
      lase: true,
      empty: true,
    };

    (useGetAllInfluencers as jest.Mock).mockImplementation(({ page }) => {
      if (page === 0) return { data: mockDataPage1 };
      if (page === 1) return { data: mockDataPage2 };
      return { data: {} };
    });

    render(
      <AuthContext.Provider
        value={{
          isAuthenticated: false,
          handleLoginSuccess: jest.fn(),
          handleLogout: jest.fn(),
        }}
      >
        <MemoryRouter future={{ v7_relativeSplatPath: true }}>
          <QueryClientProvider client={queryClient}>
            <InfluencerPage />
          </QueryClientProvider>
        </MemoryRouter>
      </AuthContext.Provider>,
    );

    expect(screen.getByText('Influencer 1')).toBeInTheDocument();

    const nextPageButton = screen.getByRole('button', { name: 'page_number_2' });
    fireEvent.click(nextPageButton);

    expect(screen.getByText('Influencer 11')).toBeInTheDocument();
  });
});
