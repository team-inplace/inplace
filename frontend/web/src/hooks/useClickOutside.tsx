import { useEffect } from 'react';

export default function useClickOutside(refs: React.RefObject<HTMLElement>[], callback: () => void) {
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      const isInside = refs.some((ref) => ref.current?.contains(event.target as Node));
      if (!isInside) {
        callback();
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [refs, callback]);
}
