import { useInfiniteQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { FilterParams, PlaceData, CursorData } from '@/types';

export const getSearchPlaceList = async (
  filters: FilterParams,
  cursorId: number | null,
  cursorValue: number | null,
  size: number,
  sort: string,
): Promise<CursorData<PlaceData>> => {
  const { categories, influencers, placeName = '' } = filters;

  const params = new URLSearchParams({
    size: size.toString(),
    sort: sort.toString(),
    categories: categories.join(','),
    influencers: influencers.join(','),
    placeName: placeName.toString(),
  });

  if (cursorId) {
    params.append('cursorId', cursorId.toString());
  }
  if (cursorValue) {
    params.append('cursorValue', cursorValue.toString());
  }

  const response = await getFetchInstance().get<CursorData<PlaceData>>(`/places/search?${params}`);
  return response.data;
};

interface QueryParams {
  filters: FilterParams;
  size: number;
  sort: string;
}

export const useGetInfiniteSearchPlaceList = ({ filters, size, sort }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    CursorData<PlaceData>,
    Error,
    { pages: CursorData<PlaceData>[]; pageParams: ({ cursorId: number; cursorValue: number } | null)[] },
    [string, FilterParams, number, string],
    { cursorId: number; cursorValue: number } | null
  >({
    queryKey: ['infiniteSearchPlaceList', filters, size, sort],
    queryFn: ({ pageParam = null }) =>
      getSearchPlaceList(filters, pageParam?.cursorId || null, pageParam?.cursorValue || null, size, sort),
    initialPageParam: null,
    getNextPageParam: (lastPage) => {
      return lastPage.cursor.hasNext
        ? { cursorId: lastPage.cursor.nextCursorId, cursorValue: lastPage.cursor.nextCursorValue }
        : undefined;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
