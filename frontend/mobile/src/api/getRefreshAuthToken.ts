import { getConfig } from "@inplace-frontend-monorepo/shared/src/api/config";

type Tokens = {
  accessToken: string;
  refreshToken: string;
};

export const getRefreshAuthToken = async (
  token: string
): Promise<Tokens | null> => {
  const config = getConfig();
  try {
    const response = await fetch(`${config.baseURL}/refresh-token`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ refreshToken: token }),
    });

    if (!response.ok) {
      throw new Error("서버 인증에 실패했습니다.");
    }

    const { accessToken, refreshToken }: Tokens = await response.json();

    return { accessToken, refreshToken };
  } catch (error) {
    console.error("API 통신 오류:", error);
    return null;
  }
};
