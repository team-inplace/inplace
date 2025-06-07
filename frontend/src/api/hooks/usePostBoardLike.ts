import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestBoardLike } from '@/types';

export const postBoardLikePath = () => `/board/likes`;
const postBoardLike = async ({ boardId, likes }: RequestBoardLike) => {
  const response = await fetchInstance.post(
    postBoardLikePath(),
    {
      boardId,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostBoardLike = () => {
  return useMutation({
    mutationFn: ({ boardId, likes }: RequestBoardLike) => postBoardLike({ boardId, likes }),
  });
};
