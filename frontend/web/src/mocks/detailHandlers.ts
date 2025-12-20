import { rest } from 'msw';
import { getConfig } from '@inplace-frontend-monorepo/shared/api/config';
import { getReviewPath } from '@/api/hooks/useGetReview';
import { getSendInfoPath } from '@/api/hooks/useGetSendInfo';
import BasicImage from '@/assets/images/basic-image.webp';
import { dummy } from './mainHandlers';

const config = getConfig();
const BASE_URL = config.baseURL;

let reviews = [
  {
    reviewId: 1,
    likes: true,
    comment: '정말 좋았어요! 다음에 또 오고 싶습니다',
    userNickname: '사용자1',
    createdDate: new Date('2024-10-01T12:00:00Z'),
    mine: true,
  },
  {
    reviewId: 2,
    likes: false,
    comment: '별로였어요. 개선이 필요합니다.',
    userNickname: '사용자2',
    createdDate: new Date('2024-10-02T15:30:00Z'),
    mine: false,
  },
  {
    reviewId: 3,
    likes: true,
    comment:
      '가성비 좋은 깔끔한 인테리어의 정육식당 해봉정육. 안창살, 토시살, 안심, 특수부의 모듬 모두 상당히 고퀄리티다. 특히 안창살이 좋았는데, 육향도 진하고 잡내없이 식감과 부드러움 그리고 육즙까지 모두 일품. 토시살은 안창살보단 약간 질긴 감이 있지만 나름대로의 맛이 좋다. 이 집의 불판은 무쇠 솥뚜껑을 뒤짚어 높은 스타일로 강한 화력이 특징. 양지찌개는 고기도 많이 들어 있고, 걸죽한 국물에 된장밥 해먹으면 좋을 듯한 느낌. 마지막으로 파채를 듬뿍 넣어 볶는 볶음밥도 술안주로 딱 좋다. 전체적으로 맛, 서비스, 인테리어 모두 좋은 곳.',
    userNickname: '사용자3',
    createdDate: new Date('2024-10-03T09:15:00Z'),
    mine: false,
  },
  {
    reviewId: 4,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자4',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 5,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자5',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 6,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자6',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 7,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자7',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 8,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자8',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 9,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자9',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 10,
    likes: false,
    comment: '서비스가 아쉬웠습니다',
    userNickname: '사용자10',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 11,
    likes: false,
    comment: '11번입니다리',
    userNickname: '사용자11',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 12,
    likes: false,
    comment: '12번입니다리',
    userNickname: '사용자11',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 13,
    likes: false,
    comment: '11번입니다리',
    userNickname: '사용자11',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
  {
    reviewId: 14,
    likes: false,
    comment: '11번입니다리',
    userNickname: '사용자11',
    createdDate: new Date('2024-10-03T11:45:00Z'),
    mine: true,
  },
];
const infodummy = {
  placeId: 1,
  placeName: '료코',
  address: {
    address1: '경북',
    address2: '경주시',
    address3: '첨성로99번길 24 료코',
  },
  videos: [
    {
      influencerName: '성시경',
      videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME',
    },
    {
      influencerName: '광마니',
      videoUrl: 'https://www.youtube.com/watch?v=cgAPp2SRDiE',
    },
    {
      influencerName: '쯔양',
      videoUrl: 'https://www.youtube.com/watch?v=B_gGpdUVe8o',
    },
    {
      influencerName: '떡볶퀸',
      videoUrl: 'https://www.youtube.com/watch?v=1A0ItL8tFZU',
    },
    {
      influencerName: '기본',
      videoUrl: BasicImage,
    },
  ],
  category: '한식',
  longitude: '126.570667',
  latitude: '33.450701',
  facility: {
    acceptsCreditCards: false,
    freeParkingLot: false,
    paidParkingLot: true,
    wheelchairAccessibleSeating: true,
  },
  rating: 2.5,
  googlePlaceUrl: 'https://google.com',
  kakaoPlaceUrl: 'https://kakao.com',
  naverPlaceUrl: 'https://naver.com',
  googleReviews: [
    {
      like: true,
      text: '가성비 좋은 깔끔한 인테리어의 정육식당 해봉정육. 안창살, 토시살, 안심, 특수부의 모듬 모두 상당히 고퀄리티다. 특히 안창살이 좋았는데, 육향도 진하고 잡내없이 식감과 부드러움 그리고 육즙까지 모두 일품. 토시살은 안창살보단 약간 질긴 감이 있지만 나름대로의 맛이 좋다. 이 집의 불판은 무쇠 솥뚜껑을 뒤짚어 높은 스타일로 강한 화력이 특징. 양지찌개는 고기도 많이 들어 있고, 걸죽한 국물에 된장밥 해먹으면 좋을 듯한 느낌. 마지막으로 파채를 듬뿍 넣어 볶는 볶음밥도 술안주로 딱 좋다. 전체적으로 맛, 서비스, 인테리어 모두 좋은 곳.',
      name: '효은',
      publishTime: new Date('2024-10-03T11:45:00Z'),
    },
    {
      like: false,
      text: '가성비 좋은 깔끔한 인테리어의 정육식당 해봉정육. 안창살, 토시살, 안심, 특수부의 모듬 모두 상당히 고퀄리티다. 특히 안창살이 좋았는데, 육향도 진하고 잡내없이 식감과 부드러움 그리고 육즙까지 모두 일품. 토시살은 안창살보단 약간 질긴 감이 있지만 나름대로의 맛',
      name: '효f은',
      publishTime: new Date('2024-10-03T11:45:00Z'),
    },
    {
      like: true,
      text: '가성비 좋은 깔끔한 인테리어의 정육식당 해봉정육. 안창살, 토시살, 안심, 특수부의 모듬 모두 상당히 고퀄리티다. 특히 안창살이 좋았는데, 육향도 진하고 잡내없이 식감과 부드러움 그리고 육즙까지 모두 일품. 토시살은 안창살보단 약간 질긴 감이 있지만 나름대로의 맛이 좋다. 이 집의 불판은 무쇠 솥뚜껑을 뒤짚어 높은 스타일로 강한 화력이 특징. 양지찌개는 고기도 많이 들어 있고, 걸죽한 국물에 된장밥 해먹으면 좋을 듯한 느낌. 마지막으로 파채를 듬뿍 넣어 볶는 볶음밥도 술안주로 딱 좋다. 전체적으로 맛, 서비스, 인테리어 모두 좋은 곳.',
      name: '효a은',
      publishTime: new Date('2024-10-03T11:45:00Z'),
    },
    {
      like: true,
      text: '정말 좋았어요! 다음에 또 니다정말 좋았어요! 다음에 또 오고 싶습니다정말 좋았어요! 다음에 또 오고 싶습니다',
      name: '효q은',
      publishTime: new Date('2024-10-03T11:45:00Z'),
    },
    {
      like: false,
      text: '별롭니다',
      name: '효b은',
      publishTime: new Date('2024-10-03T11:45:00Z'),
    },
  ],
  openingHours: ['월~수 10:00 - 12:00', '월~목 10:00 - 12:00', '월~화 10:00 - 12:00'],
  reviewLikes: {
    like: 240,
    dislike: 100,
  },
  likedCount: 100,
  likes: false,
  surroundVideos: dummy,
};
export const detailHandlers = [
  rest.get(`${BASE_URL}/places/1`, (_, res, ctx) => {
    if (infodummy) {
      return res(ctx.status(200), ctx.json(infodummy));
    }
    return res(ctx.status(404), ctx.json({ message: 'Place not found' }));
  }),

  rest.get(`${BASE_URL}${getReviewPath('1')}`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = reviews.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = reviews.slice(startIndex, endIndex);

    return res(
      ctx.status(200),
      ctx.json({
        totalPages,
        totalElements,
        size,
        content: paginatedContent,
        number: page,
        sort: {
          empty: true,
          sorted: true,
          unsorted: true,
        },
        numberOfElements: paginatedContent.length,
        pageable: {
          offset: page * size,
          sort: {
            empty: true,
            sorted: true,
            unsorted: true,
          },
          paged: true,
          pageNumber: page,
          pageSize: size,
          unpaged: false,
        },
        first: page === 0,
        last: page === totalPages - 1,
        empty: paginatedContent.length === 0,
      }),
    );
  }),
  rest.get(`${BASE_URL}${getReviewPath('7')}`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = reviews.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = reviews.slice(startIndex, endIndex);

    return res(
      ctx.status(200),
      ctx.json({
        totalPages,
        totalElements,
        size,
        content: paginatedContent,
        number: page,
        sort: {
          empty: true,
          sorted: true,
          unsorted: true,
        },
        numberOfElements: paginatedContent.length,
        pageable: {
          offset: page * size,
          sort: {
            empty: true,
            sorted: true,
            unsorted: true,
          },
          paged: true,
          pageNumber: page,
          pageSize: size,
          unpaged: false,
        },
        first: page === 0,
        last: page === totalPages - 1,
        empty: paginatedContent.length === 0,
      }),
    );
  }),
  rest.post(`${BASE_URL}/places/likes`, (req, res, ctx) => {
    const { placeId, likes } = req.body as { placeId: number | string; likes: boolean };
    const targetId = Number(placeId);

    console.log(`📢 [MSW] 장소 좋아요 요청: ID ${targetId}, Likes: ${likes}`);

    infodummy.likes = likes;
    infodummy.likedCount = likes ? infodummy.likedCount + 1 : Math.max(0, infodummy.likedCount - 1);

    return res(ctx.status(200), ctx.json({ success: true }));
  }),
  rest.delete(`${BASE_URL}/reviews/:id`, (req, res, ctx) => {
    const { id } = req.params;
    const normalizedId = id instanceof Array ? id[0] : id;
    reviews = reviews.filter((review) => review.reviewId !== parseInt(normalizedId, 10));
    return res(ctx.status(200), ctx.json({ message: '리뷰가 삭제되었습니다.' }));
  }),
  rest.get(`${BASE_URL}${getSendInfoPath('1')}`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json({ message: '완료되었습니다.' }));
  }),
];
export default detailHandlers;
