import { Link, useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';
import { useABTest } from '@/provider/ABTest';
import { sendGAEvent } from '@/utils/test/googleTestUtils';

export default function DesktopNavB() {
  const location = useLocation();
  const navigate = useNavigate();
  const testGroup = useABTest('map_ui_test');

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

      <NavButton aria-label="헤더 지도_B" onClick={handleMapNavigation}>
        <Text size="m" weight="normal">
          지도
        </Text>
      </NavButton>

      <NavItem to="/influencer" aria-label="헤더 인플루언서_B">
        <Text size="m" weight="normal">
          인플루언서
        </Text>
      </NavItem>

      <NavItem to="/post" aria-label="헤더 커뮤니티_B">
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

const NavItem = styled(Link)`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
  position: relative;
`;

const NavExternalLink = styled.a`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
  position: relative;
`;

const NavButton = styled.button`
  margin-left: 20px;
  background: none;
  border: none;
  padding: 0;
  font: inherit;
  cursor: pointer;
  color: inherit;
  text-align: left;
  position: relative;
`;
