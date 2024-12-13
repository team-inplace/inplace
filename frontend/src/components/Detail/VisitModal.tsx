import { FcInfo } from 'react-icons/fc';

import styled from 'styled-components';

import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import Button from '@/components/common/Button';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { useGetSendInfo } from '@/api/hooks/useGetSendInfo';
import useAuth from '@/hooks/useAuth';
import LoginModal from '../common/modals/LoginModal';

export default function VisitModal({ id, placeName, onClose }: { id: number; placeName: string; onClose: () => void }) {
  const isAuthenticated = useAuth();
  const location = useLocation();
  const [isSend, setIsSend] = useState(false);
  const { refetch } = useGetSendInfo(String(id), false);
  const [message, setMessage] = useState<string>('');
  const [showLoginModal, setShowLoginModal] = useState(false);

  const handleModalClick = (event: React.MouseEvent<HTMLDivElement>) => {
    event.stopPropagation();
  };
  const handleSendInfo = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    if (!isAuthenticated) {
      setMessage('로그인이 필요합니다.');
      return;
    }
    setIsSend(true);
    try {
      const { status } = await refetch();
      if (status === 'success' && isSend) {
        setMessage('카카오톡을 확인해주세요!');
      } else {
        setMessage('에러가 발생했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      setMessage('에러가 발생했습니다. 관리자에게 문의하세요.');
    } finally {
      setIsSend(false);
    }
  };
  return (
    <>
      <Overlay onClick={() => onClose()}>
        <Wrapper onClick={handleModalClick}>
          <DescriptionSection>
            <FcInfo size={180} />
            <Paragraph size="l" weight="normal">
              {message || `${placeName}에 대한 정보를\n 카카오톡으로 보내드릴까요?`}
            </Paragraph>
          </DescriptionSection>
          <BtnContainer $hasMessage={!!message}>
            {message ? (
              <Button
                variant="kakao"
                style={{ fontWeight: 'bold', width: '170px', height: '46px', fontSize: '18px' }}
                onClick={() => onClose()}
              >
                완료
              </Button>
            ) : (
              <>
                <Button
                  variant="blackOutline"
                  style={{ fontWeight: 'bold', width: '170px', height: '46px', fontSize: '18px' }}
                  onClick={() => onClose()}
                >
                  취소
                </Button>
                <Button
                  variant="kakao"
                  style={{ fontWeight: 'bold', width: '170px', height: '46px', fontSize: '18px' }}
                  onClick={handleSendInfo}
                >
                  확인
                </Button>
              </>
            )}
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
  width: 500px;
  height: 600px;
  border-radius: 8px;
  background-color: white;

  display: flex;
  flex-direction: column;
  text-align: center;
  align-items: center;
  gap: 60px;
`;
const DescriptionSection = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  margin-top: 20%;
  p {
    line-height: 180%;
    white-space: pre-line;
  }
`;
const BtnContainer = styled.div<{ $hasMessage: boolean }>`
  display: flex;
  justify-content: ${({ $hasMessage }) => ($hasMessage ? 'center' : 'space-between')};
  width: 382px;
`;
