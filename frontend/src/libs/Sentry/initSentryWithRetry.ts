import * as Sentry from '@sentry/react';
import { browserTracingIntegration } from '@sentry/react';
import { getSentryInitialized, setSentryInitialized } from './sentry';

const SENTRY_INIT_MAX_RETRIES = 3;
const SENTRY_INIT_RETRY_DELAY_MS = 1000;
const SENTRY_TRACES_SAMPLE_RATE = 1.0;

export const initSentryWithRetry = async (retryCount = 0, maxRetries = SENTRY_INIT_MAX_RETRIES): Promise<boolean> => {
  try {
    Sentry.init({
      denyUrls: [/dev/],
      dsn: import.meta.env.VITE_SENTRY_DSN,
      integrations: [browserTracingIntegration()],
      tracesSampleRate: SENTRY_TRACES_SAMPLE_RATE,
      tracePropagationTargets: [/^https:\/\/api\.inplace\.my/, '!http://localhost/', '!https://localhost/'],
      beforeSend: (event) => {
        if (getSentryInitialized()) {
          return event;
        }
        return null;
      },
    });

    setSentryInitialized(true);
    return true;
  } catch (error) {
    if (retryCount < maxRetries) {
      console.warn(`Sentry initialization failed, retrying... (${retryCount + 1}/${maxRetries})`);
      await new Promise((resolve) => {
        setTimeout(resolve, SENTRY_INIT_RETRY_DELAY_MS);
      });
      return initSentryWithRetry(retryCount + 1, maxRetries);
    }
    console.error('Failed to initialize Sentry after maximum retries', error);
    return false;
  }
};
export default initSentryWithRetry;
