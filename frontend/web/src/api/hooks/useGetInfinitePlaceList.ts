import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { LocationData, FilterParams, PlaceData, PageableData } from '@/types';

export const getPlaceList = async (
  location: LocationData,
  filters: FilterParams,
  center: { lat: number; lng: number },
  page: number,
  size: number,
): Promise<PageableData<PlaceData>> => {
  const { topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude } = location;
  const { categories, influencers } = filters;

  const params = new URLSearchParams({
    topLeftLongitude: topLeftLongitude.toString(),
    topLeftLatitude: topLeftLatitude.toString(),
    bottomRightLongitude: bottomRightLongitude.toString(),
    bottomRightLatitude: bottomRightLatitude.toString(),
    longitude: center.lng.toString(),
    latitude: center.lat.toString(),
    page: page.toString(),
    size: size.toString(),
    categories: categories.join(','),
    influencers: influencers.join(','),
  });

  const response = await fetchInstance.get<PageableData<PlaceData>>(`/places?${params}`);
  return response.data;
};

interface QueryParams {
  location: LocationData;
  filters: FilterParams;
  center: { lat: number; lng: number };
  size: number;
}

export const useGetInfinitePlaceList = ({ location, filters, center, size }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    PageableData<PlaceData>,
    Error,
    { pages: PageableData<PlaceData>[]; pageParams: number[] },
    [string, LocationData, FilterParams, { lat: number; lng: number }, number],
    number
  >({
    queryKey: ['infinitePlaceList', location, filters, center, size],
    // pageParam = 각 페이지를 가져올 때마다 사용되는 현재 페이지 번호
    queryFn: ({ pageParam = 0 }) => getPlaceList(location, filters, center, pageParam, size),
    initialPageParam: 0, // 초기 페이지 번호(0부터 시작)
    // 다음페이지 있으면 다음 페이지 번호 반환
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
