import { useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { FaLock, FaLockOpen } from 'react-icons/fa6';
import { FiSun, FiMoon } from 'react-icons/fi';
import useAuth from '@/hooks/useAuth';
import useTheme from '@/hooks/useTheme';
import LoginModal from '@/components/common/modals/LoginModal';

export default function AuthButtons() {
  const { isAuthenticated, handleLogout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const isDarkMode = theme === 'dark';
  const location = useLocation();

  return (
    <Container>
      {isAuthenticated ? (
        <IconButton onClick={handleLogout}>
          <FaLockOpen size={22} />
        </IconButton>
      ) : (
        <LoginModal currentPath={location.pathname}>
          {(openModal: () => void) => (
            <IconButton onClick={openModal}>
              <FaLock size={22} />
            </IconButton>
          )}
        </LoginModal>
      )}
      <ThemeButton onClick={toggleTheme} aria-label="테마 변경 버튼">
        {isDarkMode ? <FiSun size={22} color="white" /> : <FiMoon size={22} color="black" />}
      </ThemeButton>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 30px;

  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const IconButton = styled.div`
  cursor: pointer;
`;

const ThemeButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;

  &:hover {
    transform: rotate(30deg);
  }
`;
