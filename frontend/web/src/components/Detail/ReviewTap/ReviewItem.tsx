import { AiFillLike, AiFillDislike } from 'react-icons/ai';
import styled from 'styled-components';
import { useQueryClient } from '@tanstack/react-query';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';

import { ReviewData } from '@/types';
import { useDeleteReview } from '@/api/hooks/useDeleteReview';

export default function ReviewItem({ reviewId, likes, comment, userNickname, createdDate, mine }: ReviewData) {
  const queryClient = useQueryClient();

  const { mutate: deleteReview } = useDeleteReview();
  const handleReviewDelete = () => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteReview(String(reviewId), {
      onSuccess: () => {
        alert('삭제되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['placeInfo'] });
        queryClient.invalidateQueries({ queryKey: ['review'] });
      },
      onError: () => {
        alert('리뷰를 삭제하지 못했어요. 다시 시도해주세요!');
      },
    });
  };
  return (
    <Wrapper>
      <Title>
        <Name>
          <Text size="xs" weight="bold">
            {userNickname}
          </Text>
          {likes ? <AiFillLike size={22} color="#fe7373" /> : <AiFillDislike size={22} color="#6F6CFF" />}
        </Name>
        <Text size="xs" weight="normal">
          {new Date(createdDate).toLocaleDateString()}
        </Text>
      </Title>
      <Comment>
        <Paragraph size="xs" weight="normal">
          {comment}
        </Paragraph>
        {mine ? (
          <DeleteBtn aria-label="리뷰 삭제" onClick={handleReviewDelete}>
            삭제
          </DeleteBtn>
        ) : null}
      </Comment>
    </Wrapper>
  );
}
const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  line-height: 150%;
  gap: 4px;
  padding-bottom: 14px;
  border-bottom: 0.5px solid #6e6e6e;

  @media screen and (max-width: 768px) {
    padding-bottom: 8px;
    gap: 2px;
    svg {
      width: 20px;
      height: 16px;
    }
  }
`;
const Title = styled.div`
  display: flex;
  justify-content: space-between;
`;
const Name = styled.div`
  display: flex;
  gap: 10px;
  @media screen and (max-width: 768px) {
    gap: 4px;
    align-items: center;
  }
`;
const DeleteBtn = styled.button`
  font-size: 14px;
  color: #b0b0b0;
  background: none;
  border: none;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    font-size: 12px;
  }
`;
const Comment = styled.div`
  display: flex;
  justify-content: space-between;
`;
