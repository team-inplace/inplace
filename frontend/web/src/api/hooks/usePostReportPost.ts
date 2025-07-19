import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestReport } from '@/types';

export const postReportPostPath = () => `/reports/post`;
const postReportPost = async ({ id, reason }: RequestReport) => {
  const response = await fetchInstance.post(
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
