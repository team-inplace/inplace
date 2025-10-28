import * as Sentry from "@sentry/core";
import type { Scope } from "@sentry/core";
import type { SeverityLevel } from "@sentry/types";

const ERROR_CAPTURE_MAX_RETRIES = 3;
const ERROR_CAPTURE_RETRY_DELAY_MS = 1000;

/**
 * @returns Sentry 초기화 여부 (boolean)
 */
export const getSentryInitialized = (): boolean => {
  return !!Sentry.getCurrentHub().getClient();
};

export const captureErrorWithRetry = async (
  error: unknown,
  options?: {
    tags?: Record<string, string>;
    level?: SeverityLevel;
  },
  retryCount = 0,
  maxRetries = ERROR_CAPTURE_MAX_RETRIES
): Promise<boolean> => {
  if (!getSentryInitialized()) {
    return false;
  }

  try {
    if (options?.tags || options?.level) {
      Sentry.withScope((scope: Scope) => {
        if (options.tags) {
          Object.entries(options.tags).forEach(([key, value]) => {
            scope.setTag(key, value);
          });
        }
        if (options.level) {
          scope.setLevel(options.level);
        }
        if (error instanceof Error) {
          Sentry.captureException(error);
        } else {
          Sentry.captureMessage(String(error));
        }
      });
    } else if (error instanceof Error) {
      Sentry.captureException(error);
    } else {
      Sentry.captureMessage(String(error));
    }
    return true;
  } catch (captureError) {
    if (retryCount < maxRetries) {
      console.warn(
        `Error capture failed, retrying... (${retryCount + 1}/${maxRetries})`
      );
      await new Promise((resolve) => {
        setTimeout(resolve, ERROR_CAPTURE_RETRY_DELAY_MS);
      });
      return captureErrorWithRetry(error, options, retryCount + 1, maxRetries);
    }

    console.error("Failed to capture error after maximum retries", {
      originalError: error,
      captureError,
    });
    return false;
  }
};

export default captureErrorWithRetry;
