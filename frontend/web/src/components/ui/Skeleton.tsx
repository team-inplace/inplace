import React from 'react';
import styled, { keyframes } from 'styled-components';

type SkeletonProps = {
  width?: string;
  height?: string;
  borderRadius?: string;
} & React.HtmlHTMLAttributes<HTMLDivElement>;

export default function Skeleton({ width = '100%', height, borderRadius = '4px', ...props }: SkeletonProps) {
  return <Wrapper width={width} height={height} borderRadius={borderRadius} {...props} />;
}

const pulse = keyframes`
  0%, 100% {
    opacity: 0.4;
  }
  50% {
    opacity: 0.1;
  }
`;
const Wrapper = styled.div.withConfig({
  shouldForwardProp: (prop) => !['width', 'height', 'borderRadius'].includes(prop),
})<SkeletonProps>`
  display: inline-block;
  background-color: #828282;
  width: ${({ width }) => `${width}`};
  height: ${({ height }) => `${height}`};
  border-radius: ${({ borderRadius }) => `${borderRadius}`};
  animation: ${pulse} 2s ease-in-out infinite;
`;
