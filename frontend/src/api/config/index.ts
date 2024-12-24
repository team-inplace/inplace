type Environment = 'production' | 'development';

interface Config {
  baseURL: string;
}

const DEFAULT_CONFIGS: Record<Environment, Config> = {
  production: {
    baseURL: 'https://api.inplace.my',
  },
  development: {
    baseURL: 'https://43.203.207.183.nip.io',
  },
} as const;

const getCurrentConfig = (): Config => {
  const currentMode = import.meta.env.MODE as Environment;
  return DEFAULT_CONFIGS[currentMode];
};

export default getCurrentConfig;
