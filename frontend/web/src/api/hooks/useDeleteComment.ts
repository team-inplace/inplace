import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';

type DeleteCommentProps = {
  postId: string;
  id: string;
};
export const deleteCommentPath = ({ postId, id }: DeleteCommentProps) => `/posts/${postId}/comments/${id}`;
const deleteComment = async ({ postId, id }: DeleteCommentProps) => {
  const response = await fetchInstance.delete(deleteCommentPath({ postId, id }), { withCredentials: true });
  return response.data;
};

export const useDeleteComment = () => {
  return useMutation({
    mutationFn: (deleteCommentInfo: DeleteCommentProps) => deleteComment(deleteCommentInfo),
  });
};
