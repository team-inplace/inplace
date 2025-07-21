import { AiFillLike, AiFillDislike } from 'react-icons/ai';

import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { UserReviewData } from '@/types';
import { Text } from '@/components/common/typography/Text';
import { Paragraph } from '@/components/common/typography/Paragraph';
// import FallbackImage from '@/components/common/Items/FallbackImage';

export default function UserReviewItem({ likes, comment, place, createdDate }: UserReviewData) {
  const address = `${place.address.address1} ${place.address.address2} ${place.address.address3}`;
  return (
    <Wrapper to={`/detail/${place.placeId}`}>
      {/* <ImageContainer>
        <FallbackImage src={place.imgUrl} alt={place.placeName} />
      </ImageContainer> */}
      <TextContainer>
        <Title>
          <Text size="s" weight="bold">
            {place.placeName}
          </Text>
          {likes ? <AiFillLike size={26} color="#fe7373" /> : <AiFillDislike size={26} color="#6F6CFF" />}
        </Title>
        <Paragraph size="xs" weight="normal" variant="#9e9e9e">
          {address}
        </Paragraph>
        <Title>
          <Paragraph size="xs" weight="normal">
            {comment}
          </Paragraph>
          <Text size="xs" weight="normal">
            {new Date(createdDate).toLocaleDateString()}
          </Text>
        </Title>
      </TextContainer>
    </Wrapper>
  );
}
const Wrapper = styled(Link)`
  width: 100%;
  display: flex;
  gap: 24px;
  align-items: center;
  color: inherit;

  @media screen and (max-width: 768px) {
    gap: 14px;
  }
`;
const TextContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: center;
  gap: 8px;

  @media screen and (max-width: 768px) {
    gap: 4px;
  }
`;
// const ImageContainer = styled.div`
//   width: 90px;
//   aspect-ratio: 1 / 1;
//   object-fit: cover;
//   border-radius: 30px;

//   @media screen and (max-width: 768px) {
//     border-radius: 12px;
//   }
// `;

const Title = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  @media screen and (max-width: 768px) {
    align-items: end;
    :nth-child(1) {
      width: 70%;
    }
    :nth-child(2) {
      text-align: end;
    }
    svg {
      width: 20px;
      height: 20px;
    }
  }
`;
