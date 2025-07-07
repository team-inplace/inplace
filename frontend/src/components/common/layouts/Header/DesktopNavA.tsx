import { Link, useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';
import { useABTest } from '@/provider/ABTest';
import { sendGAEvent } from '@/utils/test/googleTestUtils';

export default function DesktopNavA() {
  const location = useLocation();
  const navigate = useNavigate();
  const testGroup = useABTest('map_ui_test');

  const isActive = (path: string) => {
    if (path.startsWith('http')) return false;
    return location.pathname === path;
  };

  const handleMapNavigation = () => {
    sendGAEvent('map_navigation_click_header', {
      test_name: 'map_ui_test',
      variation: testGroup,
      from_path: location.pathname,
      to_path: '/map',
      component: 'header_nav_a',
    });

    // 페이지 이동
    navigate('/map');
  };

  return (
    <NavLinksContainer>
      <NavExternalLink
        aria-label="헤더 설문조사_A"
        href="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
        target="_blank"
        rel="noopener noreferrer"
      >
        <Text size="xs" weight="normal">
          설문조사
        </Text>
      </NavExternalLink>

      <NavButton aria-label="헤더 지도_A" onClick={handleMapNavigation} $isActive={isActive('/map')}>
        <Text size="xs" weight="normal">
          지도
        </Text>
      </NavButton>

      <NavItem aria-label="헤더 인플루언서_A" to="/influencer" $isActive={isActive('/influencer')}>
        <Text size="xs" weight="normal">
          인플루언서
        </Text>
      </NavItem>

      <NavItem to="/post" aria-label="헤더 커뮤니티_A" $isActive={isActive('/post')}>
        <Text size="xs" weight="normal">
          커뮤니티
        </Text>
      </NavItem>
    </NavLinksContainer>
  );
}

const NavLinksContainer = styled.div`
  display: flex;
  align-items: center;
  margin-left: auto;

  @media (max-width: 768px) {
    display: none;
  }
`;

const NavItem = styled(Link)<{ $isActive: boolean }>`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
`;

const NavExternalLink = styled.a`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
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
`;
