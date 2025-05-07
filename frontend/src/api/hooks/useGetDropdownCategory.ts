import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { CategoryData, CategoryOption } from '@/types';

const useGetDropdownCategory = () => {
  return useSuspenseQuery({
    queryKey: ['categoryName'],
    queryFn: async () => {
      const { data } = await fetchInstance.get<CategoryData>('/category/names');
      const options: CategoryOption[] = data.categories.flatMap((category) => {
        const mainOption: CategoryOption = {
          label: category.name,
          id: category.id,
          isMain: true,
        };
        const subOptions: CategoryOption[] = category.subCategories.map((subCategory) => ({
          label: subCategory.name,
          id: subCategory.id,
          isMain: false,
          mainId: category.id,
        }));
        return [mainOption, ...subOptions];
      });
      return options;
    },
    staleTime: 1000 * 60 * 5,
  });
};

export default useGetDropdownCategory;
