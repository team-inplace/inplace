import { rest } from 'msw';
import { getConfig } from '@inplace-frontend-monorepo/shared/api/config';
import { InfluencerInfoData } from '@/types';

const config = getConfig();
const BASE_URL = config.baseURL;

const dummyInfluencerInfo: Record<string, InfluencerInfoData> = {
  '1': {
    influencerId: 1,
    influencerName: '성시경',
    influencerImgUrl: '',
    influencerJob: '가수',
    likes: false,
    follower: 200,
    placeCount: 200,
  },
  '2': {
    influencerId: 2,
    influencerName: '풍자',
    influencerImgUrl: '',
    influencerJob: '가수',
    likes: false,
    follower: 20,
    placeCount: 200,
  },
  '3': {
    influencerId: 3,
    influencerName: '경경',
    influencerImgUrl: '',
    influencerJob: '가수',
    likes: false,
    follower: 200,
    placeCount: 10,
  },
};
const mockVideos = [
  {
    videoId: 1,
    influencerName: '정육왕',
    videoUrl: 'https://youtu.be/qbqquv_8wM0?si=j7LiU5DSfTVpKa1I',
    place: {
      placeId: 1,
      placeName: '이선장네',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
  {
    videoId: 2,
    influencerName: '풍자',
    videoUrl: 'https://youtu.be/g5P0vpGSbng?si=RB71ZAx12kDas9a6',
    place: {
      placeId: 2,
      placeName: '성심당',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
  {
    videoId: 3,
    influencerName: '히밥',
    videoUrl: 'https://youtu.be/cz1EvePzqfM?si=L5ZsKV4DXikGIuEP',
    place: {
      placeId: 3,
      placeName: '왕거미식당',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
  {
    videoId: 7,
    influencerName: '풍자',
    videoUrl: 'https://youtu.be/g5P0vpGSbng?si=RB71ZAx12kDas9a6',
    place: {
      placeId: 2,
      placeName: '풍자또가',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
  {
    videoId: 4,
    influencerName: '히밥',
    videoUrl: 'https://youtu.be/cz1EvePzqfM?si=L5ZsKV4DXikGIuEP',
    place: {
      placeId: 3,
      placeName: '가성비집',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
  {
    videoId: 5,
    influencerName: '풍자',
    videoUrl: 'https://youtu.be/g5P0vpGSbng?si=RB71ZAx12kDas9a6',
    place: {
      placeId: 2,
      placeName: '풍자또가',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
  {
    videoId: 6,
    influencerName: '히밥',
    videoUrl: 'https://youtu.be/cz1EvePzqfM?si=L5ZsKV4DXikGIuEP',
    place: {
      placeId: 3,
      placeName: '가성비집',
      address: {
        address1: '대구',
        address2: '북구',
        address3: '대현동 119-11',
      },
    },
  },
];
export const InfluencerHandlers = [
  rest.get(`${BASE_URL}/influencers/:id([0-9]+)`, (req, res, ctx) => {
    const { id } = req.params;
    const data = dummyInfluencerInfo[id.toString()];

    return res(ctx.status(200), ctx.json(data));
  }),
  rest.get(`${BASE_URL}/influencers/:id/videos`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = mockVideos.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = mockVideos.slice(startIndex, endIndex);

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
];

export default InfluencerHandlers;
