import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '@/hooks/useAuth';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';
import { usePostDeviceToken } from '@/api/hooks/usePostDeviceToken';
import { setupFCMToken } from '@/libs/FCM';

export default function AuthPage() {
  const navigate = useNavigate();
  const { handleLoginSuccess, isAuthenticated } = useAuth();
  const { data: userInfo } = useGetUserInfo();
  const { mutateAsync: postDeviceToken } = usePostDeviceToken();

  useEffect(() => {
    const processAuth = async () => {
      if (!isAuthenticated && userInfo?.nickname) {
        try {
          await handleLoginSuccess(userInfo);
          try {
            await setupFCMToken(postDeviceToken);
          } catch (fcmError) {
            console.error('FCM 설정 실패:', fcmError);
          }

          const redirectPath = localStorage.getItem('redirectPath');
          if (redirectPath) {
            localStorage.removeItem('redirectPath');
            navigate(redirectPath, { replace: true });
          }
        } catch (error) {
          console.error('AuthPage: 로그인 처리 실패', error);
          localStorage.removeItem('nickname');
          localStorage.setItem('isAuthenticated', 'false');
          navigate('/', { replace: true });
        }
      } else if (isAuthenticated) {
        navigate('/', { replace: true });
      }
    };

    processAuth();
  }, [isAuthenticated, userInfo, handleLoginSuccess, navigate]);

  return null;
}
