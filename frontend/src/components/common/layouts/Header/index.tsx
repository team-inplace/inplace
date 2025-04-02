import { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { RiMenuLine, RiCloseLine } from 'react-icons/ri';
import { FiSun, FiMoon } from 'react-icons/fi';
import useTheme from '@/hooks/useTheme';

import LogoSection from './LogoSection';
import DesktopNav from './DesktopNav';
import AuthButtons from './AuthButtons';
import MobileNav from './MobileNav';
import SearchBar from '../../SearchBar';

export default function Header() {
  const { theme, toggleTheme } = useTheme();
  const isDarkMode = theme === 'dark';
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const headerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleOutsideClick = (e: MouseEvent) => {
      if (headerRef.current && !headerRef.current.contains(e.target as Node)) {
        setIsMenuOpen(false);
      }
    };
    document.addEventListener('mousedown', handleOutsideClick);
    return () => document.removeEventListener('mousedown', handleOutsideClick);
  }, []);

  return (
    <HeaderContainer ref={headerRef}>
      <HeaderContentWrapper>
        <LeftSection>
          <LogoSection />
        </LeftSection>

        <DesktopNav />

        <RightSection>
          <DesktopOnlySearchBar>
            <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" />
          </DesktopOnlySearchBar>

          <MobileOnlyIcons>
            {!isMenuOpen ? (
              <>
                <MobileSearchBar>
                  <SearchBar placeholder="검색하기" />
                </MobileSearchBar>
                <MobileMenuButton onClick={() => setIsMenuOpen(true)} aria-label="메뉴 열기">
                  <RiMenuLine size={24} color={isDarkMode ? 'white' : 'grey'} />
                </MobileMenuButton>
              </>
            ) : (
              <>
                <ThemeButton onClick={toggleTheme} aria-label="테마 변경 버튼">
                  {isDarkMode ? <FiSun size={20} color="white" /> : <FiMoon size={20} color="black" />}
                </ThemeButton>
                <MobileMenuButton onClick={() => setIsMenuOpen(false)} aria-label="메뉴 닫기">
                  <RiCloseLine size={24} color={isDarkMode ? 'white' : 'grey'} />
                </MobileMenuButton>
              </>
            )}
          </MobileOnlyIcons>
          <AuthButtons />
        </RightSection>
      </HeaderContentWrapper>

      <MobileNav isOpen={isMenuOpen} onClose={() => setIsMenuOpen(false)} />
    </HeaderContainer>
  );
}

export const HEADER_HEIGHT = 80;

const HeaderContainer = styled.header``;

const HeaderContentWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  min-height: 60px;
  margin-top: 10px;

  @media (max-width: 768px) {
    padding: 0 20px;
  }
`;

const LeftSection = styled.div`
  display: flex;
  align-items: center;
`;

const RightSection = styled.div`
  display: flex;
  align-items: center;
  gap: 26px;
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
const MobileOnlyIcons = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    align-items: center;
    gap: 4px;
  }
`;

const MobileMenuButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
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
