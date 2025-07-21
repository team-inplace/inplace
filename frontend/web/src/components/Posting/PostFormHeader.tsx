import styled from 'styled-components';
import { IoIosArrowBack } from 'react-icons/io';
import { Text } from '../common/typography/Text';
import useIsMobile from '@/hooks/useIsMobile';

type PostFormHeaderProps = {
  onBack: () => void;
};
export default function PostFormHeader({ onBack }: PostFormHeaderProps) {
  const isMobile = useIsMobile();
  return (
    <DetailHeader>
      <BackBtn type="button" onClick={onBack}>
        <IoIosArrowBack size={isMobile ? 20 : 24} />
      </BackBtn>
      <Text size="m" weight="bold">
        글 쓰기
      </Text>
      <SubmitButton type="submit">등록</SubmitButton>
    </DetailHeader>
  );
}
const DetailHeader = styled.div`
  z-index: 10;
  width: 100%;
  padding: 10px 0px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const BackBtn = styled.button`
  display: flex;
  background: none;
  border: none;
  cursor: pointer;
  svg {
    color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : 'black')};
  }
`;

const SubmitButton = styled.button`
  border: none;
  background: none;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#55ebff' : '#47c8d9')};
  font-weight: bold;
  font-size: 20px;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }

  @media screen and (max-width: 768px) {
    font-size: 16px;
  }
`;
