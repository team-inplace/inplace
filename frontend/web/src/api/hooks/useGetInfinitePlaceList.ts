import { useInfiniteQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { LocationData, FilterParams, PlaceData, CursorData } from '@/types';

export const getPlaceList = async (
  location: LocationData,
  filters: FilterParams,
  center: { lat: number; lng: number },
  cursorId: number | null,
  cursorValue: number | null,
  size: number,
  sort: string,
): Promise<CursorData<PlaceData>> => {
  const { topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude } = location;
  const { categories, influencers } = filters;

  const params = new URLSearchParams({
    topLeftLongitude: topLeftLongitude.toString(),
    topLeftLatitude: topLeftLatitude.toString(),
    bottomRightLongitude: bottomRightLongitude.toString(),
    bottomRightLatitude: bottomRightLatitude.toString(),
    longitude: center.lng.toString(),
    latitude: center.lat.toString(),
    size: size.toString(),
    sort: sort.toString(),
    categories: categories.join(','),
    influencers: influencers.join(','),
  });

  if (cursorId) {
    params.append('cursorId', cursorId.toString());
  }
  if (cursorValue) {
    params.append('cursorValue', cursorValue.toString());
  }

  const response = await getFetchInstance().get<CursorData<PlaceData>>(`/places?${params}`);
  return response.data;
};

interface QueryParams {
  location: LocationData;
  filters: FilterParams;
  center: { lat: number; lng: number };
  size: number;
  sort: string;
}

export const useGetInfinitePlaceList = ({ location, filters, center, size, sort }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    CursorData<PlaceData>,
    Error,
    { pages: CursorData<PlaceData>[]; pageParams: ({ cursorId: number; cursorValue: number } | null)[] },
    [string, LocationData, FilterParams, { lat: number; lng: number }, number, string],
    { cursorId: number; cursorValue: number } | null
  >({
    queryKey: ['infinitePlaceList', location, filters, center, size, sort],

    queryFn: ({ pageParam = null }) =>
      getPlaceList(location, filters, center, pageParam?.cursorId || null, pageParam?.cursorValue || null, size, sort),

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
