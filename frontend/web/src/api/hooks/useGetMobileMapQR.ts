import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const getMobileMapQRPath = () => `/qrcodes`;
export const getMobileMapQR = async (placeId: number, width: number, height: number) => {
  const params = new URLSearchParams({
    placeId: placeId.toString(),
    width: width.toString(),
    height: height.toString(),
  });
  const response = await fetchInstance.get(`${getMobileMapQRPath()}?${params}`, {
    responseType: 'arraybuffer',
  });
  return new Blob([response.data], { type: 'image/png' });
};
export const useGetMobileMapQR = (placeId: number, width: number, height: number) => {
  return useSuspenseQuery({
    queryKey: ['MobileMapQR', placeId],
    queryFn: () => getMobileMapQR(placeId, width, height),
    staleTime: 1000 * 60 * 5,
  });
};
