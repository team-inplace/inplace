import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { BoardPostData } from '@/types';

interface PutBoardProps {
  boardId: string;
  formData: BoardPostData;
}
export const putBoardPath = (boardId: string) => `/board/${boardId}`;

const putBoard = async ({ boardId, formData }: PutBoardProps) => {
  const response = await fetchInstance.put(putBoardPath(boardId), { formData }, { withCredentials: true });
  return response.data;
};

export const usePutBoard = () => {
  return useMutation({
    mutationFn: (putBoardPostData: PutBoardProps) => putBoard(putBoardPostData),
  });
};
