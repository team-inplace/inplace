import { useSuspenseQuery } from '@tanstack/react-query';
import { BoardListData } from '@/types';
import { fetchInstance } from '../instance';

export const getBoardDataPath = (id: string) => `/board/detail/${id}`;
export const getBoardData = async (id: string) => {
  const response = await fetchInstance.get<BoardListData>(getBoardDataPath(id));
  return response.data;
};
export const useGetBoardData = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['BoardData', id],
    queryFn: () => getBoardData(id),
    staleTime: 1000 * 60 * 5,
  });
};
