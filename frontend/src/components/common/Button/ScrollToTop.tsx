import styled from 'styled-components';
import { IoIosArrowUp } from 'react-icons/io';
import { useEffect, useState } from 'react';
import useScrollToTop from '@/hooks/useScrollToTop';
import Button from '.';
import useTheme from '@/hooks/useTheme';

export default function ScrollToTop() {
  const { theme } = useTheme();
  const isDark = theme === 'dark';
  const handleClick = useScrollToTop();

  const [show, setShow] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 200) {
        setShow(true);
      } else {
        setShow(false);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  if (!show) return null;

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
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};
  cursor: pointer;
  z-index: 10;

  @media screen and (max-width: 768px) {
    display: none;
  }
`;
