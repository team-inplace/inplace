import { defineConfig, loadEnv } from 'vite';
import tsconfigPaths from 'vite-tsconfig-paths';
import { sentryVitePlugin } from '@sentry/vite-plugin';
import { visualizer } from 'rollup-plugin-visualizer';
import { VitePluginRadar } from 'vite-plugin-radar';
import fs from 'fs';
import path from 'path';

import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());
  const injectEnvToServiceWorker = () => ({
    name: 'inject-env-to-sw',
    apply: 'build' as const,
    closeBundle() {
      const distSwPath = path.resolve(__dirname, 'dist/firebase-messaging-sw.js');
      const publicSwPath = path.resolve(__dirname, 'public/firebase-messaging-sw.js');

      if (fs.existsSync(publicSwPath)) {
        let swContent = fs.readFileSync(publicSwPath, 'utf-8');

        swContent = swContent
          .replace(/import\.meta\.env\.VITE_FIREBASE_API_KEY/g, `"${env.VITE_FIREBASE_API_KEY || ''}"`)
          .replace(/import\.meta\.env\.VITE_FIREBASE_AUTH_DOMAIN/g, `"${env.VITE_FIREBASE_AUTH_DOMAIN || ''}"`)
          .replace(/import\.meta\.env\.VITE_FIREBASE_PROJECT_ID/g, `"${env.VITE_FIREBASE_PROJECT_ID || ''}"`)
          .replace(/import\.meta\.env\.VITE_FIREBASE_STORAGE_BUCKET/g, `"${env.VITE_FIREBASE_STORAGE_BUCKET || ''}"`)
          .replace(
            /import\.meta\.env\.VITE_FIREBASE_MESSAGING_SENDER_ID/g,
            `"${env.VITE_FIREBASE_MESSAGING_SENDER_ID || ''}"`,
          )
          .replace(/import\.meta\.env\.VITE_FIREBASE_APP_ID/g, `"${env.VITE_FIREBASE_APP_ID || ''}"`)
          .replace(/import\.meta\.env\.VITE_FIREBASE_MEASUREMENT_ID/g, `"${env.VITE_FIREBASE_MEASUREMENT_ID || ''}"`);

        const distDir = path.dirname(distSwPath);
        if (!fs.existsSync(distDir)) {
          fs.mkdirSync(distDir, { recursive: true });
        }
        fs.writeFileSync(distSwPath, swContent);
      }
    },
  });

  return {
    base: '/',
    plugins: [
      react(),
      tsconfigPaths(),
      injectEnvToServiceWorker(),
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
