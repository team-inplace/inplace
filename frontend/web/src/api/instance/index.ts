import type { AxiosInstance, AxiosRequestConfig } from 'axios';
import axios from 'axios';

import { QueryCache, QueryClient } from '@tanstack/react-query';
import getCurrentConfig from '../config';
import captureErrorWithRetry from '@/libs/Sentry/captureErrorWithRetry';

const initInstance = (config: AxiosRequestConfig): AxiosInstance => {
  const DEFAULT_AXIOS_TIMEOUT_MS = 20_000;
  const NON_REPORTING_STATUS_CODES = [401, 403, 409];

  const instance = axios.create({
    timeout: DEFAULT_AXIOS_TIMEOUT_MS,
    ...config,
    headers: {
      'Content-Type': 'application/json',
      ...config.headers,
    },
  });
  instance.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (!error.response) {
        const requestUrl = error.config?.url || 'URL 정보 없음';
        await captureErrorWithRetry(`[Network Error] ${requestUrl} \n${error.message ?? '네트워크 오류'}`, {
          tags: { 'error type': 'Network Error' },
          level: 'error',
        });
        return Promise.reject(error);
      }

      // 예기치 못한 4~500번대 오류 로깅
      if (error.response.status >= 400 && !NON_REPORTING_STATUS_CODES.includes(error.response.status)) {
        const isServerError = error.response.status >= 500;
        const errorType = isServerError ? 'Server Error' : 'Api Error';
        await captureErrorWithRetry(`[${errorType}] ${error.config.url} \n${error.message}`, {
          tags: { 'error type': errorType },
          level: 'error',
        });
      }
      return Promise.reject(error);
    },
  );
  return instance;
};

export const BASE_URL = getCurrentConfig().baseURL;
export const fetchInstance = initInstance({
  baseURL: BASE_URL,
});

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnMount: true,
      refetchOnReconnect: true,
      refetchOnWindowFocus: true,
      throwOnError: true,
    },
    mutations: {
      onError: (error) => {
        captureErrorWithRetry(error, {
          tags: { type: 'mutation_error' },
        });
      },
      throwOnError: true,
    },
  },
  queryCache: new QueryCache({
    onError: (error) => {
      captureErrorWithRetry(error);
    },
  }),
});
