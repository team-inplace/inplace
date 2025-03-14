import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { MarkerInfo, PlaceData } from '@/types';
import { getAllMarkersPath } from '@/api/hooks/useGetAllMarkers';

const dummyInfluencers = [
  { influencerName: '성시경' },
  { influencerName: '풍자' },
  { influencerName: '아이유' },
  { influencerName: '이영자' },
  { influencerName: '정해인' },
  { influencerName: '황정민' },
  { influencerName: '히밥' },
  { influencerName: '백종원' },
  { influencerName: '안성재' },
  { influencerName: '카페랩 모디 Cafe Lab. Maudie' },
];
const dummyAllMarkers = [
  { placeId: 1, longitude: '128.59691803894955', latitude: '35.879135694467514' },
  { placeId: 2, longitude: '126.9314925', latitude: '37.5666478' },
  { placeId: 3, longitude: '128.6101069', latitude: '35.812447' },
  { placeId: 4, longitude: '128.65037738548736', latitude: '35.855300594743575' },
  { placeId: 5, longitude: '127.33301043369335', latitude: '36.351477682663756' },
  { placeId: 6, longitude: '126.931851791899', latitude: '35.1256604935401' },
  { placeId: 7, longitude: '128.6101608', latitude: '35.8857457' },
  { placeId: 8, longitude: '127.753131738285', latitude: '37.8763535115171' },
  { placeId: 9, longitude: '127.1436094525637', latitude: '37.275820090934495' },
  { placeId: 10, longitude: '129.11483195198562', latitude: '35.158494192685914' },
  { placeId: 11, longitude: '128.748643', latitude: '35.783343' },
  { placeId: 12, longitude: '128.642', latitude: '35.79' },
  { placeId: 13, longitude: '128.545', latitude: '35.81' },
  { placeId: 14, longitude: '128.51', latitude: '35.7777' },
  { placeId: 15, longitude: '128.79', latitude: '35.783222' },
  { placeId: 16, longitude: '128.71', latitude: '35.02222' },
];
const dummyMarkerInfos: Record<string, MarkerInfo> = {
  '16': {
    placeId: 16,
    placeName: '11맘스터치 구점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    menuImgUrl: null,
    videos: [
      {
        influencerName: '카페랩 모디 Cafe Lab. Maudie',
        videoUrl: 'https://www.youtube.com/watch?v=nCEtQ7dP8zY',
      },
    ],
    category: '한식',
  },
};

const dummyPlaces: PlaceData[] = [
  {
    placeId: 1,
    placeName: '광명반점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '칠성로 70-1 주소개길면어떻게되너ㅏㅏㄴㅁㄴ',
    },
    category: '음식점',
    videos: [
      {
        influencerName: '아이유',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
      {
        influencerName: '성시경',
        videoUrl: '',
      },
    ],
    longitude: '128.59691803894955',
    latitude: '35.879135694467514',
    likes: true,
    menuImgUrl: null,
  },
  {
    placeId: 2,
    placeName: '긴자료코 홍대본점',
    address: {
      address1: '서울',
      address2: '마포구',
      address3: '서교동',
    },
    category: '한식',
    videos: [
      {
        influencerName: '카페ie',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
    ],
    longitude: '126.9314925',
    latitude: '37.5666478',
    likes: true,
    menuImgUrl: null,
  },
  {
    placeId: 3,
    placeName: '맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-9',
    },
    category: '일식',
    videos: [
      {
        influencerName: '아이유',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t=6s',
      },
    ],
    longitude: '128.6101069',
    latitude: '35.812447',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 4,
    placeName: '리안',
    address: {
      address1: '대구',
      address2: '수성구',
      address3: '교학로4길 48',
    },
    category: '음식점',
    videos: [
      {
        influencerName: '아이유',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
    ],
    longitude: '128.65037738548736',
    latitude: '35.855300594743575',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 5,
    placeName: '유성카페1968',
    address: {
      address1: '대전',
      address2: '유성구',
      address3: '유성대로654번길 73',
    },
    category: '카페',
    videos: [
      {
        influencerName: '아이유',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
    ],
    longitude: '127.33301043369335',
    latitude: '36.351477682663756',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 6,
    placeName: '막동이회관',
    address: {
      address1: '광주',
      address2: '동구',
      address3: '남문로 614',
    },
    category: '카페',
    videos: [
      {
        videoUrl: '',
        influencerName: '풍자',
      },
    ],
    longitude: '126.931851791899',
    latitude: '35.1256604935401',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 7,
    placeName: '7맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-9',
    },
    category: '베트남식',
    videos: [
      {
        videoUrl: '',
        influencerName: '풍자',
      },
    ],
    longitude: '128.61999608',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 8,
    placeName: '1.5닭갈비',
    address: {
      address1: '강원특별자치도',
      address2: '춘천시',
      address3: '후만로 77',
    },
    category: '치킨',
    videos: [
      {
        influencerName: '카페ie',
        videoUrl: 'https://www.youtube.com/watch?v=nCEtQ7dP8zY',
      },
    ],
    longitude: '127.753131738285',
    latitude: '37.8763535115171',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 9,
    placeName: '맛있는초밥집 동백2호점 제목 개길면어떻게되나',
    address: {
      address1: '경기',
      address2: '용인시',
      address3: '기흥구 어정로 127',
    },
    category: '태국식',
    videos: [
      {
        influencerName: 'die',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
    ],
    longitude: '127.1436094525637',
    latitude: '37.275820090934495',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 10,
    placeName: '만우장',
    address: {
      address1: '부산',
      address2: '수영구',
      address3: '수영로594번길 28-2',
    },
    category: '인도식',
    videos: [
      {
        influencerName: '카페랩die',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
    ],
    longitude: '129.11483195198562',
    latitude: '35.158494192685914',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 11,
    placeName: '11맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '중식',
    videos: [
      {
        influencerName: '카페랩 ',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t',
      },
    ],
    longitude: '128.848643',
    latitude: '35.783343',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 12,
    placeName: '11맘스터치 대구현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '양식',
    videos: [
      {
        influencerName: '카ab. Maudie',
        videoUrl: 'https://www.youtube.com/watch?v=nCEtQ7dP8zY',
      },
    ],
    longitude: '128.848642',
    latitude: '35.7834',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 13,
    placeName: '11맘스터치 대점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '한식',
    videos: [
      {
        influencerName: '카e',
        videoUrl: 'https://www.youtube.com/watch?v=nCEtQ7dP8zY',
      },
    ],
    longitude: '128.8483',
    latitude: '35.789',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 14,
    placeName: '11맘스터치 대구대현',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '음식점',
    videos: [
      {
        influencerName: '. Maudie',
        videoUrl: 'https://www.youtube.com/watch?v=nCEtQ7dP8zY',
      },
    ],
    longitude: '128.84873',
    latitude: '35.789',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 15,
    placeName: '11맘스터치 현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '카페',
    videos: [
      {
        influencerName: '카페랩 ie',
        videoUrl: 'https://www.youtube.com/watch?v=nCEtQ7dP8zY',
      },
    ],
    longitude: '128.8499',
    latitude: '35.71',
    likes: false,
    menuImgUrl: null,
  },
  {
    placeId: 16,
    placeName: '11맘스터치 구점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '일식',
    videos: [
      {
        influencerName: '카페랩',
        videoUrl: 'https://www.youtube.com/watch?v=ZC5klhd08ME&t=6s',
      },
    ],
    longitude: '128.9',
    latitude: '35.78',
    likes: false,
    menuImgUrl: null,
  },
];

export const mapHandlers = [
  rest.get(`${BASE_URL}/places`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = dummyPlaces.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = dummyPlaces.slice(startIndex, endIndex);
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
    const { placeId, likes } = req.body as { placeId: string; likes: boolean };
    return res(
      ctx.status(200),
      ctx.json({
        placeId,
        likes,
      }),
    );
  }),
  rest.get(`${BASE_URL}/influencers/names`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json(dummyInfluencers));
  }),
  rest.get(`${BASE_URL}${getAllMarkersPath()}`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json(dummyAllMarkers));
  }),
  rest.get(`${BASE_URL}/places/marker/:id`, (req, res, ctx) => {
    const { id } = req.params;
    const data = dummyMarkerInfos[id.toString()];

    return res(ctx.status(200), ctx.json(data));
  }),
];

export default mapHandlers;
