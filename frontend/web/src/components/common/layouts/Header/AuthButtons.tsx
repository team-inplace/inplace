import { useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { FiSun, FiMoon } from 'react-icons/fi';
import { MdOutlineLogout } from 'react-icons/md';
import { TiHome } from 'react-icons/ti';
import { useRef, useState } from 'react';
import { AiOutlineUser } from 'react-icons/ai';
import useAuth from '@/hooks/useAuth';
import useTheme from '@/hooks/useTheme';
import LoginModal from '@/components/common/modals/LoginModal';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';
import FallbackImage from '../../Items/FallbackImage';
import useClickOutside from '@/hooks/useClickOutside';
import { requestNotificationPermission } from '@/libs/FCM';
import AlarmButton from './Alarm/AlarmButton';

export default function AuthButtons() {
  const { isAuthenticated, handleLogout } = useAuth();
  const { data: imgSrc } = useGetUserInfo();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const isDarkMode = theme === 'dark';
  const location = useLocation();
  const dropdownRef = useRef<HTMLDivElement>(null);

  const [isOpen, setIsOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleClickProfile = () => {
    setIsOpen(!isOpen);
  };
  const handleMyPage = () => {
    navigate('/my');
    setIsOpen(false);
  };

  // 모달이 열려 있을 때는 닫지 않도록 조건 추가
  useClickOutside([dropdownRef], () => {
    if (!isModalOpen) {
      setIsOpen(false);
    }
  });

  const handleLoginIconClick = async () => {
    try {
      await requestNotificationPermission();
    } catch (error) {
      console.log('알림 권한 요청 실패:', error);
    }
  };

  return (
    <Container>
      <AlarmButton iconSize={22} />
      <UserProfile ref={dropdownRef}>
        <Profile onClick={handleClickProfile}>
          {isAuthenticated ? (
            <FallbackImage src={imgSrc?.imgUrl} alt="profile" />
          ) : (
            <IconButton>
              <AiOutlineUser size={26} />
            </IconButton>
          )}
        </Profile>
        {isOpen && (
          <UserDropdown>
            {isAuthenticated ? (
              <>
                {location.pathname !== '/my' && (
                  <DropdownItem onClick={handleMyPage}>
                    <TiHome size={16} />
                    마이페이지
                  </DropdownItem>
                )}
                <DropdownItem onClick={toggleTheme} $isDarkMode={isDarkMode}>
                  <ThemeButton $isDarkMode={isDarkMode}>
                    {isDarkMode ? <FiSun size={16} color="white" /> : <FiMoon size={16} color="black" />}
                  </ThemeButton>
                  테마 변경
                </DropdownItem>
                <DropdownItem onClick={handleLogout}>
                  <MdOutlineLogout size={16} />
                  로그아웃
                </DropdownItem>
              </>
            ) : (
              <>
                <LoginModal
                  currentPath={location.pathname}
                  onClose={() => {
                    setIsModalOpen(false);
                    setIsOpen(false);
                  }}
                >
                  {(openModal: () => void) => (
                    <DropdownItem
                      onClick={async () => {
                        await handleLoginIconClick();
                        setIsModalOpen(true);
                        openModal();
                      }}
                    >
                      <AiOutlineUser size={16} />
                      로그인
                    </DropdownItem>
                  )}
                </LoginModal>
                <DropdownItem onClick={toggleTheme} $isDarkMode={isDarkMode}>
                  <ThemeButton $isDarkMode={isDarkMode}>
                    {isDarkMode ? <FiSun size={16} color="white" /> : <FiMoon size={16} color="black" />}
                  </ThemeButton>
                  테마 변경
                </DropdownItem>
              </>
            )}
          </UserDropdown>
        )}
      </UserProfile>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;

  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const IconButton = styled.div`
  cursor: pointer;
`;

const ThemeButton = styled.button<{ $isDarkMode: boolean }>`
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const UserProfile = styled.div`
  position: relative;
`;

const Profile = styled.div`
  width: 30px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
  display: flex;
  cursor: pointer;
  align-items: center;
  justify-content: center;
`;

const UserDropdown = styled.div`
  position: absolute;
  top: 100%;
  right: 0px;
  padding: 4px 0px;
  z-index: 100;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#434343' : 'white')};
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 110px;
  margin-top: 4px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};

  @media screen and (max-width: 768px) {
    width: 80px;
  }
`;

const DropdownItem = styled.div<{ $isDarkMode?: boolean }>`
  padding: 12px 12px;
  display: flex;
  gap: 8px;
  align-items: end;
  cursor: pointer;
  font-size: 14px;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
    button {
      transform: rotate(${(props) => (props.$isDarkMode ? '30deg' : '36deg')});
      transition: transform 0.3s ease;
    }
  }

  @media screen and (max-width: 768px) {
    font-size: 12px;
    padding: 8px 10px;
  }
`;
