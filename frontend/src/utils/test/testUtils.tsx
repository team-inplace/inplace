import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { ErrorBoundary } from 'react-error-boundary';
import { AxiosError } from 'axios';
import { AuthContext } from '@/provider/Auth';
import { PlaceInfo, SpotData } from '@/types';
import ErrorComponent from '@/components/common/layouts/Error';
import ABTestProvider from '@/provider/ABTest';
import { BASE_URL } from '@/api/instance';

export function renderWithQueryClient(children: React.ReactNode) {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: 1,
        staleTime: 0,
      },
    },
  });
  return render(
    <AuthContext.Provider
      value={{
        isAuthenticated: false,
        handleLoginSuccess: jest.fn(),
        handleLogout: jest.fn(),
      }}
    >
      <MemoryRouter future={{ v7_relativeSplatPath: true }}>
        <ABTestProvider>
          <ErrorBoundary FallbackComponent={ErrorComponent}>
            <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
          </ErrorBoundary>
        </ABTestProvider>
      </MemoryRouter>
    </AuthContext.Provider>,
  );
}

export async function testErrorBoundaryBehavior({
  renderComponent,
  mockFunction,
  mockSuccessData,
}: {
  renderComponent: () => React.ReactNode;
  mockFunction: jest.Mock;
  mockSuccessData: { data: SpotData[]; error: null }[] | { data: PlaceInfo; error: null };
}) {
  mockFunction.mockImplementation(() => {
    const error = new AxiosError();
    error.response = {
      status: 500,
      statusText: 'Internal Server Error',
      headers: {},
      config: {
        headers: {},
        method: 'GET',
        url: `${BASE_URL}/videos/cool`,
      } as never,
      data: { message: 'Internal Server Error' },
    };
    throw error;
  });

  renderWithQueryClient(renderComponent());
  await waitFor(() => {
    expect(screen.getByText(/서버 오류 발생/)).toBeInTheDocument();
  });
  mockFunction.mockReturnValueOnce(mockSuccessData);

  fireEvent.click(screen.getByText('다시 시도하기'));

  // await waitFor(() => {
  //   expect(screen.queryByText(/서버 오류 발생/)).not.toBeInTheDocument();
  // });
}
