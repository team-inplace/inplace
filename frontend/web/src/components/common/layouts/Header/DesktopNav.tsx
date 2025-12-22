import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';

export default function DesktopNav() {
  const location = useLocation();

  const isActive = (path: string) => {
    if (path.startsWith('http')) return false;
    return location.pathname === path;
  };

  return (
    <NavLinksContainer>
      <NavExternalLink
        href="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
        target="_blank"
        rel="noopener noreferrer"
        aria-label="헤더 설문조사"
      >
        <Text size="m" weight="normal">
          설문조사
        </Text>
      </NavExternalLink>

      <NavItem to="/map" aria-label="헤더 지도" $isActive={isActive('/map')}>
        <Text size="m" weight="normal">
          지도
        </Text>
      </NavItem>

      <NavItem to="/influencer" aria-label="헤더 인플루언서" $isActive={isActive('/influencer')}>
        <Text size="m" weight="normal">
          인플루언서
        </Text>
      </NavItem>

      <NavItem to="/post" aria-label="헤더 커뮤니티" $isActive={isActive('/post')}>
        <Text size="m" weight="normal">
          커뮤니티
        </Text>
      </NavItem>
    </NavLinksContainer>
  );
}

const NavLinksContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
  white-space: nowrap;

  @media (max-width: 768px) {
    display: none;
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
    bottom: -6px;
    width: 100%;
    height: 3px;
    background-color: #47c8d9;
    transform: ${({ $isActive }) => ($isActive ? 'scaleX(1)' : 'scaleX(0)')};
    transition: transform 0.3s ease;
  }

  &:hover::after {
    transform: scaleX(1);
  }
`;

const NavExternalLink = styled.a`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
  position: relative;

  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: -6px;
    width: 100%;
    height: 3px;
    background-color: #47c8d9;
    transform: scaleX(0);
    transition: transform 0.3s ease;
  }

  &:hover::after {
    transform: scaleX(1);
  }
`;
