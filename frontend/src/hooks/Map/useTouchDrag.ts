import { useState, useRef, useCallback } from 'react';

export default function useTouchDrag(setIsListExpanded: React.Dispatch<React.SetStateAction<boolean>>) {
  const [translateY, setTranslateY] = useState(window.innerHeight);
  const lastMoveTimeRef = useRef(0);
  const dragStartRef = useRef({ isDragging: false, startY: 0, startTranslate: window.innerHeight });

  const handleTouchStart = useCallback(
    (e: React.TouchEvent) => {
      dragStartRef.current = {
        isDragging: true,
        startY: e.touches[0].clientY,
        startTranslate: translateY,
      };
    },
    [translateY],
  );

  const handleTouchMove = useCallback((e: React.TouchEvent) => {
    if (!dragStartRef.current.isDragging) return;
    const now = Date.now();
    if (now - lastMoveTimeRef.current < 50) return;
    lastMoveTimeRef.current = now;

    const delta = e.touches[0].clientY - dragStartRef.current.startY;
    setTranslateY((prev) => Math.max(0, Math.min(window.innerHeight, prev + delta)));
  }, []);

  const handleTouchEnd = useCallback(() => {
    dragStartRef.current.isDragging = false;

    const threshold = 50;
    const autoCloseThreshold = window.innerHeight * 0.75;

    if (Math.abs(translateY - dragStartRef.current.startTranslate) < threshold) {
      setTranslateY(dragStartRef.current.startTranslate);
    } else if (translateY > autoCloseThreshold) {
      setTranslateY(window.innerHeight);
      setIsListExpanded(false);
    }
  }, [translateY, setIsListExpanded]);

  return { translateY, setTranslateY, handleTouchStart, handleTouchMove, handleTouchEnd };
}
