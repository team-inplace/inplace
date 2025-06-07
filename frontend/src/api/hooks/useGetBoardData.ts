import { useSuspenseQuery } from '@tanstack/react-query';
import { BoardData } from '@/types';
import { fetchInstance } from '../instance';

export const getBoardDataPath = (id: string) => `/board/detail/${id}`;
export const getBoardData = async (id: string) => {
  const response = await fetchInstance.get<BoardData>(getBoardDataPath(id), { withCredentials: true });
  return response.data;
};
export const useGetBoardData = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['BoardData', id],
    queryFn: () => getBoardData(id),
    staleTime: 1000 * 60 * 5,
  });
};
