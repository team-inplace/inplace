import { ReactElement, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';

type PrivateRouteProps = {
  children: ReactElement;
};

export default function PrivateRoute({ children }: PrivateRouteProps) {
  const [shouldShowModal, setShouldShowModal] = useState(false);
  const { isAuthenticated, handleLoginSuccess } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const directRedirectPaths = ['/choice', '/auth'];
  const isProtectedPath = directRedirectPaths.includes(location.pathname);

  const { data: userInfo, isLoading, isError } = useGetUserInfo();

  useEffect(() => {
    if (isError) {
      console.error('[PrivateRoute] Failed to fetch user info.');
      navigate('/', { replace: true });
      return;
    }

    if (isProtectedPath && !isAuthenticated && !isLoading && !userInfo?.nickname) {
      navigate('/', { replace: true });
    }
  }, [isProtectedPath, isAuthenticated, userInfo, isLoading, isError, navigate]);

  const handleModalClose = () => {
    if (window.history.length > 2) {
      navigate(-1);
    } else {
      navigate('/');
    }
  };

  const handleModalSuccess = async () => {
    setShouldShowModal(false);
    if (userInfo?.nickname) {
      await handleLoginSuccess(userInfo);
    }
  };

  useEffect(() => {
    if (userInfo?.nickname && !isAuthenticated) {
      handleLoginSuccess(userInfo);
    }
  }, [userInfo, isAuthenticated, handleLoginSuccess]);

  if (isLoading) return null;

  if (shouldShowModal && !isAuthenticated) {
    return (
      <LoginModal
        currentPath={location.pathname}
        immediateOpen
        onClose={handleModalClose}
        onLoginSuccess={handleModalSuccess}
      />
    );
  }
  if (userInfo?.nickname || isAuthenticated) {
    return children;
  }

  return null;
}
