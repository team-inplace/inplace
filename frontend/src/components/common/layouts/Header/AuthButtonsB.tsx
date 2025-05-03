import { useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { FiSun, FiMoon } from 'react-icons/fi';
import { MdOutlineLogout } from 'react-icons/md';
import { useRef, useState } from 'react';
import { AiOutlineUser } from 'react-icons/ai';
import { RiUserUnfollowLine } from 'react-icons/ri';
import useAuth from '@/hooks/useAuth';
import useTheme from '@/hooks/useTheme';
import LoginModal from '@/components/common/modals/LoginModal';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';
import FallbackImage from '../../Items/FallbackImage';
import useClickOutside from '@/hooks/useClickOutside';
import { useDeleteUser } from '@/api/hooks/useDeleteUser';

export default function AuthButtons() {
  const { isAuthenticated, handleLogout } = useAuth();
  const { data: imgSrc } = useGetUserInfo();
  const { mutate: deleteUser } = useDeleteUser();
  const { theme, toggleTheme } = useTheme();
  const isDarkMode = theme === 'dark';
  const location = useLocation();
  const dropdownRef = useRef<HTMLDivElement>(null);

  const [isOpen, setIsOpen] = useState(isAuthenticated);
  const handleClickProfile = () => {
    setIsOpen(!isOpen);
  };

  const handleDeleteUser = () => {
    if (window.confirm('정말 회원 탈퇴를 하시겠습니까?')) {
      deleteUser(undefined, {
        onSuccess: () => {
          handleLogout();
          alert('회원탈퇴가 완료되었습니다.');
        },
        onError: (error) => {
          console.error('회원탈퇴 실패:', error);
          alert('회원탈퇴에 실패했습니다. 다시 시도해주세요.');
        },
      });
    }
  };

  useClickOutside([dropdownRef], () => setIsOpen(false));

  return (
    <Container>
      <ThemeButton onClick={toggleTheme} aria-label="테마 변경 버튼_B" $isDarkMode={isDarkMode}>
        {isDarkMode ? <FiSun size={24} color="white" /> : <FiMoon size={22} color="black" />}
      </ThemeButton>
      {isAuthenticated ? (
        <UserProfile ref={dropdownRef}>
          <Profile onClick={handleClickProfile}>
            <FallbackImage src={imgSrc?.imgUrl} alt="profile" />
          </Profile>
          {isOpen && (
            <UserDropdown>
              <DropdownItem onClick={handleLogout}>
                <MdOutlineLogout size={16} />
                로그아웃
              </DropdownItem>
              <DropdownItem onClick={handleDeleteUser}>
                <RiUserUnfollowLine size={16} />
                회원탈퇴
              </DropdownItem>
            </UserDropdown>
          )}
        </UserProfile>
      ) : (
        <LoginModal currentPath={location.pathname}>
          {(openModal: () => void) => (
            <IconButton onClick={openModal}>
              <AiOutlineUser size={26} />
            </IconButton>
          )}
        </LoginModal>
      )}
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
  transition: transform 0.3s ease;

  &:hover {
    transform: rotate(${(props) => (props.$isDarkMode ? '30deg' : '36deg')});
  }
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
`;

const UserDropdown = styled.div`
  position: absolute;
  top: 100%;
  right: 0px;
  padding: 4px 0px;
  z-index: 2;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#434343' : 'white')};
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100px;
  margin-top: 4px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};

  @media screen and (max-width: 768px) {
    width: 80px;
  }
`;

const DropdownItem = styled.div`
  padding: 12px 10px;
  display: flex;
  justify-content: space-around;
  align-items: end;
  cursor: pointer;
  font-size: 14px;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }

  @media screen and (max-width: 768px) {
    font-size: 12px;
    padding: 8px 10px;
  }
`;
