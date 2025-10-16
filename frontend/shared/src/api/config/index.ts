type Environment = "production" | "development";

interface Config {
  baseURL: string;
  environment: Environment;
}

const DEFAULT_CONFIGS: Record<Environment, Config> = {
  production: {
    baseURL: "https://api.inplace.my",
    environment: "production",
  },
  development: {
    baseURL: "https://a7b2c3d4-dev.inplace.my:444",
    environment: "development",
  },
};

let currentConfig: Config | null = null;

/**
 * 앱/웹 시작 시점에 환경 설정을 초기화하는 함수
 * @param environment 'development' 또는 'production'
 */
export const initializeConfig = (environment: Environment) => {
  if (process.env.NODE_ENV === "test") {
    currentConfig = DEFAULT_CONFIGS.development;
    return;
  }
  currentConfig = DEFAULT_CONFIGS[environment];
};

export const getConfig = (): Config => {
  if (!currentConfig) {
    throw new Error(
      "Config has not been initialized. Please call initializeConfig first."
    );
  }
  return currentConfig;
};
