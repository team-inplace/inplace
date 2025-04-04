import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import AuthProvider from '@/provider/Auth';
import MainLayout from '@/components/common/layouts/MainLayout';
import PrivatedRoute from '@/routes/component/PrivatedRoute';
import AuthPage from '@/pages/Auth';

import GlobalStyle from './global';
import MainPage from './pages/Main';
import ThemeProvider from './provider/Themes';
import DetailPage from './pages/Detail';
import InfluencerInfoPage from '@/pages/InfluencerInfo';
import InfluencerPage from '@/pages/Influencer';
import MapPage from './pages/Map';
import ABTestProvider from './provider/ABTest';

const MyPage = lazy(() => import('@/pages/My'));
const ChoicePage = lazy(() => import('@/pages/Choice'));
const SearchPage = lazy(() => import('@/pages/Search'));
// const ReviewPage = lazy(() => import('@/pages/Review'));
const NotFoundPage = lazy(() => import('@/pages/NotFound'));

function App() {
  return (
    <ABTestProvider>
      <ThemeProvider>
        <GlobalStyle />
        <AuthProvider>
          <Routes>
            <Route path="/" element={<MainLayout />}>
              <Route index path="/" element={<MainPage />} />
              <Route path="/influencer" element={<InfluencerPage />} />
              <Route path="/influencer/:id" element={<InfluencerInfoPage />} />
              <Route path="/map" element={<MapPage />} />
              <Route path="/detail/:id" element={<DetailPage />} />
              <Route path="/search" element={<SearchPage />} />
              <Route
                path="/my"
                element={
                  <PrivatedRoute>
                    <MyPage />
                  </PrivatedRoute>
                }
              />
              <Route
                path="/choice"
                element={
                  <PrivatedRoute>
                    <ChoicePage />
                  </PrivatedRoute>
                }
              />
              <Route path="*" element={<NotFoundPage />} />
            </Route>
            <Route
              path="/auth"
              element={
                <PrivatedRoute>
                  <AuthPage />
                </PrivatedRoute>
              }
            />
            {/* <Route path="/reviews/:uuid" element={<ReviewPage />} /> */}
          </Routes>
        </AuthProvider>
      </ThemeProvider>
    </ABTestProvider>
  );
}

export default App;
