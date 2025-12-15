import { rest } from 'msw';
import { getConfig } from '@inplace-frontend-monorepo/shared/api/config';
import { postCommentPath } from '@/api/hooks/usePostComment';
import { PostingData } from '@/types';
import { getPostDataPath } from '@/api/hooks/useGetPostData';
import { postPostPath } from '@/api/hooks/usePostPost';
import TestImg from '@/assets/images/titletest.png';
import { postReportCommentPath } from '@/api/hooks/usePostReportComment';
import { postReportPostPath } from '@/api/hooks/usePostReportPost';
import bronze from '@/assets/images/tier_b.webp';
import silver from '@/assets/images/tier_s.webp';
import gold from '@/assets/images/tier_g.webp';
import diamond from '@/assets/images/tier_d.webp';

const config = getConfig();
const BASE_URL = config.baseURL;

const detailDummy = {
  postId: 1,
  author: {
    nickname: '랄라스윗칩고구마',
    imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
    tierImageUrl: silver,
    badgeImageUrl: TestImg,
  },
  title: '성시경 먹을텐데 질문',
  content:
    '성시경 먹을텐데 시리즈 중에 제일 추천하는 식\n당 어디신가요~? 찐후기만 댓글 달아주세요성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요성시경 먹을텐데 시리즈 중에 제일 추천하여기ㅁㄴㅇㅁㅇㅁㄴ',
  totalLikeCount: 20,
  totalCommentCount: 3,
  createdAt: '2025.07.17',
  imageUrls: [
    {
      imageUrl:
        'https://www.chosun.com/resizer/v2/https%3A%2F%2Fauthor-service-images-prod-us-east-1.publishing.aws.arc.pub%2Fchosun%2F61ee5a7f-256c-441a-84d0-f71f7fde8753.png?auth=ac62f49ccb40ba0664e55e616e25d60bbe9491af26a5c0e5ac95e3640e0a3f6a&width=616&height=346&smart=true',
      hash: '1214',
    },
    {
      imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '121334',
    },
    {
      imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '231234',
    },
    {
      imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '121233',
    },
    {
      imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      hash: '14',
    },
  ],
  selfLike: true,
  isMine: true,
};
const postListDummy = [
  {
    postId: 1,
    author: {
      nickname: '랄라스윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: bronze,
      badgeImageUrl: TestImg,
    },
    title: '성',
    content: '성',
    totalLikeCount: 2,
    totalCommentCount: 3,
    createdAt: '25.07.17',
    imageUrl:
      'https://www.chosun.com/resizer/v2/https%3A%2F%2Fauthor-service-images-prod-us-east-1.publishing.aws.arc.pub%2Fchosun%2F61ee5a7f-256c-441a-84d0-f71f7fde8753.png?auth=ac62f49ccb40ba0664e55e616e25d60bbe9491af26a5c0e5ac95e3640e0a3f6a&width=616&height=346&smart=true',

    selfLike: false,
    isMine: true,
  },
  {
    postId: 2,
    author: {
      nickname: '룰라스윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: gold,
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    totalCommentCount: 3,
    createdAt: '2025.07.17',
    imageUrl:
      'https://www.chosun.com/resizer/v2/https%3A%2F%2Fauthor-service-images-prod-us-east-1.publishing.aws.arc.pub%2Fchosun%2F61ee5a7f-256c-441a-84d0-f71f7fde8753.png?auth=ac62f49ccb40ba0664e55e616e25d60bbe9491af26a5c0e5ac95e3640e0a3f6a&width=616&height=346&smart=true',
    isMine: false,
    selfLike: true,
  },
  {
    postId: 3,
    author: {
      nickname: '라라스윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: silver,
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    commentCount: 3,
    createdAt: '1분전',
    imageUrl:
      'https://www.chosun.com/resizer/v2/https%3A%2F%2Fauthor-service-images-prod-us-east-1.publishing.aws.arc.pub%2Fchosun%2F61ee5a7f-256c-441a-84d0-f71f7fde8753.png?auth=ac62f49ccb40ba0664e55e616e25d60bbe9491af26a5c0e5ac95e3640e0a3f6a&width=616&height=346&smart=true',

    isMine: false,
    selfLike: false,
  },
  {
    postId: 4,
    author: {
      nickname: '리리라스윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: diamond,
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    totalCommentCount: 3,
    createdAt: '1분전',
    isMine: false,
    selfLike: false,
  },
  {
    postId: 5,
    author: {
      nickname: '리칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    totalCommentCount: 3,
    createdAt: '1분전',
    isMine: false,
    selfLike: false,
  },
  {
    postId: 6,
    author: {
      nickname: '리윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    totalCommentCount: 3,
    createdAt: '1분전',
    isMine: false,
    selfLike: false,
  },
  {
    postId: 7,
    author: {
      nickname: '스윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    totalCommentCount: 3,
    createdAt: '1분전',
    isMine: false,
    selfLike: false,
  },
  {
    postId: 8,
    author: {
      nickname: '리라스윗칩',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
      badgeImageUrl: TestImg,
    },
    title: '성시경 먹을텐데 질문',
    content: '성시경 먹을텐데 시리즈 중에 제일 추천하는 식당 어디신가요~? 찐후기만 댓글 달아주세요',
    totalLikeCount: 20,
    totalCommentCount: 3,
    createdAt: '1분전',
    isMine: true,
    selfLike: false,
  },
];

const commentListDummy = [
  {
    commentId: 1,
    author: {
      nickname: '이효은',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content: '토스 커뮤니티에도 웹사이트가 잇어요 놀러오세요~^^',
    createdAt: '4분전',
    totalLikeCount: 20,
    selfLike: true,
    isMine: true,
  },
  {
    commentId: 2,
    author: {
      nickname: '엉웅이',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content: '<<@1:이효은>> 여긴 인플레이슨데 뭔헛소리노',
    createdAt: '3분전',
    totalLikeCount: 10,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 3,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 5,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 6,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content: '자 댓글 잘 이동하는지 확인해봅니다람쥐',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 7,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 8,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',

    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 9,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 10,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 11,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
  {
    commentId: 12,
    author: {
      nickname: '풍자',
      imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
      tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
    },
    content:
      '그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다ㅍ그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다그것참맛나겠다',
    createdAt: '1분전',
    totalLikeCount: 0,
    selfLike: false,
    isMine: false,
  },
];
export const postHandlers = [
  rest.get(`${BASE_URL}/posts`, (req, res, ctx) => {
    const url = new URL(req.url);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);
    const nextCursorId = url.searchParams.get('cursorId');

    let startIndex = 0;
    if (nextCursorId) {
      const cursorIndex = postListDummy.findIndex((post) => post.postId === parseInt(nextCursorId, 10));
      if (cursorIndex !== -1) {
        startIndex = cursorIndex + 1;
      }
    }

    const endIndex = Math.min(startIndex + size, postListDummy.length);
    const paginatedContent = postListDummy.slice(startIndex, endIndex);

    const hasNext = endIndex < postListDummy.length;
    const nextCursor = hasNext ? postListDummy[paginatedContent.length - 1]?.postId : null;

    return res(
      ctx.status(200),
      ctx.json({
        posts: paginatedContent,
        cursor: {
          hasNext,
          nextCursorId: nextCursor,
          nextCursorValue: nextCursor,
        },
      }),
    );
  }),
  rest.get(`${BASE_URL}${getPostDataPath('1')}`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json(detailDummy));
  }),
  rest.get(`${BASE_URL}/posts/1/comments`, (req, res, ctx) => {
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
  rest.post(`${BASE_URL}/posts/:postId/comments/likes`, (req, res, ctx) => {
    const { commentId, likes } = req.body as { commentId: number | string; likes: boolean };
    const targetId = Number(commentId);

    console.log(`📢 [MSW] 댓글 좋아요 요청 수신: ID ${targetId}, Likes: ${likes}`);

    const targetComment = commentListDummy.find((c) => c.commentId === targetId);

    if (targetComment) {
      targetComment.selfLike = likes;

      if (likes) {
        targetComment.totalLikeCount += 1;
      } else {
        targetComment.totalLikeCount = Math.max(0, targetComment.totalLikeCount - 1);
      }

      return res(ctx.status(200), ctx.json({ success: true }));
    }
    return res(ctx.status(404), ctx.json({ message: 'Comment not found' }));
  }),
  rest.post(`${BASE_URL}${postCommentPath('1')}`, async (req, res, ctx) => {
    const { comment } = req.body as { comment: string };
    const newComment = {
      commentId: 4,
      author: {
        nickname: 'test',
        imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
        tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
      },
      content: comment,
      createdAt: '1분전',
      totalLikeCount: 0,
      selfLike: false,
      isMine: true,
    };
    commentListDummy.push(newComment);
    return res(ctx.status(200), ctx.json(newComment));
  }),
  rest.post(`${BASE_URL}${postPostPath()}`, async (req, res, ctx) => {
    const { title, content } = req.body as PostingData;
    const newPost = {
      postId: 9,
      author: {
        nickname: 'test',
        imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEH9YJyZ8cIW7fXHzSw3N_PpYE6JFkcrUtKw&s',
        tierImageUrl: 'https://img.icons8.com/?size=100&id=12782&format=png&color=55ebff',
        badgeImageUrl: TestImg,
      },
      title,
      content,
      totalLikeCount: 0,
      totalCommentCount: 0,
      createdAt: '1분전',
      selfLike: false,
      isMine: true,
    };
    postListDummy.push(newPost);
    return res(ctx.status(200), ctx.json(newPost));
  }),

  rest.post(`${BASE_URL}${postReportCommentPath()}`, async (_, res, ctx) => {
    return res(ctx.status(200), ctx.json('success.'));
  }),
  rest.post(`${BASE_URL}${postReportPostPath()}`, async (_, res, ctx) => {
    return res(ctx.status(200), ctx.json('success.'));
  }),
  rest.post(`${BASE_URL}/posts/likes`, (req, res, ctx) => {
    const { postId, likes } = req.body as { postId: number | string; likes: boolean };
    const targetId = Number(postId);
    console.log(`📢 [MSW] 게시글 좋아요 요청: ID ${targetId}, Likes: ${likes}`);

    detailDummy.selfLike = likes;
    detailDummy.totalLikeCount = likes ? detailDummy.totalLikeCount + 1 : Math.max(0, detailDummy.totalLikeCount - 1);

    return res(ctx.status(200), ctx.json({ success: true }));
  }),

  rest.put(`${BASE_URL}/posts/1/comments/:commentId`, async (req, res, ctx) => {
    const { commentId } = req.params;
    const { comment } = req.body as { comment: string };

    const stringId = Array.isArray(commentId) ? commentId[0] : commentId;
    const numId = Number(stringId);
    const idx = commentListDummy.findIndex((c) => c.commentId === numId);

    commentListDummy[idx] = {
      ...commentListDummy[idx],
      content: comment,
      createdAt: '방금 전',
    };

    return res(ctx.status(200), ctx.json('success.'));
  }),

  rest.delete(`${BASE_URL}/posts/1/comments/:commentId`, async (req, res, ctx) => {
    const { commentId } = req.params;

    const stringId = Array.isArray(commentId) ? commentId[0] : commentId;
    const numId = Number(stringId);
    const idx = commentListDummy.findIndex((c) => c.commentId === numId);

    commentListDummy.splice(idx, 1);

    return res(ctx.status(200), ctx.json('success.'));
  }),
];
export default postHandlers;
