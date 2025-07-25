import { useEffect, useState } from 'react';
import styled from 'styled-components';
import SurveyIcon from '@/assets/images/survey-icon.png';

export default function ResearchModal() {
  const [isOpen, setIsOpen] = useState(false);
  const [hideModal, setHideModal] = useState(false);

  const setModalCookie = () => {
    const date = new Date();
    date.setDate(date.getDate() + 3);
    document.cookie = `hideResearchModal=true; expires=${date.toUTCString()}; path=/`;
  };

  const checkModalCookie = () => {
    const cookies = document.cookie.split(';');
    const modalCookie = cookies.find((cookie) => cookie.trim().startsWith('hideResearchModal='));
    return !!modalCookie;
  };

  useEffect(() => {
    const shouldHideModal = checkModalCookie();
    setIsOpen(!shouldHideModal);
  }, []);

  const handleClose = () => {
    if (hideModal) {
      setModalCookie();
    }
    setIsOpen(false);
  };

  const handleSurvey = () => {
    window.open(
      'https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1',
      '_blank',
    );
  };

  if (!isOpen) return null;

  return (
    <ModalContainer>
      <CloseButton aria-label="설문조사 닫기" onClick={handleClose}>
        X
      </CloseButton>
      <ContentWrapper>
        <TitleWrapper>
          <Subtitle>서비스를 이용해보니 어떠셨나요?</Subtitle>
          <Title>
            바라는 점이 있다면
            <br />
            자유롭게 말씀해주세요!
          </Title>
        </TitleWrapper>
        <IconWrapper>
          <IconBackground>
            <img src={SurveyIcon} alt="설문 아이콘" />
          </IconBackground>
        </IconWrapper>
        <ButtonWrapper>
          <SubmitButton aria-label="의견 남기러 가기" onClick={handleSurvey}>
            의견 남기러 가기
          </SubmitButton>
        </ButtonWrapper>
      </ContentWrapper>
      <CheckboxWrapper>
        <input type="checkbox" id="showAgain" checked={hideModal} onChange={(e) => setHideModal(e.target.checked)} />
        <CheckboxLabel>3일 동안 보지 않기</CheckboxLabel>
      </CheckboxWrapper>
    </ModalContainer>
  );
}

const ModalContainer = styled.div`
  position: absolute;
  top: 14%;
  left: 50%;
  transform: translate(-50%);
  width: 500px;
  height: 600px;
  background-color: #e8f9ff;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  z-index: 999;
  border: 2px solid black;

  @media screen and (max-width: 768px) {
    width: 80%;
    height: 28rem;
    border: 1.6px solid black;
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

const ContentWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 40px;
  height: 100%;
  width: 100%;
  @media screen and (max-width: 768px) {
    gap: 30px;
  }
`;

const TitleWrapper = styled.div`
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const Subtitle = styled.div`
  font-size: 18px;
  color: #8d99a5;

  @media screen and (max-width: 768px) {
    font-size: 14px;
  }
`;

const Title = styled.div`
  font-size: 32px;
  font-weight: 600;
  line-height: 1.2;
  color: #2f3f68;

  @media screen and (max-width: 768px) {
    line-height: 1.1;
    font-size: 26px;
  }
`;

const IconWrapper = styled.div`
  display: flex;
  justify-content: center;
`;

const IconBackground = styled.div`
  background-color: white;
  border-radius: 50%;
  padding: 20px 40px;

  img {
    width: 132px;
    height: 143px;
  }
  @media screen and (max-width: 768px) {
    padding: 14px 28px;

    img {
      width: 96px;
      height: 106px;
    }
  }
`;

const ButtonWrapper = styled.div`
  margin: 0 auto;
`;

const SubmitButton = styled.button`
  background-color: #4cd5e8;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 14px 24px;
  cursor: pointer;
  font-size: 18px;
  box-shadow: 1px 1px 1px #b3b3b3;

  &:hover {
    background-color: #3899a5;
  }

  @media screen and (max-width: 768px) {
    padding: 12px 18px;
    font-size: 14px;
  }
`;

const CheckboxWrapper = styled.div`
  padding: 6px 4px;
  display: flex;
  align-items: center;
  gap: 2px;
  background-color: #383838;
  border-radius: 0 0 6px 6px;

  @media screen and (max-width: 768px) {
    padding: 4px 2px;
  }
`;

const CheckboxLabel = styled.label`
  font-size: 14px;
  color: #f6f6f6;

  @media screen and (max-width: 768px) {
    font-size: 12px;
  }
`;
