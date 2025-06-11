import styled from 'styled-components';
import { IoIosArrowUp } from 'react-icons/io';
import useScrollToTop from '@/hooks/useScrollToTop';
import Button from '.';
import useTheme from '@/hooks/useTheme';

export default function ScrollToTop() {
  const { theme } = useTheme();
  const isDark = theme === 'dark';
  const handleClick = useScrollToTop();
  return (
    <ButtonWrapper aria-label="상단 이동 버튼" variant={isDark ? 'outline' : 'blackOutline'} onClick={handleClick}>
      <IoIosArrowUp size={30} />
    </ButtonWrapper>
  );
}

const ButtonWrapper = styled(Button)`
  position: fixed;
  right: max(20px, calc(50% - 480px));
  bottom: 5%;
  width: 46px;
  height: 46px;
  border-radius: 50%;
  cursor: pointer;
  z-index: 10;
`;
