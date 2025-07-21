import { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import styled from 'styled-components';
import { useQueryClient } from '@tanstack/react-query';
import { RequestPlaceReview } from '@/types';
import RatingStep from '@/components/Review/steps/RatingStep';
import CommentStep from '@/components/Review/steps/CommentStep';
import { usePostPlaceReview } from '@/api/hooks/usePostPlaceReview';
import { useGetReviewInfo } from '@/api/hooks/useGetReviewInfo';
import Header from '@/components/common/layouts/Header';
import Button from '@/components/common/Button';

export default function ReviewPage() {
  const { uuid } = useParams() as { uuid: string };
  const { data: reviewInfo } = useGetReviewInfo(uuid);
  const { mutate: postReview } = usePostPlaceReview(uuid);
  const queryClient = useQueryClient();

  const [reviewData, setReviewData] = useState<RequestPlaceReview>({
    likes: null,
    comments: '',
  });
  const [currentStep, setCurrentStep] = useState<number>(1);
  const [isCompleted, setIsCompleted] = useState(false);

  const handleRatingSubmit = (isLiked: boolean) => {
    setReviewData((prev) => ({ ...prev, likes: isLiked }));
    setCurrentStep(2);
  };

  const handleCommentSubmit = async (comment: string) => {
    const finalReviewData = {
      likes: reviewData.likes,
      comments: comment,
    };

    postReview(finalReviewData, {
      onSuccess: () => {
        setIsCompleted(true);
        queryClient.invalidateQueries({ queryKey: ['placeInfo'] });
        queryClient.invalidateQueries({ queryKey: ['review'] });
      },
      onError: (error) => {
        console.error('리뷰 등록 중 오류가 발생했습니다:', error);
      },
    });
  };

  if (isCompleted) {
    return (
      <Container>
        <Header />
        <MainContent>
          <CompletionMessage>
            <h2>완료되었습니다</h2>
            <p>리뷰가 성공적으로 등록되었습니다.</p>
          </CompletionMessage>
          <Link to="/">
            <StyledButton aria-label="리뷰완료 홈으로" variant="outline" size="small">
              홈으로 이동하기
            </StyledButton>
          </Link>
        </MainContent>
      </Container>
    );
  }

  return (
    <Container>
      <Header />
      <MainContent>
        {currentStep === 1 && <RatingStep onSubmit={handleRatingSubmit} placeInfo={reviewInfo} />}
        {currentStep === 2 && (
          <CommentStep
            isLiked={reviewData.likes}
            onBack={() => setCurrentStep(1)}
            onSubmit={handleCommentSubmit}
            placeInfo={reviewInfo}
          />
        )}
      </MainContent>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #1c1c1c;
  color: white;
  width: min(100%, 700px);
  margin: 0 auto;
`;

const MainContent = styled.main`
  flex: 1;
  padding: 1.5rem;
  padding-bottom: 5rem;
`;

const CompletionMessage = styled.div`
  margin-top: 10rem;
  margin-bottom: 3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;

  h2 {
    font-size: 1.5rem;
    margin-bottom: 1rem;
  }

  p {
    color: #9e9e9e;
  }
`;

const StyledButton = styled(Button)`
  width: 40%;
  padding: 20px;
  margin: 20px auto;
`;
