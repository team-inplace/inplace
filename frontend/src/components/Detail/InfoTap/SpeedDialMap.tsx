import { useContext, useState } from 'react';
import styled from 'styled-components';
import { FaMapMarkedAlt } from 'react-icons/fa';
import { FcGoogle } from 'react-icons/fc';
import { IoClose, IoQrCode } from 'react-icons/io5';
import Button from '@/components/common/Button';
import { ThemeContext } from '@/provider/Themes';
import KakaoIcon from '@/assets/images/kakaomap-icon.webp';
import NaverIcon from '@/assets/images/navermap-icon.webp';
import useIsMobile from '@/hooks/useIsMobile';

interface SpeedDialMapProps {
  kakaoPlaceUrl: string;
  naverPlaceUrl: string;
  googlePlaceUrl?: string;
  visitModal: boolean;
  setVisitModal: (visitModal: boolean) => void;
}

export default function SpeedDialMap({
  kakaoPlaceUrl,
  googlePlaceUrl,
  naverPlaceUrl,
  visitModal,
  setVisitModal,
}: SpeedDialMapProps) {
  const [isOpen, setIsOpen] = useState(true);
  const { theme } = useContext(ThemeContext);
  const buttonVariant = theme === 'dark' ? 'outline' : 'blackOutline';
  const isMobile = useIsMobile();

  const toggleSpeedDial = () => {
    setIsOpen(!isOpen);
  };

  return (
    <SpeedDialContainer>
      <SpeedDialItems isOpen={isOpen}>
        <KakaoButton
          aria-label="kakao_btn"
          variant="white"
          onClick={() => {
            window.location.href = kakaoPlaceUrl;
          }}
          data-tooltip="카카오맵"
        >
          <img src={KakaoIcon} alt="카카오맵" width="20" height="20" />
        </KakaoButton>

        <SpeedDialItem
          aria-label="naver_btn"
          variant="white"
          onClick={() => {
            window.location.href = naverPlaceUrl;
          }}
          data-tooltip="네이버맵"
        >
          <img src={NaverIcon} alt="네이버맵" width="20" height="20" />
        </SpeedDialItem>

        {googlePlaceUrl && (
          <GoogleButton
            aria-label="google_btn"
            variant="white"
            onClick={() => {
              window.location.href = googlePlaceUrl;
            }}
            data-tooltip="구글맵"
          >
            <FcGoogle size={20} />
          </GoogleButton>
        )}
        {!isMobile ? (
          <SpeedDialItem
            aria-label="mobile_qr_btn"
            variant="white"
            onClick={() => setVisitModal(!visitModal)}
            data-tooltip="모바일로 연결"
          >
            <IoQrCode size={18} color="black" />
          </SpeedDialItem>
        ) : null}
      </SpeedDialItems>

      <MainButton aria-label="toggle_map_options" variant={buttonVariant} onClick={toggleSpeedDial}>
        {isOpen ? <IoClose size={30} /> : <FaMapMarkedAlt size={24} />}
      </MainButton>
    </SpeedDialContainer>
  );
}

const SpeedDialContainer = styled.div`
  position: fixed;
  top: 328px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 56px;
  height: 56px;
  z-index: 2;

  @media screen and (max-width: 768px) {
    top: auto;
    bottom: 60px;
  }
`;

const MainButton = styled(Button)`
  position: absolute;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
  padding: 0;

  &[variant='blackOutline'] {
    background-color: #ecfbfb;
  }
  &[variant='outline'] {
    background-color: #292929;
  }

  @media screen and (max-width: 768px) {
    width: 48px;
    height: 48px;

    svg {
      width: 20px;
      height: 20px;
    }
  }
`;

const SpeedDialItems = styled.div<{ isOpen: boolean }>`
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s ease;

  & > * {
    position: absolute;
    transition: transform 0.3s ease;
    opacity: ${(props) => (props.isOpen ? 1 : 0)};
    pointer-events: ${(props) => (props.isOpen ? 'auto' : 'none')};
  }

  & > *:nth-child(1) {
    transform: ${(props) => (props.isOpen ? 'translateY(-190%)' : 'translateY(0)')};
  }

  & > *:nth-child(2) {
    transform: ${(props) => (props.isOpen ? 'translateY(-320%)' : 'translateY(0)')};
  }

  & > *:nth-child(3) {
    transform: ${(props) => (props.isOpen ? 'translateY(-450%)' : 'translateY(0)')};
  }

  & > *:nth-child(4) {
    transform: ${(props) => (props.isOpen ? 'translateY(-580%)' : 'translateY(0)')};
  }
`;

const SpeedDialItem = styled(Button)`
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);

  &:hover {
    background-color: white !important;
  }
  &::after {
    content: attr(data-tooltip);
    position: absolute;
    left: calc(100% + 8px);
    top: 50%;
    transform: translateY(-50%);
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    white-space: nowrap;
    opacity: 0;
    visibility: hidden;
    transition: all 0.2s ease;
    pointer-events: none;
  }
  &:hover::after {
    opacity: 1;
    visibility: visible;
  }

  @media screen and (max-width: 768px) {
    width: 40px;
    height: 40px;

    svg {
      width: 14px;
      height: 14px;
    }
  }

  @media screen and (min-width: 769px) {
    &::after {
      opacity: 1;
      visibility: visible;
    }
  }
`;

const KakaoButton = styled(SpeedDialItem)`
  @media screen and (max-width: 768px) {
    svg {
      width: 18px;
      height: 18px;
    }
  }
`;

const GoogleButton = styled(SpeedDialItem)`
  @media screen and (max-width: 768px) {
    svg {
      width: 18px;
      height: 18px;
    }
  }
`;
