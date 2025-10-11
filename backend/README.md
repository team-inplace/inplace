# Inplace ( Team7_BE )

<p align="center">
  <img src="https://i.ibb.co/CVBQHPg/image-2.png" alt="메인 이미지">
</p>

# 목차

1. [**프로젝트 개요**](#-프로젝트-개요)
   > 1.1 [**프로젝트 목적**](#-프로젝트-목적)
   >
   > 1.2 [**서비스 정보**](#-서비스-정보)
   >
   > 1.3 [**배포 주소**](#-배포-주소)

2. [**프로젝트 구조**](#-프로젝트-구조)
   > 2.1 [**프로그램 구조도**](#-프로그램-구조도)
   >
   > 2.2 [**API & ERD**](#-api--erd)
   >
   > 2.3 [**패키지 구조**](#패키지-구조)

3. [**개발 정보**](#-개발-정보)
   > 3.1 [**개발 기간**](#-개발-기간)
   >
   > 3.2 [**팀원**](#-팀원)
   >
   > 3.3 [**컨벤션**](#컨벤션)
   >
   > 3.4 [**브랜치 전략**](#브랜치-전략)
   >
   > 3.5 [**저장소 정보**](#-저장소-정보)

4. [**기술 정보**](#-기술-정보)
   > 4.1 [**주요 종속성 버전**](#-주요-종속성-버전)
   >
   > 4.2 [**기술 스택**](#-기술-스택)

5. [**테스트**](#-테스트)

6. [**기능 및 사용 예시**](#-기능-및-사용-예시)
   > 6.1 [**세부 기능 흐름**](#-세부-기능-흐름)
   >
   > 6.2 [**사용 예시**](#-사용-예시)

---

# 📝 프로젝트 개요

## 🙌 프로젝트 목적

> 저희의 아이디어는 **데이트 코스의 단조로움**을 어떻게 하면 해소할 수 있을까? 💡 라는 생각에서 시작했습니다.
>
> 아이디어를 구체화하는 과정에서 저희는 **장소**에 관련된 소재로 인스타, 유튜브 등의 SNS 및 동영상 플랫폼 📱의 성장과 함께 등장한 **인플루언서** 라는 개념에 집중하게
> 되었습니다.
>
> 이는 **인플루언서가 방문한 장소에 대한 정보를 서비스 해보자!** 📍 라는 생각으로 이어졌습니다.
>
> 이를 통해 사용자가 관심있는 **인플루언서를 등록하고, 이에 따른 장소 추천 및, 장소에 대한 리뷰 기능** 📋을 제공하여, **Inplace** 라는 저희만의 웹
> 애플리케이션으로 구현해보았습니다.

## 📋 서비스 정보

> 긴 영상은 필요 없어요 인플루언서가 다녀간 쿨플, 한눈에 쏙!

1. **회원가입 및 로그인**
    - Spring Security를 이용한 OAuth 2.0 카카오 로그인 기능을 사용합니다
    - 쿠키에 Access, Refresh Token을 담아 사용하며, Refresh 동작을 수행할 수 있습니다
2. **현재 위치 기반 서비스**
    - 웹 페이지의 위치 정보 사용에 동의시, 카카오 API와 내 위치 정보를 사용하여 주변의 장소 정보와, 자동 지도 위치 설정을 사용할 수 있습니다
3. **통합 검색 서비스**
    - ~~Elastic Search~~ NGram을 사용한 인플루언서, 장소, 비디오 이름에 대한 통합 검색 기능을 사용할 수 있습니다
4. **관심 인플루언서 등록 및 이를 토대로 한 서비스**
    - 최초 로그인 시 & 인플루언서 페이지에서 관심 인플루언서를 등록할 수 있습니다
    - 이를 토대로 메인 페이지에서 관심 인플루언서의 최신 방문 장소를 확인할 수 있습니다
5. **지도 기반 검색 서비스**
    - 지도 API를 이용하여 장소를 검색할 수 있습니다
        - 관심 등록하지 않은 인플루언서 및 주소, 장소 태그를 이용하여 세부 검색이 가능합니다
6. **장소 세부 정보 서비스**
    - 장소의 세부 정보를 열람할 수 있습니다
    - 장소에 달린 다른 유저들의 리뷰를 확인할 수 있습니다
    - 장소에 대한 좋아요 기능을 사용할 수 있습니다
    - 장소 세부 페이지에서 장소에 대한 정보를 카카오톡 메세지로 받아 볼 수 있습니다
7. **리뷰 기능**
    - 장소 정보를 받은 후 3일 뒤, 해당 장소에 대한 리뷰 링크를 받아 리뷰를 작성할 수 있습니다
    - 위 기능들은 카카오톡 메세지 보내기 API를 이용하며, 카카오톡으로 전송되는 링크는 모바일 뷰를 지원합니다
8. **마이 페이지 기능**
    - 좋아요 표시한 장소, 인플루언서를 관리할 수 있습니다
    - 내가 작성한 리뷰를 관리할 수 있습니다
    - 사용자 닉네임을 변경할 수 있습니다

## 🌐 배포 주소

> **BackEnd** : [**_api.inplace.my_**](https://api.inplace.my)
>
> **FrontEnd**: [**_inplace.my_**](https://inplace.my)
>
> **AdminPage** : [**_api.inplace.my/admin/main_**](https://api.inplace.my/admin/main)

---

# 🏗️ 프로젝트 구조

## 🖼️ 프로그램 구조도

### CI CD

![Backend_CICD](https://github.com/user-attachments/assets/f7db576e-5150-443c-8d2f-ab232294c296)

### System Architecture

![Architecture](https://github.com/user-attachments/assets/b21a697a-003e-4371-bdde-e12db6dbc1d9)

## 📊 API & ERD

![image](https://github.com/user-attachments/assets/ce11ff39-8294-4eda-aa58-7b5bd0ac1620)

- [🚗 Visit Team7 API](https://www.notion.so/API-9e96d1ef1475414b861a50d0e4ca366e)

![image](https://github.com/user-attachments/assets/333c9f3b-6678-48a6-b32d-b9b36d8cb182)

- [🙋‍♂️ Visit Team7 ERD](https://www.notion.so/ERD-36ec8e40cb264abe87588e97ae77ac55)

## 🗂️패키지 구조

<details><summary> 패키지 구조
</summary>

```
📦src
 ┣ 📂main
 ┃ ┣ 📂generated
 ┃ ┃ ┗ 📂team7
 ┃ ┃ ┃ ┗ 📂inplace
 ┃ ┃ ┃ ┃ ┣ 📂crawling
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QYoutubeChannel.java
 ┃ ┃ ┃ ┃ ┣ 📂favoriteInfluencer
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QFavoriteInfluencer.java
 ┃ ┃ ┃ ┃ ┣ 📂global
 ┃ ┃ ┃ ┃ ┃ ┗ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QErrorLog.java
 ┃ ┃ ┃ ┃ ┣ 📂influencer
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QInfluencer.java
 ┃ ┃ ┃ ┃ ┣ 📂likedPlace
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QLikedPlace.java
 ┃ ┃ ┃ ┃ ┣ 📂oauthToken
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QOauthToken.java
 ┃ ┃ ┃ ┃ ┣ 📂place
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QAddress.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QCoordinate.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QMenu.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QOffDay.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QOpenTime.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QPlace.java
 ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QReview.java
 ┃ ┃ ┃ ┃ ┣ 📂user
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QUser.java
 ┃ ┃ ┃ ┃ ┗ 📂video
 ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QVideo.java
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂team7
 ┃ ┃ ┃ ┗ 📂inplace
 ┃ ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┃ ┣ 📂banner
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂command
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BannerCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BannerInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BannerService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Banner.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BannerRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BannerS3Repository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BannerController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BannerRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BannerResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂cicd
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂crawling
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CrawlingInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddressUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrawlingFacade.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoCrawlingService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜VideoCrawlingService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜YoutubeCrawlingService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceNode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoMapClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜YoutubeClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChannelType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜YoutubeChannel.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜YoutubeChannelRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CrawlingController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂error
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorLog.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorLogController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ErrorLogRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜AdminPageController.java
 ┃ ┃ ┃ ┃ ┣ 📂favoriteInfluencer
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteInfluencerCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteInfluencerListCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteInfluencerService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteInfluencer.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistent
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteInfluencerRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerLikeRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerListLikeRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteInfluencerController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteInfluencerControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┣ 📂global
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Facade.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂code
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorizationErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BannerErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChannelErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LikedPlaceErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InplaceException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InplaceExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂kakao
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoApiProperties.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂queryDsl
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QueryDslConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂rest
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestTemplateConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebClientConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂scheduler
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ScheduledExecutorConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂influencer
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerNameInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Influencer.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerNameResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┣ 📂infra
 ┃ ┃ ┃ ┃ ┃ ┗ 📂s3
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AwsProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜S3Config.java
 ┃ ┃ ┃ ┃ ┣ 📂likedPlace
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LikedPlace.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LikedPlaceRepository.java
 ┃ ┃ ┃ ┃ ┣ 📂oauthToken
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂command
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OauthTokenCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OauthTokenService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OauthToken.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OauthTokenRepository.java
 ┃ ┃ ┃ ┃ ┣ 📂place
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂command
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceLikeCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlacesCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LikedPlaceInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceDetailInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceForVideo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Address.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Category.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Coordinate.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Menu.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OffDay.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenTime.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Place.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceCustomRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceCustomRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoriesResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceDetailResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceLikeRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlacesResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┣ 📂placeMessage
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂command
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceMessageCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoMessageService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceMessageFacade.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceMessageController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceMessageControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Button.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Content.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FeedTemplate.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoMessageMaker.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Link.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LocationTemplate.java
 ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MyReviewInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Review.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┣ 📂search
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AutoCompletionInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceSearchInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SearchType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SearchService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MatchAgainstFunctionContributor.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SearchResult.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerSearchRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceSearchRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SearchRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoSearchRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SearchController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SearchControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomOAuth2User.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoOAuthResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CurrentUserProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomOAuth2UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CorsConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityEntryPointConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityFilterConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityHandlerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityServiceConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityUtilConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entryPoint
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginAuthenticationEntryPoint.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorizationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExceptionHandlingFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenType.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomAccessDeniedHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomFailureHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomSuccessHandler.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorizationUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CookieUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenEncryptionUtil.java
 ┃ ┃ ┃ ┃ ┣ 📂token
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshTokenFacade.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshTokenService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshToken.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshTokenRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshTokenController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshTokenControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┣ 📂user
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserFacade.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Role.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜User.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserType.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LikedInfluencerResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LikedPlaceResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MyReviewResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserInfoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TempController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserControllerApiSepc.java
 ┃ ┃ ┃ ┃ ┣ 📂video
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂command
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoCommand.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AliasUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Template.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜VideoFacade.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Video.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂presentation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜VideoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoSearchParams.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜VideoController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoControllerApiSpec.java
 ┃ ┃ ┃ ┃ ┗ 📜my.inplace.api.InplaceApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂META-INF
 ┃ ┃ ┃ ┗ 📂services
 ┃ ┃ ┃ ┃ ┗ 📜org.hibernate.boot.model.FunctionContributor
 ┃ ┃ ┣ 📂sql
 ┃ ┃ ┃ ┣ 📜data.sql
 ┃ ┃ ┃ ┗ 📜schema.sql
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┃ ┣ 📂css
 ┃ ┃ ┃ ┃ ┣ 📜banner.css
 ┃ ┃ ┃ ┃ ┣ 📜error_logs_style.css
 ┃ ┃ ┃ ┃ ┣ 📜main.css
 ┃ ┃ ┃ ┃ ┗ 📜style.css
 ┃ ┃ ┃ ┣ 📂js
 ┃ ┃ ┃ ┃ ┣ 📜banner.js
 ┃ ┃ ┃ ┃ ┣ 📜error-logs.js
 ┃ ┃ ┃ ┃ ┣ 📜main.js
 ┃ ┃ ┃ ┃ ┗ 📜video.js
 ┃ ┃ ┃ ┗ 📜favicon.ico
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┣ 📜banner.html
 ┃ ┃ ┃ ┃ ┣ 📜error-logs.html
 ┃ ┃ ┃ ┃ ┣ 📜main.html
 ┃ ┃ ┃ ┃ ┗ 📜video.html
 ┃ ┃ ┃ ┗ 📂introduce
 ┃ ┃ ┃ ┃ ┗ 📜main.html
 ┃ ┃ ┣ 📜application-db.yaml
 ┃ ┃ ┣ 📜application-kakao.yaml
 ┃ ┃ ┣ 📜application-redis.yaml
 ┃ ┃ ┣ 📜application-s3.yaml
 ┃ ┃ ┣ 📜application-security.yaml
 ┃ ┃ ┣ 📜application-youtube.yaml
 ┃ ┃ ┗ 📜application.yaml
 ┗ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂team7
 ┃ ┃ ┃ ┗ 📂inplace
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┗ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomRepositoryTest.java
 ┃ ┃ ┃ ┃ ┣ 📂crawling
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddressUtilTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoMapClientTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜YoutubeClientTest.java
 ┃ ┃ ┃ ┃ ┣ 📂influencer
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerControllerTest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InfluencerRepositoryTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜InfluencerServiceTest.java
 ┃ ┃ ┃ ┃ ┣ 📂likedPlace
 ┃ ┃ ┃ ┃ ┃ ┗ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LikedPlaceRepositoryTest.java
 ┃ ┃ ┃ ┃ ┣ 📂place
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceRepositoryTest.java
 ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┣ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewRepositoryTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewServiceTest.java
 ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CorsTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfigTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorizationUtilTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtUtilTest.java
 ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TestUtil.java
 ┃ ┃ ┃ ┃ ┣ 📂video
 ┃ ┃ ┃ ┃ ┃ ┣ 📂application
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂persistence
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VideoRepositoryTest.java
 ┃ ┃ ┃ ┃ ┗ 📜InplaceApplicationTests.java
```

</details>

---

# 👨‍💻 개발 정보

## 📅 개발 기간

> **_2024.08.22 ~ 2024.11.15_**

## 👥 팀원

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<table>
  <tr>
    <td align="center">
       <b>Frontend</b><br />
    </td>
    <td align="center">
       <b>Frontend</b><br />
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/userjmmm"><img src="https://avatars.githubusercontent.com/u/141299582?v=4" width="80px;" alt=""/><br /><sub><b>이정민</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/Hyoeunkh"><img src="https://avatars.githubusercontent.com/u/102338613?v=4" width="80px;" alt=""/><br /><sub><b>이효은</b></sub></a>
    </td>
  </tr>
</table>
<table>
  <tr>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
    <td align="center">
      <b>Backend</b><br />
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/sanghee0820"><img src="https://avatars.githubusercontent.com/u/102018082?v=4" width="80px;" alt=""/><br /><sub><b>이상희</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/dong-yxxn"><img src="https://avatars.githubusercontent.com/u/129285999?v=4" width="80px;" alt=""/><br /><sub><b>김동윤</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/suhyeon7497"><img src="https://avatars.githubusercontent.com/u/137245467?v=4" width="80px;" alt=""/><br /><sub><b>정수현</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/wndlthsk"><img src="https://avatars.githubusercontent.com/u/80496766?v=4" width="80px;" alt=""/><br /><sub><b>우현서</b></sub></a>
    </td>
    <td align="center">
      <a href="https://github.com/BaeJunH0"><img src="https://avatars.githubusercontent.com/u/114082026?v=4" width="80px;" alt=""/><br /><sub><b>배준호</b></sub></a>
    </td>
  </tr>
</table>
<!-- ALL-CONTRIBUTORS-LIST:END -->

## 컨벤션

> [**Commit Convention
**](https://github.com/kakao-tech-campus-2nd-step3/Team7_BE/wiki/Commit-%EC%BB%A8%EB%B2%A4%EC%85%98)
>
> [**PR Convention
**](https://github.com/kakao-tech-campus-2nd-step3/Team7_BE/wiki/PR-%EC%BB%A8%EB%B2%A4%EC%85%98)
>
> [**Issue Convention
**](https://github.com/kakao-tech-campus-2nd-step3/Team7_BE/wiki/%EC%9D%B4%EC%8A%88-%EC%BB%A8%EB%B2%A4%EC%85%98)
>
> [**Coding Convention
**](https://github.com/kakao-tech-campus-2nd-step3/Team7_BE/wiki/%EC%BD%94%EB%94%A9-%EC%BB%A8%EB%B2%A4%EC%85%98)
>
> [**브랜치 전략
**](https://github.com/kakao-tech-campus-2nd-step3/Team7_BE/wiki/%EB%B8%8C%EB%9E%9C%EC%B9%98-%EC%A0%84%EB%9E%B5)
>
💡 *컨벤션과
브랜치전략은 <span style="color: red;">[Notiion](https://quickest-asterisk-75d.notion.site/7-7-05d799c3f023443f8fb66abf97ca5965)</span>
에서도 확인이 가능합니다!*

## 📂 저장소 정보

> **Using Language**
>
![GitHub language count](https://img.shields.io/github/languages/count/kakao-tech-campus-2nd-step3/Team7_BE)
![GitHub top language](https://img.shields.io/github/languages/top/kakao-tech-campus-2nd-step3/Team7_BE)

> **Repo, Code Volume**
>
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kakao-tech-campus-2nd-step3/Team7_BE)
![GitHub repo size](https://img.shields.io/github/repo-size/kakao-tech-campus-2nd-step3/Team7_BE)

> **Commit Avg**
>
![GitHub commit activity](https://img.shields.io/github/commit-activity/w/kakao-tech-campus-2nd-step3/Team7_BE)

> **Issues**
>
![GitHub open issues](https://img.shields.io/github/issues/kakao-tech-campus-2nd-step3/Team7_BE)
![GitHub closed issues](https://img.shields.io/github/issues-closed/kakao-tech-campus-2nd-step3/Team7_BE)

> **PRs**
>
![GitHub pull requests](https://img.shields.io/github/issues-pr/kakao-tech-campus-2nd-step3/Team7_BE?label=open%20pull%20requests)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/kakao-tech-campus-2nd-step3/Team7_BE?label=closed%20pull%20requests)

---

# 🛠️ 기술 정보

## 🧩 주요 종속성 버전

> **Spring Boots 3.3.3**
>
> **Java 17 LTS**
>
> **AWS**
> Spring-Cloud-Starter-AWS:2.2.6.RELEASE
>
> **JWT**
>
> JJWT-API:0.12.3
>
> JJWT-IMPL:0.12.3
>
> JJWT-JACKSON:0.12.3
>
> **SpringDoc(Swagger)**
>
> SpringDoc-OpenApi-Starter-WebMvc-UI: 2.0.3
>
> **QueryDSL**
>
> QueryDsl-JPA: 5.0.0(Jakarata)
>
> **Spring Starter는 Spring버전을 따릅니다**
>
> spring-boot-starter-security
>
> spring-boot-starter-oauth2-client
>
> spring-boot-starter-data-jpa
>
> spring-boot-starter-data-redis
>
> spring-boot-starter-thymeleaf

## 🚀 기술 스택

> **Backend**
>
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-DC382D?style=flat-square&logo=lombok&logoColor=white)

> **Security**
>
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)

> **DB**
>
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0055a2?style=flat-square&logo=appveyor&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)

> **Web**
>
![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-6DB33F?style=flat-square&logo=spring&logoColor=white)

> **Deployment**
>
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=flat-square&logo=amazonaws&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)

> **Admin Page**
>
![jQuery](https://img.shields.io/badge/jQuery-0769AD?style=flat-square&logo=jquery&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white)
![AJAX](https://img.shields.io/badge/AJAX-005571?style=flat-square&logo=ajax&logoColor=white)

> **Test**
>
![JMeter](https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=apachejmeter&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-FFCA28?style=flat-square&logo=mockito&logoColor=white)

> **Code Maintenance**
>
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)

> **Collaboration Tool**
>
![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=discord&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white)

---

# ✅ 테스트

> **테스트 시나리오** : https://www.notion.so/9ed68b292c004fc69f7eaad513054d96
>
> **테스트 결과보고서** : https://www.notion.so/08a520d3b8c44154a19425b0bcc16f6f

---

# 🎬 기능 및 사용 예시

## 🔍 세부 기능 흐름

- ### Spring Security
    - oauth 로그인 시, jwt로 accessToken과 refreshToken을 Cookie에 담아줍니다.
        - oauthToken은 aes알고리즘으로 암호화 되어 db에 저장됩니다.
    - 모든 요청은 AuthorizationFilter에서 Cookie에 있는 토큰이 유효한지 확인하고, 유효하면 Authenticate합니다.
- ### Influencer
    - 로그인 상태인 경우 사용자가 좋아요한 인플루언서를 먼저 반환합니다.
- ### FavoriteInfluencer
    - 로그인 상태를 확인한 후 좋아요/ 싫어요 요청을 처리합니다.
    - 내가 좋아요한 인플루언서 정보를 반환합니다.
- ### Place
    - places에 videos, influecers 테이블을 Left join하고 지도 범위 내에있는 장소를 추려냅니다.
    - 카테고리와 인플루언서 이름으로 필터링하고, 사용자와 가까운순으로 정렬 후 반환합니다.
        - 특정 장소에 대한 요청이 들어오면 세부 정보와 함께 관련된 인플루언서, 비디오와 사용자 리뷰 정보를 추가하여 반환합니다.
    - 내가 좋아요한 장소 조회 시 인플루언서 정보 일부를 함께 반환합니다.
    - PlaceMessage
        - 요청이 오면 webClient(비동기)로 나에게보내기 kakao api를 통해 장소 정보를 보냅니다.
        - 3일 뒤 나에게 보내기 kakao api를 통해 리뷰 요청 메세지를 보냅니다.
- ### LikedPlace
    - 사용자가 장소에 좋아요를 누르거나 취소하면 상태를 업데이트합니다.
        - 기존 정보가 존재하지 않으면 새로 생성후 저장합니다.
- ### Video
    - 로그인 상태인 경우 사용자가 좋아요한 인플루언서의 동영상, 사용자 주변 장소의 동영상을 반환합니다.
    - 로그인 상태가 아닌 경우 조회수 증가량이 높은 동영상, 새로운 동영상을 반환합니다.
- ### Review
    - 로그인 상태를 확인한 후 리뷰 추가, 리뷰 삭제 요청을 처리합니다.
    - 장소별 리뷰 조회 시 본인이 작성한 리뷰인지 여부를 포함하여 반환합니다.
    - 내가 작성한 리뷰 조회 시 장소 정보 일부를 함께 반환합니다.
- ### User
    - User nickname을 더티 체킹을 통해 변경한다.
- ### TokenRefresh
    - Redis DB에 username(key)로 refreshToken을 확인하고, RTR (Refresh Token Rotation)을 합니다.
- ### videoCrawling
    - DB에서 인플루언서 유튜브 채널을 가져와 새로 업데이트 된 정보 크롤링해서 정규표현식으로 주소정보를 추출합니다.
    - Video에서 장소정보가 추출되면 kakaoApi를 통해 매장 정보를 가져와 인플루언서별로 트랜잭션을 분리해 장소정보를 저장합니다.
    - Video에서 장소정보가 추출되지 않으면 Admin페이지를 통해 수동으로 장소 정보를 입력합니다.
- ### AdminPage
    - 배너, 에러로그, 강제크롤링 기능을 제공합니다.
    - 홈페이지에 배너를 등록하거나 삭제할 수 있습니다.
    - 서버 내에서 발생한 500번대 에러로그와 StackTrace를 확인할 수 있습니다.
    - 스케쥴링 되어있는 비디오 크롤링을 강제로 실행할 수 있습니다.
    - 스케쥴링 되어있는 조회수 크롤링을 강제로 실행할 수 있습니다.

## 📱 사용 예시

> 실제 유저 사용 페이지 흐름 보여주기 ( 영상 x )
