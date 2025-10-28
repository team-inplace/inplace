import { getConfig } from "@inplace-frontend-monorepo/shared/src/api/config";

export const getAccessToken = async (token: string) => {
  const config = getConfig();
  try {
    const response = await fetch(`${config.baseURL}/auth/kakao`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ kakaoAccessToken: token }),
    });

    if (!response.ok) {
      throw new Error("서버 인증에 실패했습니다.");
    }

    const { jwtToken } = await response.json();
    return jwtToken;
  } catch (error) {
    console.error("API 통신 오류:", error);
    return null;
  }
};
