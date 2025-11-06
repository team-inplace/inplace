import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

interface PutCommentProps {
  postId: string;
  commentId: string;
  comment: string;
}
export const putCommentPath = (postId: string, commentId: string) => `/posts/${postId}/comments/${commentId}`;

const putComment = async ({ postId, commentId, comment }: PutCommentProps) => {
  const response = await getFetchInstance().put(
    putCommentPath(postId, commentId),
    { comment },
    { withCredentials: true },
  );
  return response.data;
};

export const usePutComment = () => {
  return useMutation({
    mutationFn: (putCommentData: PutCommentProps) => putComment(putCommentData),
  });
};
