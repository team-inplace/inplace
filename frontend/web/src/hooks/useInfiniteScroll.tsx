import { useInView } from 'react-intersection-observer';
import { useEffect } from 'react';

interface InfiniteScrollOptions {
  fetchNextPage: () => void;
  hasNextPage: boolean;
  isFetchingNextPage: boolean;
}

export default function useInfiniteScroll({ fetchNextPage, hasNextPage, isFetchingNextPage }: InfiniteScrollOptions) {
  const { ref, inView } = useInView({
    rootMargin: '0px',
    threshold: 0,
  });

  useEffect(() => {
    if (inView && hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  }, [inView, hasNextPage, isFetchingNextPage, fetchNextPage]);

  return ref;
}
