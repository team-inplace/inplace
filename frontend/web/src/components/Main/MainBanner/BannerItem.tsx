import { Link } from 'react-router-dom';
import styled from 'styled-components';

import { BannerData } from '@/types';

export default function BannerItem({ id, imageUrl, isFirst, influencerId }: BannerData & { isFirst: boolean }) {
  return (
    <Wrapper
      as={influencerId ? Link : 'div'}
      to={influencerId ? `/influencer/${influencerId}` : undefined}
      $isFirst={isFirst}
    >
      <Image src={imageUrl} alt={`배너-${id}번`} loading="eager" />
    </Wrapper>
  );
}
const Wrapper = styled.div<{ $isFirst: boolean }>`
  width: ${({ $isFirst }) => ($isFirst ? '100%' : '50%')};
  flex: 0 0 ${({ $isFirst }) => ($isFirst ? '100%' : '50%')};
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  position: relative;
  text-decoration: none;
  cursor: ${(props) => (props.as === 'div' ? 'default' : 'pointer')};

  @media screen and (max-width: 768px) and (min-width: 500px) {
    width: 100%;
    flex: 0 0 100%;
    height: 500px;
  }

  @media screen and (max-width: 500px) {
    width: 100%;
    height: auto;
    flex: 0 0 100%;
    aspect-ratio: 1;
  }
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;
