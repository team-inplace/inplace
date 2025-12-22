import { useMemo, useEffect } from 'react';
import { CursorData, PlaceData } from '@/types';

type PlaceCursorParam = { cursorId: number; cursorValue: number } | null;

export interface PlaceListProps {
  data:
    | {
        pages: CursorData<PlaceData>[];
        pageParams: PlaceCursorParam[];
      }
    | undefined;
  onGetPlaceData: (data: PlaceData[]) => void;
}
export default function usePlaceList({ data, onGetPlaceData }: PlaceListProps) {
  const filteredPlaces = useMemo(() => {
    return data?.pages?.flatMap((page) => page.contents) ?? [];
  }, [data]);

  useEffect(() => {
    if (data?.pages) {
      onGetPlaceData(filteredPlaces);
    }
  }, [data, filteredPlaces, onGetPlaceData]);

  return { filteredPlaces };
}
