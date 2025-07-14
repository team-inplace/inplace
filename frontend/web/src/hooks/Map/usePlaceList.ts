import { useMemo, useEffect } from 'react';
import { PageableData, PlaceData } from '@/types';

export interface PlaceListProps {
  data:
    | {
        pages: PageableData<PlaceData>[];
        pageParams: number[];
      }
    | undefined;
  onGetPlaceData: (data: PlaceData[]) => void;
}
export default function usePlaceList({ data, onGetPlaceData }: PlaceListProps) {
  const filteredPlaces = useMemo(() => {
    return data?.pages?.flatMap((page) => page.content) || [];
  }, [data]);

  useEffect(() => {
    if (data?.pages) {
      onGetPlaceData(filteredPlaces);
    }
  }, [data, filteredPlaces, onGetPlaceData]);

  return { filteredPlaces };
}
