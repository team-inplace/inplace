import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestReport } from '@/types';

export const postReportPostPath = () => `/reports/post`;
const postReportPost = async ({ id, reason }: RequestReport) => {
  const response = await getFetchInstance().post(
    postReportPostPath(),
    {
      id,
      reason,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostReportPost = () => {
  return useMutation({
    mutationFn: ({ id, reason }: RequestReport) => postReportPost({ id, reason }),
  });
};
