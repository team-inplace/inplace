import styled, { keyframes } from 'styled-components';
import { HEADER_HEIGHT } from './Header';

type Props = {
  size?: number;
};
export default function Loading({ size = 24 }: Props) {
  return (
    <SpinnerWrapper>
      <Wrapper size={size} className="loader" />
    </SpinnerWrapper>
  );
}

const spin = keyframes`
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
`;

const Wrapper = styled.div<Pick<Props, 'size'>>`
  border: 4px solid rgba(177, 177, 177, 0.01);
  width: ${({ size }) => size}px;
  height: ${({ size }) => size}px;
  border-radius: 50%;
  border-left-color: #999;

  animation: ${spin} 1s linear infinite;
`;

const SpinnerWrapper = styled.div`
  width: 100%;
  height: calc(100vh - ${HEADER_HEIGHT});
  display: flex;
  justify-content: center;
  padding: 80px 16px;
`;
