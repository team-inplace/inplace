import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { PostingData } from '@/types';

export const postPostPath = () => `/posts`;
const postPost = async (data: PostingData) => {
  const response = await fetchInstance.post(postPostPath(), data, { withCredentials: true });
  return response.data;
};

export const usePostPost = () => {
  return useMutation({
    mutationFn: (data: PostingData) => postPost(data),
  });
};
