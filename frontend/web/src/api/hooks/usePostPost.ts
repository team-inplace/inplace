import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PostingData } from '@/types';

export const postPostPath = () => `/posts`;
const postPost = async (data: PostingData) => {
  const response = await getFetchInstance().post(postPostPath(), data, { withCredentials: true });
  return response.data;
};

export const usePostPost = () => {
  return useMutation({
    mutationFn: (data: PostingData) => postPost(data),
  });
};
