import { useState, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { FaRegBell } from 'react-icons/fa';
import AlarmList from './AlarmList';
import { useGetAlarms } from '@/api/hooks/useGetAlarms';
import { AlarmData } from '@/types';
import useClickOutside from '@/hooks/useClickOutside';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';

interface AlarmButtonProps {
  iconSize?: number;
}

export default function AlarmButton({ iconSize = 20 }: AlarmButtonProps) {
  const { isAuthenticated } = useAuth();
  const [isAlarmOpen, setIsAlarmOpen] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const alarmRef = useRef<HTMLDivElement>(null);
  const location = useLocation();

  const { data: alarms = [] } = useGetAlarms(isAuthenticated && isAlarmOpen);
  const unreadCount = alarms.filter((alarm: AlarmData) => !alarm.checked).length;

  // 알림 토글
  const toggleAlarm = () => {
    if (!isAuthenticated) {
      setShowLoginModal(true);
      return;
    }
    setIsAlarmOpen((prev) => !prev);
  };

  // 외부 클릭 시 알림창 닫기 (기존 헤더와 동일한 훅 사용)
  useClickOutside([alarmRef], () => setIsAlarmOpen(false));

  return (
    <>
      <AlarmContainer ref={alarmRef}>
        <BellButton onClick={toggleAlarm} $isActive={isAlarmOpen}>
          <FaRegBell size={iconSize} />
          {isAuthenticated && unreadCount > 0 && <UnreadBadge>{unreadCount > 99 ? '99+' : unreadCount}</UnreadBadge>}
        </BellButton>
        {isAuthenticated && <AlarmList alarms={alarms} isVisible={isAlarmOpen} />}
      </AlarmContainer>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}

const AlarmContainer = styled.div`
  position: relative;
  display: inline-block;
`;

const BellButton = styled.button<{
  $isActive: boolean;
}>`
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: ${(props) => props.theme.textColor};
  transition: transform 0.3s ease;
  position: relative;

  &:hover {
    transform: scale(1.04);
  }
`;

const UnreadBadge = styled.div`
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  top: -2px;
  right: -6px;
  background-color: #ff4757;
  color: white;
  border-radius: 10px;
  min-width: 14px;
  height: 14px;
  font-size: 10px;
  box-sizing: border-box;
`;
