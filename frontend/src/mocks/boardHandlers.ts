import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';

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
    contentImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
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
    contentImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
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
    contentImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
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
    contentImgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
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
];
export default boardHandlers;
