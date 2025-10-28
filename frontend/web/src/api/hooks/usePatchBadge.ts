import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';

export const patchBadgePath = () => `/users/main-badge`;
const patchBadge = async (badgeId: number) => {
  const response = await fetchInstance.patch(`${patchBadgePath()}`, null, {
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
