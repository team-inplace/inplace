import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Button from '@/components/common/Button';
import Logo from '@/assets/images/InplaceLogo.png';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { ThemeContext } from '@/provider/Themes';

export default function NotFound() {
  const navigate = useNavigate();
  const { theme } = useContext(ThemeContext);
  const buttonVariant = theme === 'dark' ? 'outline' : 'blackOutline';
  const handleHome = () => {
    navigate('/');
  };
  return (
    <Wrapper>
      <TextWrapper>
        <LogoImage src={Logo} alt="인플레이스 로고" />
        <Paragraph size="xl" weight="bold">
          페이지를 찾을 수 없어요 🥲
        </Paragraph>
        <Paragraph size="m" weight="normal">
          요청한 페이지가 존재하지 않거나 삭제되었어요.
        </Paragraph>
      </TextWrapper>
      <StyledButton aria-label="home-btn" variant={buttonVariant} onClick={handleHome}>
        홈으로 이동하기
      </StyledButton>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding-bottom: 40px;
  gap: 60px;

  @media screen and (max-width: 768px) {
    gap: 40px;
    padding-bottom: 0px;
  }
`;
const TextWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  white-space: pre-line;
  line-height: 26px;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;
const LogoImage = styled.img`
  height: 180px;
  width: 150px;

  @media screen and (max-width: 768px) {
    height: 100px;
    width: 100px;
  }
`;

const StyledButton = styled(Button)`
  width: 30%;
  @media screen and (max-width: 768px) {
    width: fit-content;
    padding: 20px;
  }
`;
