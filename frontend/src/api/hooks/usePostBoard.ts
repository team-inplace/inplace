import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { BoardData } from '@/types';

export const postBoardPath = () => `/board`;
const postBoard = async (data: BoardData) => {
  const response = await fetchInstance.post(
    postBoardPath(),
    {
      data,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostBoard = () => {
  return useMutation({
    mutationFn: (data: BoardData) => postBoard(data),
  });
};
