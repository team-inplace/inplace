import { useState } from 'react';
import styled from 'styled-components';
import { Paragraph } from '@/components/common/typography/Paragraph';
import useIsMobile from '@/hooks/useIsMobile';

const REVIEW_TRUNCATE_LENGTH = {
  MOBILE: 65,
  DESKTOP: 150,
};

export default function GoogleReviewComment({ text }: { text: string }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const isMobile = useIsMobile();

  const slice = isMobile ? REVIEW_TRUNCATE_LENGTH.MOBILE : REVIEW_TRUNCATE_LENGTH.DESKTOP;
  const truncatedText = text.length > slice ? `${text.slice(0, slice)}...` : text;

  return (
    <Paragraph size="xs" weight="normal">
      {isExpanded ? text : truncatedText}
      {text.length > slice && !isExpanded && <MoreLink onClick={() => setIsExpanded(true)}>더보기</MoreLink>}
      {isExpanded && <MoreLink onClick={() => setIsExpanded(false)}>간략히</MoreLink>}
    </Paragraph>
  );
}
const MoreLink = styled.span`
  color: grey;
  cursor: pointer;
  margin-left: 5px;
  font-size: 14px;

  @media screen and (max-width: 768px) {
    font-size: 12px;
  }
`;
