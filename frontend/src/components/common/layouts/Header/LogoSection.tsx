import { Link } from 'react-router-dom';
import styled from 'styled-components';
import Logo from '@/assets/images/InplaceLogo.png';
import { Text } from '@/components/common/typography/Text';

export default function LogoSection() {
  return (
    <LogoLink to="/">
      <LogoContainer>
        <LogoImage src={Logo} alt="인플레이스 로고" />
        <Text size="l" weight="bold" variant="mint">
          인플레이스
        </Text>
      </LogoContainer>
    </LogoLink>
  );
}

const LogoLink = styled(Link)`
  text-decoration: none;
  display: flex;
  align-items: center;
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
`;

const LogoImage = styled.img`
  height: 22px;
  width: auto;
  margin-right: 10px;
`;
