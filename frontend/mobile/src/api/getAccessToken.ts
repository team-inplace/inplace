import { getConfig } from "@inplace-frontend-monorepo/shared/src/api/config";

type UserInfo = {
  nickname: string;
  username: string;
  profile_image_url: string;
};

type Tokens = {
  accessToken: string;
  refreshToken: string;
};

export const getAccessToken = async (userInfo: UserInfo) => {
  const config = getConfig();
  try {
    const response = await fetch(
      `${config.baseURL}/login/oauth2/code/kakao/mobile`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userInfo }),
      }
    );

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
