import { defineConfig, loadEnv } from 'vite';
import tsconfigPaths from 'vite-tsconfig-paths';
import { sentryVitePlugin } from '@sentry/vite-plugin';
import { visualizer } from 'rollup-plugin-visualizer';
import { VitePluginRadar } from 'vite-plugin-radar';

import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());
  return {
    base: '/',
    plugins: [
      react(),
      tsconfigPaths(),
      sentryVitePlugin({
        org: 'inplace-na',
        project: 'inplace',
        authToken: env.VITE_SENTRY_AUTH_TOKEN,
        sourcemaps: {
          filesToDeleteAfterUpload: '**/*.map',
        },
      }),
      visualizer({ open: true }),
      VitePluginRadar({
        analytics: {
          id: env.VITE_GOOGLE_ANALYTICS,
        },
      }),
    ],
    build: {
      minify: 'esbuild',
      rollupOptions: {
        treeshake: true,
      },
      sourcemap: mode === 'production',
    },
  };
});
