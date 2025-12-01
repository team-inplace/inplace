import { useMutation, useQueryClient, QueryKey } from '@tanstack/react-query';

interface OptimisticConfig<TData, TVariables> {
  mutationFn: (variables: TVariables) => Promise<TData>;
  queryKey: QueryKey;
  updater: (oldData: any, variables: TVariables) => any;
  invalidates?: QueryKey[];
}

export default function useOptimisticUpdate<TData, TVariables>({
  mutationFn,
  queryKey,
  updater,
  invalidates,
}: OptimisticConfig<TData, TVariables>) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn,

    onMutate: async (variables) => {
      await queryClient.cancelQueries({ queryKey });
      const previousData = queryClient.getQueryData(queryKey);

      queryClient.setQueryData(queryKey, (oldData: any) => {
        return updater(oldData, variables);
      });
      return { previousData };
    },

    onError: (err, variables, context) => {
      if (context?.previousData) {
        queryClient.setQueryData(queryKey, context.previousData);
      }
      alert('좋아요 처리에 실패했습니다.');
    },

    onSettled: () => {
      const keysToInvalidate = invalidates ? [queryKey, ...invalidates] : [queryKey];
      keysToInvalidate.forEach((key) => {
        queryClient.invalidateQueries({ queryKey: key });
      });
    },
  });
}
