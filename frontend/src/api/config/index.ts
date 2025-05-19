type Environment = 'production' | 'development';

interface Config {
  baseURL: string;
  environment: Environment;
}

const DEFAULT_CONFIGS: Record<Environment, Config> = {
  production: {
    baseURL: 'https://api.inplace.my',
    environment: 'production',
  },
  development: {
    baseURL: 'https://a7b2c3d4-dev.inplace.my',
    environment: 'development',
  },
} as const;

const getCurrentConfig = (): Config => {
  // Jest 테스트 환경 체크
  if (process.env.NODE_ENV === 'test') {
    return DEFAULT_CONFIGS.development;
  }

  // Vite 환경용 (빌드 시점에 교체됨)
  const currentMode = import.meta.env.MODE as Environment;
  return DEFAULT_CONFIGS[currentMode];
};

export default getCurrentConfig;
