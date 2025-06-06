import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { motion, Variants } from 'framer-motion';
import { Text } from '@/components/common/typography/Text';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import { useDeleteUser } from '@/api/hooks/useDeleteUser';

interface MobileNavProps {
  isOpen: boolean;
  onClose: () => void;
}

const navVariants: Variants = {
  open: {
    opacity: 1,
    height: 'auto',
    transition: { staggerChildren: 0.1 },
  },
  closed: {
    opacity: 0,
    height: 0,
    transition: { staggerChildren: 0.05, staggerDirection: -1 },
  },
};

const itemVariants: Variants = {
  open: { opacity: 1, y: 0 },
  closed: { opacity: 0, y: -20 },
};

export default function MobileNav({ isOpen, onClose }: MobileNavProps) {
  const { isAuthenticated, handleLogout } = useAuth();
  const { mutate: deleteUser } = useDeleteUser();
  const location = useLocation();

  const handleDeleteUser = () => {
    if (window.confirm('정말 회원 탈퇴를 하시겠습니까?')) {
      deleteUser(undefined, {
        onSuccess: () => {
          handleLogout();
          alert('회원 탈퇴가 완료되었습니다.');
        },
        onError: (error) => {
          console.error('회원탈퇴 실패:', error);
          alert('회원 탈퇴에 실패했습니다. 다시 시도해주세요.');
        },
      });
    }
  };

  const commonLinks = [
    { to: '/map', label: '지도' },
    { to: '/influencer', label: '인플루언서' },
    { to: '/board', label: '커뮤니티' },
  ];

  return (
    <Container
      as={motion.nav}
      initial="closed"
      animate={isOpen ? 'open' : 'closed'}
      variants={navVariants}
      $isOpen={isOpen}
    >
      <Menu variants={itemVariants}>
        {location.pathname === '/' && (
          <NavItem
            to="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
            target="_blank"
            rel="noopener noreferrer"
            aria-label="모바일 설문조사"
            onClick={onClose}
          >
            <Text size="xs" weight="normal">
              설문조사
            </Text>
          </NavItem>
        )}

        {commonLinks.map((link) => (
          <NavItem key={link.to} to={link.to} aria-label={`모바일 ${link.label}`} onClick={onClose}>
            <Text size="xs" weight="normal">
              {link.label}
            </Text>
          </NavItem>
        ))}

        {isAuthenticated ? (
          <>
            <NavItem to="/my" aria-label="모바일 마이페이지" onClick={onClose}>
              <Text size="xs" weight="normal">
                마이페이지
              </Text>
            </NavItem>
            <LogoutButton
              onClick={() => {
                handleLogout();
                onClose();
              }}
            >
              <Text size="xs" weight="normal">
                로그아웃
              </Text>
            </LogoutButton>
            <LogoutButton
              onClick={() => {
                handleDeleteUser();
                onClose();
              }}
            >
              <Text size="xs" weight="normal">
                회원탈퇴
              </Text>
            </LogoutButton>
          </>
        ) : (
          <LoginModal currentPath={location.pathname}>
            {(openModal: () => void) => (
              <LogoutButton
                onClick={() => {
                  openModal();
                  onClose();
                }}
              >
                <Text size="xs" weight="normal">
                  로그인
                </Text>
              </LogoutButton>
            )}
          </LoginModal>
        )}
      </Menu>
    </Container>
  );
}

const Container = styled(motion.nav)<{ $isOpen: boolean }>`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    position: absolute;
    top: 66px;
    left: 0;
    width: 100%;
    flex-direction: column;
    background-color: ${({ theme }) =>
      theme.backgroundColor === '#292929' ? 'rgba(41, 41, 41, 0.9)' : 'rgba(236, 251, 251, 0.9)'};
    padding: 20px 0;
    gap: 20px;
    z-index: 102;
    pointer-events: ${({ $isOpen }) => ($isOpen ? 'auto' : 'none')};
  }
`;

const Menu = styled(motion.div)`
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  width: 100%;
`;

const NavItem = styled(Link)`
  text-decoration: none;
  cursor: pointer;
  color: inherit;
`;

const LogoutButton = styled.div`
  cursor: pointer;
`;
