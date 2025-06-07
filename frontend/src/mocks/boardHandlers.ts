import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { getBoardDataPath } from '@/api/hooks/useGetBoardData';
import { postCommentPath } from '@/api/hooks/usePostComment';
import { BoardPostData, PostCommentProps } from '@/types';
import { postBoardPath } from '@/api/hooks/usePostBoard';

const boardListDummy = [
  {
    boardId: 1,
    userNickname: '랄라스윗칩',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    like: 20,
    comment: 3,
    create: '1분전',
    imgUrls: {
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '1234',
    },
    likes: true,
  },
  {
    boardId: 2,
    userNickname: '룰라스윗칩',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    like: 20,
    comment: 3,
    create: '1분전',
    imgUrls: {
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '1234',
    },
    likes: true,
  },
  {
    boardId: 3,
    userNickname: '럴라스윗칩',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    like: 20,
    comment: 3,
    create: '1분전',
    imgUrls: {
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '1234',
    },
    likes: false,
  },
  {
    boardId: 4,
    userNickname: '롤라스윗칩',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    like: 20,
    comment: 3,
    create: '1분전',
    imgUrls: {
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '1234',
    },
    likes: false,
  },
];

const commentListDummy = [
  {
    commentId: 1,
    userNickname: '랄라스윗칩',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    content: '토스 커뮤니티에도 웹사이트가 잇어요 놀러오세요~^^',
    create: '4분전',
    like: 20,
    likes: true,
  },
  {
    commentId: 2,
    userNickname: '엉웅이',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    content: '@랄라스윗칩 여긴 인플레이슨데 뭔헛소리노',
    create: '3분전',
    like: 10,
    likes: false,
  },
  {
    commentId: 3,
    userNickname: '풍자',
    userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    create: '1분전',
    like: 0,
    likes: false,
  },
];
export const boardHandlers = [
  rest.get(`${BASE_URL}/board`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = boardListDummy.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = boardListDummy.slice(startIndex, endIndex);

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
  rest.get(`${BASE_URL}${getBoardDataPath('1')}`, (_, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        boardId: 1,
        userNickname: '랄라스윗칩',
        userImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
        title: '성시경 먹을텐데 질문',
        content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
        like: 20,
        comment: 3,
        create: '1분전',
        imgUrls: [
          {
            imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
            hash: '1234',
          },
        ],
        likes: true,
      }),
    );
  }),
  rest.get(`${BASE_URL}/board/1/comment`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = commentListDummy.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = commentListDummy.slice(startIndex, endIndex);

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
  rest.post(`${BASE_URL}${postCommentPath('1')}`, async (req, res, ctx) => {
    const { boardId, comment } = req.body as PostCommentProps;
    return res(
      ctx.status(200),
      ctx.json({
        boardId,
        comment,
      }),
    );
  }),
  rest.post(`${BASE_URL}${postBoardPath()}`, async (req, res, ctx) => {
    const { title, content, imgUrls } = req.body as BoardPostData;
    return res(
      ctx.status(200),
      ctx.json({
        title,
        content,
        imgUrls,
      }),
    );
  }),
];
export default boardHandlers;
