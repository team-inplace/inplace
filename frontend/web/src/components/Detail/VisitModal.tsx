import styled from 'styled-components';

import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
// import { useQueryClient } from '@tanstack/react-query';
import Button from '@/components/common/Button';
import { Paragraph } from '@/components/common/typography/Paragraph';
import LoginModal from '../common/modals/LoginModal';
import { useGetMobileMapQR } from '@/api/hooks/useGetMobileMapQR';

export default function VisitModal({ id, onClose }: { id: number; onClose: () => void }) {
  const location = useLocation();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const { data: blobData } = useGetMobileMapQR(id, 200, 200);
  const [qrSrc, setQrSrc] = useState<string | null>(null);

  useEffect(() => {
    if (!blobData) return;

    if (qrSrc) {
      URL.revokeObjectURL(qrSrc);
    }

    const imageUrl = URL.createObjectURL(blobData);
    setQrSrc(imageUrl);
  }, [blobData]);

  useEffect(() => {
    const checkMobile = () => {
      if (window.innerWidth <= 768) onClose();
    };
    checkMobile();
    window.addEventListener('resize', checkMobile);

    return () => window.removeEventListener('resize', checkMobile);
  }, [window.innerWidth]);

  const handleModalClick = (event: React.MouseEvent<HTMLDivElement>) => {
    event.stopPropagation();
  };
  // const buttonStyle = {
  //   fontWeight: 'bold',
  //   width: isMobile ? '46%' : '170px',
  //   height: isMobile ? '40px' : '46px',
  //   fontSize: isMobile ? '16px' : '18px',
  // };
  return (
    <>
      <Overlay onClick={() => onClose()}>
        <Wrapper onClick={handleModalClick}>
          <DescriptionSection>
            <Paragraph size="l" weight="bold" variant="#333333">
              Scan QR Code
            </Paragraph>
            <Paragraph size="xs" weight="normal" variant="#333333">
              QR코드를 스캔하면 카카오맵으로 이동해요!
            </Paragraph>
          </DescriptionSection>
          <ImageFrame>
            <span />
            {qrSrc && <Image src={qrSrc} alt="QR CODE" />}
          </ImageFrame>
          <BtnContainer>
            <Button aria-label="큐알코드 닫기" variant="mint" size="small" onClick={() => onClose()}>
              닫기
            </Button>
          </BtnContainer>
        </Wrapper>
      </Overlay>
      {showLoginModal && <LoginModal currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />}
    </>
  );
}
const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
`;
const Wrapper = styled.div`
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  width: 400px;
  border-radius: 8px;
  background-color: white;
  padding: 60px 0px;
  border: 30px solid #e8f9ff;

  display: flex;
  flex-direction: column;
  text-align: center;
  align-items: center;
  gap: 80px;
`;
const Image = styled.img`
  width: 200px;
  height: 200px;
`;
const ImageFrame = styled.div`
  position: relative;
  &::before,
  &::after,
  & > span::before,
  & > span::after {
    content: '';
    position: absolute;
    width: 1.5rem;
    height: 1.5rem;
  }

  &::before {
    top: -10%;
    left: -10%;
    border-left: 4px solid #55ebff;
    border-top: 4px solid #55ebff;
  }

  &::after {
    top: -10%;
    right: -10%;
    border-right: 4px solid #55ebff;
    border-top: 4px solid #55ebff;
  }

  & > span::before {
    bottom: -10%;
    left: -10%;
    border-left: 4px solid #55ebff;
    border-bottom: 4px solid #55ebff;
  }

  & > span::after {
    bottom: -10%;
    right: -10%;
    border-right: 4px solid #55ebff;
    border-bottom: 4px solid #55ebff;
  }
`;

const DescriptionSection = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
`;
const BtnContainer = styled.div`
  display: flex;
  justify-content: center;
  width: 40%;
`;
