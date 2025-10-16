import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { PostingData } from '@/types';

interface PutPostProps {
  postId: string;
  formData: PostingData;
}
export const putPostPath = (postId: string) => `/posts/${postId}`;

const putPost = async ({ postId, formData }: PutPostProps) => {
  const response = await fetchInstance.put(putPostPath(postId), formData, { withCredentials: true });
  return response.data;
};

export const usePutPost = () => {
  return useMutation({
    mutationFn: (putPostData: PutPostProps) => putPost(putPostData),
  });
};
