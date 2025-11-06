import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const patchBadgePath = () => `/users/main-badge`;
const patchBadge = async (badgeId: number) => {
  const response = await getFetchInstance().patch(`${patchBadgePath()}`, null, {
    params: {
      id: badgeId,
    },
    withCredentials: true,
  });
  return response.data;
};

export const usePatchBadge = () => {
  return useMutation({
    mutationFn: (badgeId: number) => patchBadge(badgeId),
  });
};
