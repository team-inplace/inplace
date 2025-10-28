import { useInfiniteQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { FilterParams, PlaceData, PageableData } from '@/types';

export const getSearchPlaceList = async (
  filters: FilterParams,
  page: number,
  size: number,
): Promise<PageableData<PlaceData>> => {
  const { categories, influencers, placeName = '' } = filters;

  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    categories: categories.join(','),
    influencers: influencers.join(','),
    placeName: placeName.toString(),
  });

  const response = await getFetchInstance().get<PageableData<PlaceData>>(`/places/search?${params}`);
  return response.data;
};

interface QueryParams {
  filters: FilterParams;
  size: number;
}

export const useGetInfiniteSearchPlaceList = ({ filters, size }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    PageableData<PlaceData>,
    Error,
    { pages: PageableData<PlaceData>[]; pageParams: number[] },
    [string, FilterParams, number],
    number
  >({
    queryKey: ['infiniteSearchPlaceList', filters, size],
    queryFn: ({ pageParam = 0 }) => getSearchPlaceList(filters, pageParam, size),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
