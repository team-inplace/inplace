import { useState, useEffect, useRef } from 'react';
import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { RiMenuLine, RiCloseLine } from 'react-icons/ri';
import { FaLock, FaLockOpen } from 'react-icons/fa6';
import { FiSun, FiMoon } from 'react-icons/fi';
import { motion, Variants } from 'framer-motion';
import LoginModal from '@/components/common/modals/LoginModal';
import { Text } from '@/components/common/typography/Text';
import Logo from '@/assets/images/Logo.svg';
import useAuth from '@/hooks/useAuth';
import useTheme from '@/hooks/useTheme';
import SearchBar from '../SearchBar';

const navVariants: Variants = {
  open: {
    opacity: 1,
    height: 'auto',
    transition: {
      staggerChildren: 0.1,
    },
  },
  closed: {
    opacity: 0,
    height: 0,
    transition: {
      staggerChildren: 0.05,
      staggerDirection: -1,
    },
  },
};

const itemVariants: Variants = {
  open: { opacity: 1, y: 0 },
  closed: { opacity: 0, y: -20 },
};

export default function Header() {
  const { isAuthenticated, handleLogout } = useAuth();
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const headerRef = useRef<HTMLDivElement>(null);
  const { theme, toggleTheme } = useTheme();
  const isDarkMode = theme === 'dark';

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (headerRef.current && !headerRef.current.contains(e.target as Node)) {
        setIsMenuOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const isActive = (path: string) => {
    if (path.startsWith('http')) return false;
    return location.pathname === path;
  };

  return (
    <HeaderContainer ref={headerRef}>
      <HeaderContentWrapper>
        <LeftSection>
          <LogoLink to="/">
            <LogoContainer>
              <LogoImage src={Logo} alt="인플레이스 로고" />
              <Text size="l" weight="bold" variant="mint">
                인플레이스
              </Text>
            </LogoContainer>
          </LogoLink>
        </LeftSection>
        <NavLinksContainer>
          <NavItem
            to="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
            target="_blank"
            rel="noopener noreferrer"
            $isActive={false}
          >
            <Text size="s" weight="normal">
              설문조사
            </Text>
          </NavItem>
          <NavItem to="/map" $isActive={isActive('/map')}>
            <Text size="s" weight="normal">
              지도
            </Text>
          </NavItem>
          <NavItem to="/influencer" $isActive={isActive('/influencer')}>
            <Text size="s" weight="normal">
              인플루언서
            </Text>
          </NavItem>
          <NavItem
            to="/my"
            style={{
              visibility: isAuthenticated ? 'visible' : 'hidden',
              pointerEvents: isAuthenticated ? 'auto' : 'none',
            }}
            $isActive={isActive('/my')}
          >
            <Text size="s" weight="normal">
              마이페이지
            </Text>
          </NavItem>
        </NavLinksContainer>
        <RightSection>
          <DesktopOnlySearchBar>
            <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" />
          </DesktopOnlySearchBar>
          <MobileOnlyIcons>
            {!isMenuOpen && (
              <>
                <MobileSearchBar>
                  <SearchBar placeholder="검색하기" />
                </MobileSearchBar>
                <MobileMenuButton aria-label="메뉴 열기" onClick={() => setIsMenuOpen(true)}>
                  <RiMenuLine size={24} color={isDarkMode ? 'white' : 'grey'} />
                </MobileMenuButton>
              </>
            )}
            {isMenuOpen && (
              <>
                <ThemeButton aria-label="테마 변경 버튼" onClick={toggleTheme}>
                  {isDarkMode ? <FiSun size={20} color="white" /> : <FiMoon size={20} color="black" />}
                </ThemeButton>
                <MobileMenuButton aria-label="메뉴 닫기" onClick={() => setIsMenuOpen(false)}>
                  <RiCloseLine size={24} color={isDarkMode ? 'white' : 'grey'} />
                </MobileMenuButton>
              </>
            )}
          </MobileOnlyIcons>
          <DesktopOnlyButtons>
            {isAuthenticated ? (
              <LoginButton onClick={handleLogout}>
                <FaLockOpen size={20} />
              </LoginButton>
            ) : (
              <LoginModal currentPath={location.pathname}>
                {(openModal: () => void) => (
                  <LoginButton onClick={openModal}>
                    <FaLock size={20} />
                  </LoginButton>
                )}
              </LoginModal>
            )}
            <ThemeButton aria-label="테마 변경 버튼" onClick={toggleTheme}>
              {isDarkMode ? <FiSun size={20} color="white" /> : <FiMoon size={20} color="black" />}
            </ThemeButton>
          </DesktopOnlyButtons>
        </RightSection>
      </HeaderContentWrapper>
      <MobileNav
        as={motion.nav}
        initial="closed"
        animate={isMenuOpen ? 'open' : 'closed'}
        variants={navVariants}
        $isOpen={isMenuOpen}
      >
        <MenuContainer variants={itemVariants}>
          {isAuthenticated ? (
            <>
              {location.pathname === '/' && (
                <MobileNavItem
                  to="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
                  target="_blank"
                  rel="noopener noreferrer"
                  onClick={() => setIsMenuOpen(false)}
                >
                  <Text size="xs" weight="normal">
                    설문조사
                  </Text>
                </MobileNavItem>
              )}
              <MobileNavItem to="/map" onClick={() => setIsMenuOpen(false)}>
                <Text size="xs" weight="normal">
                  지도
                </Text>
              </MobileNavItem>
              <MobileNavItem to="/influencer" onClick={() => setIsMenuOpen(false)}>
                <Text size="xs" weight="normal">
                  인플루언서
                </Text>
              </MobileNavItem>
              <MobileNavItem to="/my" onClick={() => setIsMenuOpen(false)}>
                <Text size="xs" weight="normal">
                  마이페이지
                </Text>
              </MobileNavItem>
              <MobileLoginButton
                onClick={() => {
                  handleLogout();
                  setIsMenuOpen(false);
                }}
              >
                <Text size="xs" weight="normal">
                  로그아웃
                </Text>
              </MobileLoginButton>
            </>
          ) : (
            <>
              {location.pathname === '/' && (
                <MobileNavItem
                  to="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
                  target="_blank"
                  rel="noopener noreferrer"
                  onClick={() => setIsMenuOpen(false)}
                >
                  <Text size="xs" weight="normal">
                    설문조사
                  </Text>
                </MobileNavItem>
              )}
              <MobileNavItem to="/map" onClick={() => setIsMenuOpen(false)}>
                <Text size="xs" weight="normal">
                  지도
                </Text>
              </MobileNavItem>
              <MobileNavItem to="/influencer" onClick={() => setIsMenuOpen(false)}>
                <Text size="xs" weight="normal">
                  인플루언서
                </Text>
              </MobileNavItem>
              <LoginModal currentPath={location.pathname}>
                {(openModal: () => void) => (
                  <MobileLoginButton
                    onClick={() => {
                      openModal();
                      setIsMenuOpen(false);
                    }}
                  >
                    <Text size="xs" weight="normal">
                      로그인
                    </Text>
                  </MobileLoginButton>
                )}
              </LoginModal>
            </>
          )}
        </MenuContainer>
      </MobileNav>
    </HeaderContainer>
  );
}

export const HEADER_HEIGHT = 80;

const HeaderContainer = styled.header``;

const HeaderContentWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  min-height: 80px;

  @media (max-width: 768px) {
    padding: 0 20px;
  }
`;

const LeftSection = styled.div`
  display: flex;
  align-items: center;
`;

const NavLinksContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;

  @media (max-width: 768px) {
    display: none;
  }
`;

const RightSection = styled.div`
  display: flex;
  align-items: center;
  gap: 26px;
`;

const LogoLink = styled(Link)`
  text-decoration: none;
  display: flex;
  align-items: center;
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
`;

const LogoImage = styled.img`
  height: 22px;
  width: 22px;
  margin-right: 10px;
`;

const DesktopOnlySearchBar = styled.div`
  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const MobileSearchBar = styled.div`
  width: 140px;
  & form {
    right: 30%;
  }
`;

const DesktopOnlyButtons = styled.div`
  display: flex;
  align-items: center;
  gap: 30px;

  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const MobileOnlyIcons = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    align-items: center;
    gap: 4px;
  }
`;

const MobileNav = styled(motion.nav)<{ $isOpen: boolean }>`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    position: absolute;
    top: 52px;
    left: 0;
    width: 100%;
    flex-direction: column;
    background-color: ${({ theme }) =>
      theme.backgroundColor === '#292929' ? 'rgba(41, 41, 41, 0.9)' : 'rgba(236, 251, 251, 0.9)'};
    padding: 20px 0;
    gap: 20px;
    z-index: 10;
    pointer-events: ${({ $isOpen }) => ($isOpen ? 'auto' : 'none')};
  }
`;

const NavItem = styled(Link)<{ $isActive: boolean }>`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
  position: relative;

  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: -5px;
    width: 100%;
    height: 3px;
    background-color: #55ebff;
    transform: ${({ $isActive }) => ($isActive ? 'scaleX(1)' : 'scaleX(0)')};
    transition: transform 0.3s ease;
  }
`;

const MobileNavItem = styled(Link)`
  text-decoration: none;
  cursor: pointer;
  color: inherit;
`;

const LoginButton = styled.div`
  cursor: pointer;
`;

const MobileLoginButton = styled.div`
  cursor: pointer;
`;

const MobileMenuButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
`;

const MenuContainer = styled(motion.div)`
  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    gap: 20px;
    align-items: center;
    width: 100%;
  }
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

  @media screen and (max-width: 768px) {
    margin-right: 20px;
  }
`;
