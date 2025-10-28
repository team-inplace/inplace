require("dotenv").config();

module.exports = {
  expo: {
    name: "mobile",
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
        LSApplicationQueriesSchemes: ["kakaokompassauth", "kakaolink"],
        CFBundleURLTypes: [
          {
            CFBundleURLSchemes: [`kakao${process.env.KAKAO_NATIVE_APP_KEY}`],
          },
        ],
      },
    },
    android: {
      package: "my.inplace.mobile",
      adaptiveIcon: {
        foregroundImage: "./assets/adaptive-icon.png",
        backgroundColor: "#292929",
      },
    },
    web: {
      favicon: "./assets/favicon.png",
    },
    plugins: [
      [
        "expo-secure-store",
        {
          configureAndroidBackup: true,
          faceIDPermission:
            "Allow $(inplace) to access your Face ID biometric data.",
        },
      ],
    ],
    scheme: "my.inplace",
    extra: {
      eas: {
        projectId: "1f938aec-ae98-4f0c-ab7a-c4c37114e2a7",
      },
    },
  },
};
