import { useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { FiSun, FiMoon } from 'react-icons/fi';
import useAuth from '@/hooks/useAuth';
import useTheme from '@/hooks/useTheme';
import LoginModal from '@/components/common/modals/LoginModal';
import { Text } from '../../typography/Text';

export default function AuthButtons() {
  const { isAuthenticated, handleLogout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const isDarkMode = theme === 'dark';
  const location = useLocation();

  return (
    <Container>
      {isAuthenticated ? (
        <IconButton onClick={handleLogout}>
          <Text size="xs" weight="normal">
            로그아웃
          </Text>
        </IconButton>
      ) : (
        <LoginModal currentPath={location.pathname}>
          {(openModal: () => void) => (
            <IconButton onClick={openModal}>
              <Text size="xs" weight="normal">
                로그인
              </Text>
            </IconButton>
          )}
        </LoginModal>
      )}
      <ThemeButton onClick={toggleTheme} aria-label="테마 변경 버튼_A" $isDarkMode={isDarkMode}>
        {isDarkMode ? <FiSun size={20} color="white" /> : <FiMoon size={20} color="black" />}
      </ThemeButton>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
  margin-left: 20px;

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
