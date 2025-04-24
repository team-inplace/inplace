import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { getUserInfoPath } from '@/api/hooks/useGetUserInfo';
import { getUserInfluencerPath } from '@/api/hooks/useGetUserInfluencer';
import { getUserPlacePath } from '@/api/hooks/useGetUserPlace';
import { getUserReviewPath } from '@/api/hooks/useGetUserReview';
import { postPlaceReviewPath } from '@/api/hooks/usePostPlaceReview';
import { patchNicknamePath } from '@/api/hooks/usePatchNickname';

const mockInfluencers = [
  {
    influencerId: 1,
    influencerName: '성시경',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '모델',
    likes: true,
  },
  {
    influencerId: 2,
    influencerName: '풍자',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 3,
    influencerName: '아이유',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '가수',
    likes: false,
  },
  {
    influencerId: 4,
    influencerName: '이영자',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '방송인',
    likes: false,
  },
  {
    influencerId: 5,
    influencerName: '정해인',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 6,
    influencerName: '황정민',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 7,
    influencerName: '히밥',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '유튜버',
    likes: false,
  },
  {
    influencerId: 8,
    influencerName: '백종원',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '유튜버',
    likes: true,
  },
  {
    influencerId: 9,
    influencerName: '안성재',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '셰프',
    likes: false,
  },
  {
    influencerId: 10,
    influencerName: '임영웅',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 11,
    influencerName: '짱구 대디',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '패션 유튜버',
    likes: false,
  },
];

const mockReviews = [
  {
    reviewId: 1,
    place: {
      placeId: '1',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '대구광역시',
        address2: '북구',
        address3: '대현로',
      },
    },
    likes: true,
    comment: '료무라이스 맛있어요~~ 다시 가고 시퍼요',
    createdDate: new Date('2024-10-01T12:00:00Z'),
  },
  {
    reviewId: 2,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 3,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 4,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 5,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 6,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 7,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 8,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 9,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 10,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
  {
    reviewId: 11,
    place: {
      placeId: '2',
      placeName: '료코',
      imgUrl: null,
      address: {
        address1: '경상북도',
        address2: '경주시',
        address3: '황리단길',
      },
    },
    likes: false,
    comment: '느끼하고 양 적어요 우우 ㅜㅜ',
    createdDate: new Date('2024-10-02T12:00:00Z'),
  },
];

const mockPlaces = [
  {
    placeId: 1,
    placeName: '료코존나길면어떻게될까용가리',
    imageUrl: null,
    likes: true,
    influencerName: '성시경',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 2,
    placeName: '이선장네',
    likes: true,
    imageUrl: null,
    influencerName: '임영웅',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로주소가길면어떻게되나요',
    },
  },
  {
    placeId: 3,
    placeName: '풍자또가',
    imageUrl: null,
    likes: true,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 4,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 5,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 6,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 7,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 8,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 9,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 10,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
  {
    placeId: 11,
    placeName: '풍자또가',
    imageUrl: null,
    influencerName: '풍자',
    address: {
      address1: '대구광역시',
      address2: '북구',
      address3: '대현로',
    },
  },
];
let nickName = '랄라스윗칩';
export const myHandlers = [
  rest.get(`${BASE_URL}${getUserInfoPath()}`, (_, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        nickname: nickName,
        imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      }),
    );
  }),
  rest.get(`${BASE_URL}${getUserInfluencerPath()}`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = mockInfluencers.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = mockInfluencers.slice(startIndex, endIndex);

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
  rest.get(`${BASE_URL}${getUserPlacePath()}`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = mockPlaces.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = mockPlaces.slice(startIndex, endIndex);

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
  rest.get(`${BASE_URL}${getUserReviewPath()}`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = mockReviews.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = mockReviews.slice(startIndex, endIndex);

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
  rest.post(`${BASE_URL}${postPlaceReviewPath('1')}`, async (_, res, ctx) => {
    return res(ctx.status(200), ctx.json({ message: 'send successfully.' }));
  }),
  rest.patch(`${BASE_URL}${patchNicknamePath()}`, async (req, res, ctx) => {
    const { nickname } = req.body as { nickname: string };
    nickName = nickname;
    return res(ctx.status(200), ctx.json({ message: 'Nickname updated successfully.', nickName }));
  }),
];
export default myHandlers;
