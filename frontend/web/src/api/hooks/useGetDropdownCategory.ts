import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { CategoryData, CategoryOption } from '@/types';

const useGetDropdownCategory = () => {
  return useSuspenseQuery({
    queryKey: ['categoryName'],
    queryFn: async () => {
      const { data } = await fetchInstance.get<CategoryData>('/places/categories');
      const options: CategoryOption[] = data.categories.flatMap((category) => {
        const mainOption: CategoryOption = {
          label: category.name,
          id: category.id,
          isMain: true,
        };

        const allOption: CategoryOption = {
          label: '전체',
          id: category.id,
          isMain: false,
          mainId: category.id,
        };

        const subOptions: CategoryOption[] = category.subCategories.map((subCategory) => ({
          label: subCategory.name,
          id: subCategory.id,
          isMain: false,
          mainId: category.id,
        }));

        return [mainOption, allOption, ...subOptions];
      });
      return options;
    },
    staleTime: 1000 * 60 * 5,
  });
};

export default useGetDropdownCategory;
