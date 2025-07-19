import { useCallback, useEffect } from 'react';
import { useLocation } from 'react-router-dom';

export default function useScrollToTop() {
  const location = useLocation();

  const scrollToTop = useCallback(() => {
    window.scrollTo(0, 0);
  }, []);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  return scrollToTop;
}
