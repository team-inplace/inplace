import { useEffect, useState } from 'react';
import * as ReactDOM from 'react-dom';
import { FaComment } from 'react-icons/fa';
import styled from 'styled-components';

import { BASE_URL } from '@inplace-frontend-monorepo/shared/api/instance';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';
import Logo from '@/assets/images/InplaceLogo.png';

type LoginModalProps = {
  children?: (openModal: () => void) => React.ReactNode;
  currentPath: string;
  immediateOpen?: boolean;
  onClose?: () => void;
  onLoginSuccess?: () => void;
};

const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

export default function LoginModal({
  children,
  currentPath,
  immediateOpen = false,
  onClose,
  onLoginSuccess,
}: LoginModalProps) {
  const [isOpen, setIsOpen] = useState(immediateOpen);

  useEffect(() => {
    if (immediateOpen) {
      setIsOpen(true);
    }
  }, [immediateOpen]);

  const openModal = () => {
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
    if (onClose) {
      onClose();
    }
    if (onLoginSuccess) {
      onLoginSuccess();
    }
  };

  const handleKakaoLogin = () => {
    localStorage.setItem('redirectPath', currentPath);
    if (isReactNativeWebView) {
      window.ReactNativeWebView?.postMessage(JSON.stringify({ type: 'requestKakaoLogin' }));
    } else {
      window.location.href = `${BASE_URL}/oauth2/authorization/kakao`;
    }
  };

  return (
    <>
      {children && children(openModal)}
      {isOpen &&
        ReactDOM.createPortal(
          <ModalOverlay>
            <ModalContainer>
              <CloseButton aria-label="로그인 창닫기" onClick={closeModal}>
                X
              </CloseButton>
              <TitleWrapper>
                <IconBackground>
                  <LogoImage src={Logo} alt="인플레이스 로고" />
                  <Paragraph size="xl" weight="bold" variant="black">
                    인플레이스
                  </Paragraph>
                </IconBackground>
              </TitleWrapper>
              <KakaoLoginButton aria-label="카카오 로그인" onClick={handleKakaoLogin}>
                <FaComment />
                <Text size="s" weight="normal">
                  카카오 로그인
                </Text>
              </KakaoLoginButton>
            </ModalContainer>
          </ModalOverlay>,
          document.body,
        )}
    </>
  );
}

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
`;

const ModalContainer = styled.div`
  position: relative;
  width: 500px;
  height: 600px;
  background-color: #e8f9ff;
  border-radius: 8px;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 70px;

  @media screen and (max-width: 768px) {
    width: 90%;
    height: 470px;
    gap: 50px;
  }
`;

const CloseButton = styled.button`
  position: absolute;
  right: 4%;
  top: 4%;
  font-weight: 700;
  font-size: 32px;
  color: #4e4e4e;
  background: none;
  border: none;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    font-size: 28px;
  }
`;

const IconBackground = styled.div`
  background-color: white;
  border-radius: 50%;
  padding: 16px;
  width: 280px;
  height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  @media screen and (max-width: 768px) {
    padding: 12px;
    width: 210px;
    height: 180px;
  }
`;

const LogoImage = styled.img`
  height: 130px;
  width: 130px;
  margin-bottom: 30px;

  @media screen and (max-width: 768px) {
    height: 98px;
    width: 98px;
    margin-bottom: 23px;
  }
`;

const TitleWrapper = styled.div`
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 18px;
`;

const KakaoLoginButton = styled.button`
  width: 80%;
  height: 60px;
  background: #fee500;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 20px;

  svg {
    width: 22px;
    height: 22px;
    margin-right: 8px;
    flex-shrink: 0;
  }

  span {
    flex-grow: 1;
    text-align: center;
  }

  @media screen and (max-width: 768px) {
    color: black;
    height: 46px;

    svg {
      width: 18px;
      height: 18px;
    }
  }
`;
