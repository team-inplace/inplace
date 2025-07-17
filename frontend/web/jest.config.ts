/**
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/configuration
 */
import type { Config } from 'jest';

const config: Config = {
  testEnvironment: 'jsdom',
  preset: 'ts-jest',
  clearMocks: true,
  collectCoverage: true,
  coverageDirectory: 'coverage',
  coverageProvider: 'v8',
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
  },
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  transform: {
    '^.+\\.tsx?$': [
      'ts-jest',
      {
        diagnostics: {
          ignoreCodes: [1343],
        },
        astTransformers: {
          before: [
            {
              path: require.resolve('ts-jest-mock-import-meta'),
              options: {
                metaObjectReplacement: {
                  env: {
                    MODE: 'test',
                    NODE_ENV: 'test',
                    DEV: false,
                    PROD: false,
                    BASE_URL: '/',
                  },
                },
              },
            },
          ],
        },
      },
    ],
    '.+\\.(css|styl|less|sass|scss|webp|png|jpg|svg|ttf|woff|woff2)$': 'jest-transform-stub',
  },
};

export default config;
