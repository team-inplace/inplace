import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { FaChevronRight } from 'react-icons/fa6';
import VisitPlace from '@/assets/images/visit-place.png';
import { Paragraph } from '@/components/common/typography/Paragraph';
import useIsMobile from '@/hooks/useIsMobile';
import { useABTest } from '@/provider/ABTest';
import { sendGAEvent } from '@/utils/test/googleTestUtils';

interface MapSectionProps {
  mainText?: string;
  highlightText?: string;
}

export default function MapSection({ highlightText, mainText }: MapSectionProps) {
  const navigate = useNavigate();
  const isMobile = useIsMobile();
  const testGroup = useABTest('map_ui_test');
  const highlight = highlightText || '';
  const paragraph = mainText || '인플루언서가 방문한 장소는 어디일까요?';

  const handleConfirmClick = () => {
    sendGAEvent('map_navigation_click_main', {
      test_name: 'map_ui_test',
      variation: testGroup,
      from_path: window.location.pathname,
      to_path: '/map',
    });
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
                wordBreak: 'keep-all',
                width: '100%',
              }}
            >
              <HighlightText>{highlight}</HighlightText> {paragraph}
            </span>
          </Paragraph>
          <IconContainer>
            <img src={VisitPlace} alt="방문 장소" width={isMobile ? '30px' : '44px'} />
          </IconContainer>
        </TextSection>
        <ConfirmButton onClick={handleConfirmClick}>
          {isMobile ? '보기' : '확인하러 가기'}
          <FaChevronRight size={isMobile ? 10 : 12} />
        </ConfirmButton>
      </Wrapper>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#2f2f2f' : '#d0f0f08b')};
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#292929')};
  border-radius: 10px;
  padding: 0 42px;

  @media screen and (max-width: 768px) {
    padding: 0 16px;
    box-sizing: border-box;
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
    gap: 8px;
  }
`;

const TextSection = styled.div`
  display: flex;
  align-items: center;
  gap: 26px;

  @media screen and (max-width: 768px) {
    gap: 4px;
  }
`;

const HighlightText = styled.span`
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#55EBFF' : '#35b1c1')};
  font-weight: bold;
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
  }

  @media screen and (max-width: 768px) {
    min-width: fit-content;
    height: 30px;
    font-size: 12px;
    gap: 2px;
    padding: 0px 12px;
  }
`;
