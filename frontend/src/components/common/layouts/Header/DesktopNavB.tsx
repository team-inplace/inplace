import { Link, useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';
import { useABTest } from '@/provider/ABTest';
import { sendGAEvent } from '@/utils/test/googleTestUtils';

export default function DesktopNavB() {
  const location = useLocation();
  const navigate = useNavigate();
  const testGroup = useABTest('map_ui_test');

  const isActive = (path: string) => {
    if (path.startsWith('http')) return false;
    return location.pathname.startsWith(path);
  };

  const handleMapNavigation = () => {
    sendGAEvent('map_navigation_click_header', {
      test_name: 'map_ui_test',
      variation: testGroup,
      from_path: location.pathname,
      to_path: '/map',
      component: 'header_nav_b',
    });

    // 페이지 이동
    navigate('/map');
  };

  return (
    <NavLinksContainer>
      <NavExternalLink
        href="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
        target="_blank"
        rel="noopener noreferrer"
        aria-label="헤더 설문조사_B"
      >
        <Text size="m" weight="normal">
          설문조사
        </Text>
      </NavExternalLink>

      <NavButton aria-label="헤더 지도_B" onClick={handleMapNavigation} $isActive={isActive('/map')}>
        <Text size="m" weight="normal">
          지도
        </Text>
      </NavButton>

      <NavItem to="/influencer" aria-label="헤더 인플루언서_B" $isActive={isActive('/influencer')}>
        <Text size="m" weight="normal">
          인플루언서
        </Text>
      </NavItem>

      <NavItem to="/board" aria-label="헤더 커뮤니티_B" $isActive={isActive('/board')}>
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

const NavButton = styled.button<{ $isActive: boolean }>`
  margin-left: 20px;
  background: none;
  border: none;
  padding: 0;
  font: inherit;
  cursor: pointer;
  color: inherit;
  text-align: left;
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
