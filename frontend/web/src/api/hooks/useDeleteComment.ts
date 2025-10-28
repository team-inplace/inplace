import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

type DeleteCommentProps = {
  postId: string;
  id: string;
};
export const deleteCommentPath = ({ postId, id }: DeleteCommentProps) => `/posts/${postId}/comments/${id}`;
const deleteComment = async ({ postId, id }: DeleteCommentProps) => {
  const response = await getFetchInstance().delete(deleteCommentPath({ postId, id }), { withCredentials: true });
  return response.data;
};

export const useDeleteComment = () => {
  return useMutation({
    mutationFn: (deleteCommentInfo: DeleteCommentProps) => deleteComment(deleteCommentInfo),
  });
};
