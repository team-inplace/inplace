let isSentryInitialized = false;

export const setSentryInitialized = (value: boolean) => {
  isSentryInitialized = value;
};

export const getSentryInitialized = () => isSentryInitialized;
