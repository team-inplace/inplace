import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { PlaceData, RequestPlaceLike } from '@/types';

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

      const prevUserPlace = queryClient.getQueryData(['UserPlace']);
      const prevPlaceInfo = queryClient.getQueryData(['placeInfo', String(placeId)]);

      queryClient.setQueryData(['UserPlace'], (old: PlaceData[] | undefined) => {
        if (!old) return [];
        return old.map((place) =>
          place.placeId === placeId
            ? {
                ...place,
                likes,
                likedCount: likes ? place.likedCount + 1 : Math.max(0, place.likedCount - 1),
              }
            : place,
        );
      });

      queryClient.setQueryData(['placeInfo', String(placeId)], (old: any) => {
        if (!old) return old;
        return {
          ...old,
          likes,
          likedCount: likes ? old.likedCount + 1 : Math.max(0, old.likedCount - 1),
        };
      });

      return { prevUserPlace, prevPlaceInfo };
    },

    onError: (_err, { placeId }, context) => {
      if (context) {
        queryClient.setQueryData(['UserPlace'], context.prevUserPlace);
        queryClient.setQueryData(['placeInfo', String(placeId)], context.prevPlaceInfo);
      }
      alert('좋아요 처리에 실패했습니다.');
    },

    onSettled: (_data, _err, { placeId }) => {
      queryClient.invalidateQueries({ queryKey: ['UserPlace'] });
      queryClient.invalidateQueries({ queryKey: ['placeInfo', String(placeId)] });
    },
  });
};
