import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { getSearchInfluencerPath, getSearchPlacePath, getSearchVideoPath } from '@/api/hooks/useGetSearchData';
import { getSearchKeywordPath } from '@/api/hooks/useGetSearchKeyword';

export const searchHandlers = [
  rest.get(`${BASE_URL}/search/complete`, (req, res, ctx) => {
    const searchParam = req.url.searchParams.get('value') || '';
    const data = [
      { result: '프론트엔드', score: 0, searchType: 'PLACE' },
      { result: '온으프보딩', score: 0, searchType: 'INFLUENCER' },
      { result: '프론트검색', score: 0, searchType: 'PLACE' },
      { result: '띄워쓰기 검사', score: 0, searchType: 'PLACE' },
      { result: '21료코', score: 0, searchType: 'PLACE' },
      { result: 'javascript', score: 0, searchType: 'PLACE' },
      { result: '풍자', score: 0, searchType: 'PLACE' },
    ];

    const filteredData = data.filter((item) => {
      return searchParam.split('').some((char) => item.result.includes(char));
    });

    return res(ctx.status(200), ctx.json(filteredData));
  }),

  rest.get(`${BASE_URL}${getSearchInfluencerPath()}`, (req, res, ctx) => {
    const searchParam = req.url.searchParams.get('value') || '';
    const data = [
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
        influencerName: '풍자',
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
        influencerName: '풍',
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
    ];
    const filteredData = data.filter((item) => {
      return searchParam.split('').some((char) => item.influencerName.includes(char));
    });
    return res(ctx.status(200), ctx.json(filteredData));
  }),
  rest.get(`${BASE_URL}${getSearchVideoPath()}`, (req, res, ctx) => {
    const searchParam = req.url.searchParams.get('value') || '';
    const data = [
      {
        videoId: 1,
        videoAlias: '성시경이 갔다가 못 돌아온 바로 그곳',
        videoUrl: 'https://youtu.be/qbqquv_8wM0?si=j7LiU5DSfTVpKa1I',
        place: {
          placeId: 1,
          placeName: '이선장네',
        },
      },
      {
        videoId: 2,
        videoAlias: '풍자가 기절한 바로 그곳',
        videoUrl: 'https://youtu.be/g5P0vpGSbng?si=RB71ZAx12kDas9a6',
        place: {
          placeId: 2,
          placeName: '풍자또가',
        },
      },
      {
        videoId: 3,
        videoAlias: '풍 기절한 바로 그곳',
        videoUrl: 'https://youtu.be/g5P0vpGSbng?si=RB71ZAx12kDas9a6',
        place: {
          placeId: 3,
          placeName: '풍',
        },
      },
    ];
    const filteredData = data.filter((item) => {
      return searchParam.split('').some((char) => item.videoAlias.includes(char));
    });
    return res(ctx.status(200), ctx.json(filteredData));
  }),
  rest.get(`${BASE_URL}${getSearchPlacePath()}`, (req, res, ctx) => {
    const searchParam = req.url.searchParams.get('value') || '';
    const data = [
      {
        placeId: 1,
        placeName: '료코',
        imageUrl: null,
        influencerName: '성시경',
        likes: true,
        address: {
          address1: '대구광역시',
          address2: '북구',
          address3: '대현로',
        },
      },
      {
        placeId: 2,
        placeName: '이선장네',
        imageUrl: null,
        likes: true,
        address: {
          address1: '대구광역시',
          address2: '북구',
          address3: '대현로',
        },
        influencerName: '성시경',
      },
      {
        placeId: 3,
        placeName: '풍자또가',
        imageUrl: null,
        likes: true,
        address: {
          address1: '대구광역시',
          address2: '북구',
          address3: '대현로',
        },
        influencerName: '성시경',
      },
      {
        placeId: 4,
        placeName: '풍',
        imageUrl: null,
        likes: true,
        address: {
          address1: '대구광역시',
          address2: '북구',
          address3: '대현로',
        },
        influencerName: '성시경',
      },
    ];
    const filteredData = data.filter((item) => {
      return searchParam.split('').some((char) => item.placeName.includes(char));
    });
    return res(ctx.status(200), ctx.json(filteredData));
  }),
  rest.get(`${BASE_URL}${getSearchKeywordPath()}`, (_, res, ctx) => {
    const data = [
      { placeId: 12, longitude: '128.642', latitude: '35.79' },
      { placeId: 13, longitude: '128.545', latitude: '35.81' },
      { placeId: 14, longitude: '128.51', latitude: '35.7777' },
      { placeId: 15, longitude: '128.79', latitude: '35.783222' },
      { placeId: 16, longitude: '128.71', latitude: '35.02222' },
    ];

    return res(ctx.status(200), ctx.json(data));
  }),
];
export default searchHandlers;
