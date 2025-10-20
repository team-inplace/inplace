import styled from 'styled-components';
import { useState, useRef, useEffect } from 'react';
import { FaChevronLeft } from 'react-icons/fa';
import AlarmItem from './AlarmItem';
import { AlarmData } from '@/types';

interface AlarmListProps {
  alarms: AlarmData[];
  isVisible: boolean;
  onClose?: () => void;
}

export default function AlarmList({ alarms, isVisible, onClose }: AlarmListProps) {
  const [translateX, setTranslateX] = useState(0);
  const [isClosing, setIsClosing] = useState(false);
  const startXpos = useRef(0);
  const isDragging = useRef(false);
  const CLOSE_ANIMATION_DURATION = 500;

  useEffect(() => {
    if (isVisible) {
      setIsClosing(false);
      setTranslateX(0);
    }
  }, [isVisible]);

  const handleClose = () => {
    setIsClosing(true);
    setTranslateX(window.innerWidth);
    setTimeout(() => {
      onClose?.();
      setIsClosing(false);
    }, CLOSE_ANIMATION_DURATION);
  };

  if (!isVisible && !isClosing) return null;

  const handleTouchStart = (e: React.TouchEvent) => {
    startXpos.current = e.touches[0].clientX;
    isDragging.current = true;
  };

  const handleTouchMove = (e: React.TouchEvent) => {
    if (!isDragging.current) return;

    const currentXpos = e.touches[0].clientX;
    const deltaX = currentXpos - startXpos.current;
    if (deltaX > 0) {
      setTranslateX(deltaX);
    }
  };

  const handleTouchEnd = () => {
    if (!isDragging.current) return;
    isDragging.current = false;

    if (translateX > 50) {
      handleClose();
    } else {
      setTranslateX(0);
    }
  };

  return (
    <AlarmListContainer
      $translateX={translateX}
      $isClosing={isClosing}
      onTouchStart={handleTouchStart}
      onTouchMove={handleTouchMove}
      onTouchEnd={handleTouchEnd}
    >
      <AlarmHeader>
        <MobileCloseButton onClick={handleClose}>
          <FaChevronLeft size={20} />
        </MobileCloseButton>
        <AlarmTitle>알림</AlarmTitle>
      </AlarmHeader>

      <AlarmScrollContainer>
        {alarms.length > 0 ? (
          alarms.map((alarm) => <AlarmItem key={alarm.alarmId} {...alarm} />)
        ) : (
          <EmptyMessage>알림이 없습니다.</EmptyMessage>
        )}
      </AlarmScrollContainer>
    </AlarmListContainer>
  );
}

const AlarmListContainer = styled.div<{ $translateX?: number; $isClosing?: boolean }>`
  position: absolute;
  top: calc(100% + 6px);
  right: -50px;
  width: 330px;
  max-height: 400px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1a1a1a' : '#ffffff')};
  border: 1px solid ${({ theme }) => (theme.backgroundColor === '#292929' ? '#2a2a2a' : '#e1e5e9')};
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 21;
  overflow: hidden;

  @media screen and (max-width: 768px) {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    width: 100%;
    max-height: 100%;
    height: 100%;
    border: none;
    border-radius: 0;
    z-index: 100;
    transform: translateX(${({ $translateX = 0 }) => `${$translateX}px`});
    transition: transform 0.5s ease-out;
    animation: slideInFromRight 0.5s ease-out;
    touch-action: pan-x;
  }

  @keyframes slideInFromRight {
    from {
      transform: translateX(100%);
    }
    to {
      transform: translateX(0);
    }
  }
`;

const AlarmHeader = styled.div`
  padding: 16px;
  border-bottom: 1px solid ${({ theme }) => (theme.backgroundColor === '#292929' ? '#2a2a2a' : '#f0f0f0')};
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1a1a1a' : '#ffffff')};

  @media screen and (max-width: 768px) {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 18px 16px;
  }
`;

const AlarmTitle = styled.h3`
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: inherit;
`;

const AlarmScrollContainer = styled.div`
  max-height: 320px;
  overflow-y: auto;
  padding: 0 10px;

  @media screen and (max-width: 768px) {
    max-height: calc(100vh - 80px);
    padding: 0 16px 20px;
  }

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#525252' : '#8e8e8e')};
    border-radius: 3px;
  }
`;

const EmptyMessage = styled.div`
  padding: 32px 16px;
  text-align: center;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#666' : '#999')};
  font-size: 14px;
`;

const MobileCloseButton = styled.button`
  display: none;

  @media screen and (max-width: 768px) {
    position: absolute;
    left: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: none;
    border: none;
    color: inherit;
    padding: 4px;
    cursor: pointer;
  }
`;
