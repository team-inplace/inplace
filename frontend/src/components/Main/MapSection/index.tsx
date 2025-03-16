import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { FaChevronRight } from 'react-icons/fa6';
import VisitPlace from '@/assets/images/visit-place.png';
import { Paragraph } from '@/components/common/typography/Paragraph';

export default function MapSection() {
  const navigate = useNavigate();
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const handleConfirmClick = () => {
    navigate('/map');
  };

  return (
    <Container>
      <Wrapper>
        <TextSection>
          <Paragraph size={isMobile ? 's' : 'm'} weight="normal">
            <span
              style={{
                lineHeight: isMobile ? '1.4' : 'normal',
              }}
            >
              {isMobile ? (
                <>
                  내 주변 인플루언서가 방문한
                  <br />
                  장소는 어디일까요?
                </>
              ) : (
                '내 주변 인플루언서가 방문한 장소는 어디일까요?'
              )}
            </span>
          </Paragraph>
          <IconContainer>
            <img src={VisitPlace} alt="방문 장소" width={isMobile ? '36px' : '44px'} />
          </IconContainer>
        </TextSection>
        <ConfirmButton onClick={handleConfirmClick}>
          {isMobile ? '확인하기' : '확인하러 가기'}
          <FaChevronRight />
        </ConfirmButton>
      </Wrapper>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#2f2f2f' : '#d8f4f4')};
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#797979')};
  border-radius: 10px;
  padding: 0 42px;

  @media screen and (max-width: 768px) {
    padding: 0 32px;
    width: 90%;
  }
`;

const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 20px;

  @media screen and (max-width: 768px) {
    padding: 16px;
    gap: 16px;
  }
`;

const TextSection = styled.div`
  display: flex;
  align-items: center;
  gap: 26px;

  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 16px;
  }
`;

const IconContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ConfirmButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  height: 46px;
  padding: 0px 24px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'rgba(255, 255, 255, 0.1)' : '#62b4c4')};
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50px;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'rgba(255, 255, 255, 0.2)' : '#6dc0d0')};
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }

  @media screen and (max-width: 768px) {
    min-width: fit-content;
    height: 36px;
    font-size: 12px;
    padding: 0px 14px;
  }
`;
