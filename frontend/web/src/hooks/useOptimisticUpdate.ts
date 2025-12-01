import { useMutation, useQueryClient, QueryKey } from '@tanstack/react-query';

interface OptimisticConfig<TData, TVariables> {
  mutationFn: (variables: TVariables) => Promise<unknown>;
  queryKey: QueryKey;
  updater: (oldData: TData | undefined, variables: TVariables) => TData | undefined;
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
      const previousData = queryClient.getQueryData<TData>(queryKey);

      queryClient.setQueryData<TData>(queryKey, (oldData) => {
        return updater(oldData, variables);
      });
      return { previousData };
    },

    onError: (err, variables, context) => {
      if (context?.previousData) {
        queryClient.setQueryData<TData>(queryKey, context.previousData);
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
