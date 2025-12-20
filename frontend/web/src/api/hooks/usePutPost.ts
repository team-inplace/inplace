import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PostingData } from '@/types';

interface PutPostProps {
  postId: string;
  formData: PostingData;
}
export const putPostPath = (postId: string) => `/posts/${postId}`;

const putPost = async ({ postId, formData }: PutPostProps) => {
  const response = await getFetchInstance().put(putPostPath(postId), formData, { withCredentials: true });
  return response.data;
};

export const usePutPost = () => {
  return useMutation({
    mutationFn: (putPostData: PutPostProps) => putPost(putPostData),
  });
};
