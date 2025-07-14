import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { RequestPlaceReview } from '@/types';
import { getReviewInfoPath } from '@/api/hooks/useGetReviewInfo';
import { postPlaceReviewPath } from '@/api/hooks/usePostPlaceReview';

const mockReviewInfo = {
  placeName: '료코',
  placeAddress: '대구 북구 대학로',
  placeImgUrl: '',
  influencerName: '성시경',
  userNickname: '테스트유저',
};

export const reviewHandlers = [
  rest.get(`${BASE_URL}${getReviewInfoPath('123')}`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockReviewInfo));
  }),

  rest.post(`${BASE_URL}${postPlaceReviewPath('123')}`, async (req, res, ctx) => {
    const { likes, comments } = req.body as RequestPlaceReview;
    return res(
      ctx.status(200),
      ctx.json({
        likes,
        comments,
      }),
    );
  }),
];

export default reviewHandlers;
