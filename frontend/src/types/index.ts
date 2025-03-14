export type BannerData = {
  id: number;
  imageUrl: string;
  influencerId: number | null;
  isMain: boolean;
  isMobile: boolean;
};
export type InfluencerData = {
  influencerId: number;
  influencerName: string;
  influencerImgUrl: string;
  influencerJob: string;
  likes?: boolean;
};

export type SpotData = {
  videoId: number;
  videoAlias: string;
  videoUrl: string;
  place: {
    placeId: number;
    placeName: string;
  };
};
export type PageableData<T> = {
  totalPages: number;
  totalElements: number;
  size: number;
  content: T[];
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  pageable: {
    offset: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    paged: boolean;
    pageNumber: number;
    pageSize: number;
    unpaged: boolean;
  };
  first: boolean;
  last: boolean;
  empty: boolean;
};

export type AddressInfo = {
  address1: string;
  address2: string;
  address3: string;
};

export type PlaceData = {
  placeId: number;
  placeName: string;
  address: AddressInfo;
  category: string;
  videos: {
    videoUrl: string;
    influencerName: string;
  }[];
  menuImgUrl?: string | null;
  longitude: string;
  latitude: string;
  likes: boolean;
};

export type LocationData = {
  topLeftLongitude: number;
  topLeftLatitude: number;
  bottomRightLongitude: number;
  bottomRightLatitude: number;
};

export type FilterParams = {
  categories: string[];
  influencers: string[];
};

export type PlaceList = {
  places: PlaceData[];
};

export type PlaceInfo = {
  placeId: number;
  placeName: string;
  address: AddressInfo;
  category: string;
  videos: {
    videoUrl: string;
    influencerName: string;
  }[];
  openingHours: string[];
  googlePlaceUrl: string;
  kakaoPlaceUrl: string;
  naverPlaceUrl: string;
  googleReviews: GoogleReview[];
  rating: number;
  longitude: string;
  latitude: string;
  facility: FacilityInfo;
  placeLikes: PlaceLikes;
  likes: boolean;
};
export type GoogleReview = {
  like: boolean;
  text: string;
  name: string;
  publishTime: Date;
};

export type PlaceLikes = {
  like: number;
  dislike: number;
};

export type FacilityInfo = {
  wheelchairAccessibleSeating?: boolean;
  freeParkingLot?: boolean;
  paidParkingLot?: boolean;
  acceptsCreditCards?: boolean;
};

export type ReviewData = {
  reviewId: number;
  likes: boolean;
  comment: string;
  userNickname: string;
  createdDate: Date;
  mine: boolean;
};

export type RequestInfluencerLike = {
  influencerId: number;
  likes: boolean;
};
export type UserInfoData = {
  nickname: string;
};
export type UserPlaceData = {
  placeId: number;
  placeName: string;
  imageUrl?: string | null;
  influencerName: string;
  address: AddressInfo;
  likes: boolean;
};
export type RequestPlaceLike = {
  placeId: number;
  likes: boolean;
};
export type UserReviewData = {
  reviewId: number;
  place: {
    placeId: number;
    placeName: string;
    imgUrl?: string | null;
    address: {
      address1: string;
      address2: string;
      address3: string;
    };
  };
  likes: boolean;
  comment: string;
  createdDate: Date;
};
export type RequestPlaceReview = {
  likes: boolean | null;
  comments: string;
};

export type MultipleLikeRequest = {
  influencerIds: number[];
  likes: boolean;
};

export type SearchComplete = {
  result: string;
  score: number;
  searchType: string;
};
export type InfluencerInfoData = {
  influencerId: number;
  influencerName: string;
  influencerImgUrl: string;
  influencerJob: string;
  likes: boolean;
  follower: number;
  placeCount: number;
};
export type MarkerData = {
  placeId: number;
  longitude: number;
  latitude: number;
};
export type MarkerInfo = {
  placeId: number;
  placeName: string;
  category: string;
  videos: {
    videoUrl: string;
    influencerName: string;
  }[];
  address: AddressInfo;
  menuImgUrl?: string | null;
};

export interface ReviewInfo {
  placeName: string;
  placeAddress: string;
  placeImgUrl: string | null;
  influencerName: string;
  userNickname: string;
}
