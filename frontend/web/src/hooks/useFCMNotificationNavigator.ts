import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCommentPosition } from '@/api/hooks/useGetCommentPage';
import { setupForegroundNotificationHandler } from '@/libs/FCM';

export default function useFCMNotificationNavigator() {
  const navigate = useNavigate();
  const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

  const navigateToComment = useCallback(
    async (postId: string | number, commentId: string | number) => {
      try {
        const result = await getCommentPosition(postId, commentId);
        if (result.postDeleted) {
          alert('삭제된 게시글입니다.');
          return;
        }
        if (result.commentDeleted) {
          alert('삭제된 댓글입니다.');
          navigate(`/post/${postId}`);
          return;
        }
        navigate(`/post/${postId}?commentPage=${result.commentPage}&commentId=${commentId}`);
      } catch (error) {
        console.error('댓글 위치 조회 실패:', error);
      }
    },
    [navigate],
  );

  useEffect(() => {
    if (!isReactNativeWebView) {
      setupForegroundNotificationHandler();
    }
  }, []);

  useEffect(() => {
    if (!navigator.serviceWorker || isReactNativeWebView) return undefined;

    const handleSWMessage = async (event: MessageEvent) => {
      if (event.data?.type === 'FCM_NOTIFICATION_NAVIGATE') {
        const { postId, commentId } = event.data;
        await navigateToComment(postId, commentId);
      }
    };

    navigator.serviceWorker.addEventListener('message', handleSWMessage);
    return () => navigator.serviceWorker.removeEventListener('message', handleSWMessage);
  }, [navigateToComment]);

  useEffect(() => {
    if (!isReactNativeWebView) return undefined;

    const handleExpoNotification = async (event: Event) => {
      const { postId, commentId } = (event as CustomEvent<{ postId: number; commentId: number }>).detail || {};
      if (postId != null && commentId != null) {
        await navigateToComment(postId, commentId);
      }
    };

    window.addEventListener('expoNotificationNavigate', handleExpoNotification);
    return () => window.removeEventListener('expoNotificationNavigate', handleExpoNotification);
  }, [navigateToComment]);
}
