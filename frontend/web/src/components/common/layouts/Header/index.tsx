import { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { RiMenuLine, RiCloseLine } from 'react-icons/ri';
import { FiSun, FiMoon } from 'react-icons/fi';
import useTheme from '@/hooks/useTheme';

import LogoSection from './LogoSection';
import DesktopNav from './DesktopNav';
import MobileNav from './MobileNav';
import SearchBar from '../../SearchBar';
import AuthButtons from './AuthButtons';
import AlarmButton from './Alarm/AlarmButton';

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
        {!isMenuOpen && (
          <LeftSection>
            <LogoSection />
          </LeftSection>
        )}

        <DesktopNav />

        <RightSection>
          <DesktopOnlySearchBar>
            <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" onClose={() => setIsMenuOpen(false)} />
          </DesktopOnlySearchBar>
          <AuthButtons />
          <MobileOnlyIcons>
            {!isMenuOpen ? (
              <>
                <ThemeButton onClick={toggleTheme} aria-label="모바일 테마 변경" $isDarkMode={isDarkMode}>
                  {isDarkMode ? <FiSun size={20} color="white" /> : <FiMoon size={20} color="black" />}
                </ThemeButton>
                <AlarmButton />
                <MobileMenuButton onClick={() => setIsMenuOpen(true)} aria-label="모바일 메뉴 열기">
                  <RiMenuLine size={24} color={isDarkMode ? 'white' : 'grey'} />
                </MobileMenuButton>
              </>
            ) : (
              <>
                <FixedMarginSearchBar>
                  <SearchBar
                    placeholder="인플루언서, 장소를 검색해주세요!"
                    width="100%"
                    onClose={() => setIsMenuOpen(false)}
                  />
                </FixedMarginSearchBar>
                <MobileMenuButton onClick={() => setIsMenuOpen(false)} aria-label="모바일 메뉴 닫기">
                  <RiCloseLine size={26} color={isDarkMode ? 'white' : 'grey'} />
                </MobileMenuButton>
              </>
            )}
          </MobileOnlyIcons>
        </RightSection>
        <MobileNav isOpen={isMenuOpen} onClose={() => setIsMenuOpen(false)} />
      </HeaderContentWrapper>
    </HeaderContainer>
  );
}

export const HEADER_HEIGHT = 50;

const HeaderContainer = styled.header`
  width: 960px;
  margin: 0 auto;
`;

const HeaderContentWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  min-height: 60px;
  margin-top: 10px;

  @media screen and (max-width: 768px) {
    position: relative;
    padding: 0 20px;
    width: 90%;
    position: fixed;
    max-height: 50px;
    top: 0;
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#f7fcff')};
    margin-top: 0px;
    z-index: 10000;
  }
`;

const LeftSection = styled.div`
  margin-right: 24px;
  display: flex;
  align-items: center;
`;

const RightSection = styled.div`
  display: flex;
  align-items: center;
  margin-left: auto;
  gap: 26px;
`;

const DesktopOnlySearchBar = styled.div`
  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const FixedMarginSearchBar = styled.div`
  position: absolute;
  left: 20px;
  width: calc(100% - 80px);
`;
const MobileOnlyIcons = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    align-items: center;
    gap: 12px;
  }
`;

const MobileMenuButton = styled.button`
  display: flex;
  align-items: center;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
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
  @media screen and (max-width: 768px) {
    margin-right: 4px;
  }
`;
