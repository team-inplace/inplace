import { Outlet, useLocation } from 'react-router-dom';
import { Suspense } from 'react';
import styled from 'styled-components';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import Footer from '@/components/common/layouts/Footer';
import Loading from '@/components/common/layouts/Loading';
import Header, { HEADER_HEIGHT } from '@/components/common/layouts/Header/index';
import MainSkeleton from '@/components/Main/MainSkeleton';
import DetailSkeleton from '@/components/Detail/DetailSkeleton';
import useScrollToTop from '@/hooks/useScrollToTop';
import ErrorComponent from '@/components/common/layouts/Error';

export default function MainLayout() {
  const location = useLocation();
  const renderSkeleton = () => {
    if (location.pathname === '/') {
      return <MainSkeleton />;
    }
    if (location.pathname.startsWith('/detail')) {
      return <DetailSkeleton />;
    }
    return <Loading size={50} />;
  };
  useScrollToTop();
  return (
    <>
      <Header />
      <Wrapper>
        <InnerWrapper>
          <QueryErrorResetBoundary>
            {({ reset }) => (
              <ErrorBoundary FallbackComponent={ErrorComponent} onReset={reset}>
                <Suspense fallback={renderSkeleton()}>
                  <Outlet />
                </Suspense>
              </ErrorBoundary>
            )}
          </QueryErrorResetBoundary>
        </InnerWrapper>
        <Footer />
      </Wrapper>
    </>
  );
}

const Wrapper = styled.div`
  width: 960px;
  height: 100vh;
  margin: 0 auto;
  position: relative;
  display: flex;
  flex-direction: column;

  @media screen and (max-width: 768px) {
    width: 100%;
  }
`;

const InnerWrapper = styled.div`
  margin-top: ${HEADER_HEIGHT}px;
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  width: 100%;
  margin-bottom: 40px;
`;
