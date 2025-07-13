import styled from 'styled-components';

import { IText, Text } from './Text';

export interface IParagraph extends IText {
  children: React.ReactNode;
}

const ParagraphStyled = styled.p``;

export function Paragraph({ size, weight, variant, children }: IParagraph) {
  return (
    <ParagraphStyled>
      <Text size={size} weight={weight} variant={variant}>
        {children}
      </Text>
    </ParagraphStyled>
  );
}
