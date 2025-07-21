import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import Review from '@/components/Detail/ReviewTap/Review';
import { useGetReview } from '@/api/hooks/useGetReview';
import { ReviewData } from '@/types';

const queryClient = new QueryClient();

let reviewsMockData = [
  {
    reviewId: 1,
    likes: true,
    comment: '정말 좋았어요! 다음에 또 오고 싶습니다',
    userNickname: '사용자1',
    createdDate: new Date('2024-10-01T12:00:00Z'),
    mine: true,
  },
  {
    reviewId: 2,
    likes: false,
    comment: '별로였어요. 개선이 필요합니다.',
    userNickname: '사용자2',
    createdDate: new Date('2024-10-02T15:30:00Z'),
    mine: false,
  },
];

const generateMockData = (reviews: ReviewData[]) => ({
  content: reviews,
  totalElements: reviews.length,
  size: 2,
  number: 1,
  sort: {
    empty: true,
    sorted: true,
    unsorted: true,
  },
  numberOfElements: 2,
  pageable: {
    offset: 0,
    sort: {
      empty: true,
      sorted: true,
      unsorted: true,
    },
    paged: true,
    pageNumber: 0,
    pageSize: 1,
    unpaged: false,
  },
  first: true,
  last: true,
  empty: reviews.length === 0,
});
let currentMockData = generateMockData(reviewsMockData);

jest.mock('@/api/hooks/useGetReview', () => ({
  useGetReview: jest.fn(() => ({
    data: currentMockData,
    isLoading: false,
  })),
}));

jest.mock('@/api/hooks/useDeleteReview', () => ({
  useDeleteReview: jest.fn(() => ({
    mutate: (reviewId: string, { onSuccess }: { onSuccess: () => void }) => {
      reviewsMockData = reviewsMockData.filter((review) => review.reviewId !== Number(reviewId));

      currentMockData = generateMockData(reviewsMockData);

      (useGetReview as jest.Mock).mockImplementation(() => ({
        data: currentMockData,
        isLoading: false,
      }));

      queryClient.setQueryData(['review'], currentMockData);

      onSuccess();
    },
  })),
}));

function TestComponent() {
  const { data: reviews } = useGetReview({ page: 1, size: 10, id: '1' });
  if (!reviews) return null;

  return <Review items={reviews.content} />;
}

describe('Review 테스트', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    queryClient.clear();

    reviewsMockData = [
      {
        reviewId: 1,
        likes: true,
        comment: '정말 좋았어요! 다음에 또 오고 싶습니다',
        userNickname: '사용자1',
        createdDate: new Date('2024-10-01T12:00:00Z'),
        mine: true,
      },
      {
        reviewId: 2,
        likes: false,
        comment: '별로였어요. 개선이 필요합니다.',
        userNickname: '사용자2',
        createdDate: new Date('2024-10-02T15:30:00Z'),
        mine: false,
      },
    ];
    currentMockData = generateMockData(reviewsMockData);
  });

  test('리뷰 삭제가 반영되는지 확인', async () => {
    window.confirm = jest.fn().mockImplementation(() => true);
    window.alert = jest.fn();

    const { rerender } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter future={{ v7_relativeSplatPath: true }}>
          <TestComponent />
        </MemoryRouter>
      </QueryClientProvider>,
    );

    expect(screen.getByText('정말 좋았어요! 다음에 또 오고 싶습니다')).toBeInTheDocument();

    const deleteButton = screen.getByText('삭제');
    fireEvent.click(deleteButton);

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith('삭제되었습니다.');
    });

    // 강제 리렌더링
    rerender(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter future={{ v7_relativeSplatPath: true }}>
          <TestComponent />
        </MemoryRouter>
      </QueryClientProvider>,
    );

    await waitFor(() => {
      expect(screen.queryByText('정말 좋았어요! 다음에 또 오고 싶습니다')).not.toBeInTheDocument();
    });
  });
});
