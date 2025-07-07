import { useCallback } from 'react';

export default function useAutoResizeTextarea() {
  const handleResizeHeight = useCallback((textarea: HTMLTextAreaElement | null) => {
    if (!textarea) return;
    const ta = textarea;
    ta.style.height = 'auto';
    ta.style.height = `${ta.scrollHeight}px`;
  }, []);

  return handleResizeHeight;
}
