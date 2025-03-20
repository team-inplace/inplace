import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';

export default function Footer() {
  return (
    <FooterContainer>
      <FooterSection>
        <FooterInfo>
          <CompanyInfo>
            <Text size="xxs" weight="normal" variant="#979797">
              (주) 쿠키 7개입
            </Text>
            <Text size="xxs" weight="normal" variant="#979797">
              Copyright 2024. sevenKookies Co.,Ltd. All rights reserved
            </Text>
            <Text size="xxs" weight="normal" variant="#979797">
              주소 : 대구광역시 북구 대학로 80
            </Text>
            <Text size="xxs" weight="normal" variant="#979797">
              이메일 : inplace.kakao@gmail.com
            </Text>
          </CompanyInfo>
        </FooterInfo>
      </FooterSection>
    </FooterContainer>
  );
}

const FooterContainer = styled.footer`
  background: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#2f2f2f' : '#ecf5fa')};
  width: 100%;
  height: 160px;
  padding: 30px;
  box-sizing: border-box;

  @media screen and (max-width: 768px) {
    height: 130px;
  }
`;

const FooterSection = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
`;

const FooterInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;

  @media screen and (max-width: 768px) {
    flex-direction: column;
    gap: 10px;
  }
`;

const CompanyInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;
