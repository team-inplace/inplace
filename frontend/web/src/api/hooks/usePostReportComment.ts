import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestReport } from '@/types';

export const postReportCommentPath = () => `/reports/comment`;
const postReportComment = async ({ id, reason }: RequestReport) => {
  const response = await getFetchInstance().post(
    postReportCommentPath(),
    {
      id,
      reason,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostReportComment = () => {
  return useMutation({
    mutationFn: ({ id, reason }: RequestReport) => postReportComment({ id, reason }),
  });
};
