import { useQuery } from '@tanstack/react-query';
import axios from 'axios';

export const getSearchKakaoKeywordPath = () => 'https://dapi.kakao.com/v2/local/search/keyword';

export const fetchSearchKakaoKeyword = async (query: string) => {
  if (!query) return null;

  try {
    const response = await axios.get(getSearchKakaoKeywordPath(), {
      params: { query },
      headers: {
        Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_KEY}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching Kakao search keyword:', error);
    return null;
  }
};

export const useGetSearchKakaoKeyword = (query: string) => {
  return useQuery({
    queryKey: ['kakaoKeyword', query],
    queryFn: () => fetchSearchKakaoKeyword(query),
    enabled: !!query,
    staleTime: 1000 * 60 * 5,
  });
};
