import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { PageableData, PlaceData, RequestPlaceLike, UserPlaceData } from '@/types';

interface InfiniteQueryData {
  pages: PageableData<UserPlaceData>[];
  pageParams: number[];
}

export const postPlaceLikePath = () => `/places/likes`;
const postPlaceLike = async ({ placeId, likes }: RequestPlaceLike) => {
  const response = await getFetchInstance().post(
    postPlaceLikePath(),
    {
      placeId,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostPlaceLike = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: postPlaceLike,

    onMutate: async ({ placeId, likes }) => {
      await queryClient.cancelQueries({ queryKey: ['UserPlace'] });
      await queryClient.cancelQueries({ queryKey: ['placeInfo', String(placeId)] });
      await queryClient.cancelQueries({ queryKey: ['placeList'] });

      const prevUserPlace = queryClient.getQueryData<PageableData<UserPlaceData>>(['UserPlace']);
      const prevPlaceInfo = queryClient.getQueryData<PlaceData>(['placeInfo', String(placeId)]);
      const prevPlaceList = queryClient.getQueryData<PageableData<PlaceData>>(['placeList']);

      queryClient.setQueriesData({ queryKey: ['UserPlace'] }, (oldData: InfiniteQueryData | undefined) => {
        if (!oldData) return oldData;

        return {
          ...oldData,
          pages: oldData.pages.map((page) => ({
            ...page,
            content: page.content.map((place) => {
              if (place.placeId === Number(placeId)) {
                return { ...place, likes };
              }
              return place;
            }),
          })),
        };
      });

      queryClient.setQueryData<PlaceData>(['placeInfo', String(placeId)], (old) => {
        if (!old) return undefined;
        return {
          ...old,
          likes,
          likedCount: likes ? old.likedCount + 1 : Math.max(0, old.likedCount - 1),
        };
      });

      queryClient.setQueriesData({ queryKey: ['placeList'] }, (oldData: InfiniteQueryData | undefined) => {
        if (!oldData) return oldData;

        return {
          ...oldData,
          pages: oldData.pages.map((page) => ({
            ...page,
            content: page.content.map((place) => {
              if (place.placeId === Number(placeId)) {
                return { ...place, likes };
              }
              return place;
            }),
          })),
        };
      });

      return { prevUserPlace, prevPlaceInfo, prevPlaceList };
    },

    onError: (_err, { placeId }, context) => {
      if (context) {
        queryClient.setQueryData(['UserPlace'], context.prevUserPlace);
        queryClient.setQueryData(['placeInfo', String(placeId)], context.prevPlaceInfo);
        queryClient.setQueryData(['placeList'], context.prevPlaceList);
      }
      alert('좋아요 처리에 실패했습니다.');
    },

    onSettled: (_data, _err, { placeId }) => {
      queryClient.invalidateQueries({ queryKey: ['placeInfo', String(placeId)] });
    },
  });
};
