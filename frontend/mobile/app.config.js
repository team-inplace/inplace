require("dotenv").config();

module.exports = {
  expo: {
    name: "인플레이스",
    slug: "mobile",
    newArchEnabled: true,
    version: "1.0.0",
    orientation: "portrait",
    icon: "./assets/icon.png",
    userInterfaceStyle: "light",
    splash: {
      image: "./assets/splash-icon.png",
      resizeMode: "contain",
      backgroundColor: "#292929",
    },
    ios: {
      supportsTablet: true,
      bundleIdentifier: "my.inplace.mobile",
      infoPlist: {
        LSApplicationQueriesSchemes: [
          "kakaokompassauth",
          "kakaolink",
          `kakao${process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY}`,
        ],
        CFBundleURLTypes: [
          {
            CFBundleTypeRole: "Editor",
            CFBundleURLSchemes: [
              `kakao${process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY}`,
            ],
          },
        ],
      },
    },
    android: {
      package: "my.inplace.mobile",
      googleServicesFile: "./google-services.json",
      adaptiveIcon: {
        foregroundImage: "./assets/InplaceLogo.png",
        backgroundColor: "#ffffff",
      },
      intentFilters: [
        {
          action: "VIEW",
          autoVerify: true,
          data: [
            {
              scheme: `kakao${process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY}`,
              host: "oauth",
            },
          ],
          category: ["DEFAULT", "BROWSABLE"],
        },
      ],
    },
    web: {
      favicon: "./assets/favicon.png",
    },
    plugins: [
      [
        "expo-notifications",
        {
          icon: "./assets/icon.png",
          defaultChannel: "default",
        },
      ],
      [
        "expo-secure-store",
        {
          configureAndroidBackup: true,
          faceIDPermission:
            "Allow $(inplace) to access your Face ID biometric data.",
        },
      ],
      [
        "expo-build-properties",
        {
          android: {
            extraMavenRepos: [
              "https://devrepo.kakao.com/nexus/content/groups/public/",
            ],
          },
        },
      ],
      [
        "@react-native-kakao/core",
        {
          nativeAppKey: process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY,
          android: { authCodeHandlerActivity: true },
          ios: { handleKakaoOpenUrl: true },
        },
      ],
    ],
    extra: {
      eas: {
        projectId: process.env.EXPO_PUBLIC_PROJECT_ID,
      },
    },
  },
};
