import styled from 'styled-components';
import { Map, MapMarker } from 'react-kakao-maps-sdk';
import { IoMdStar } from 'react-icons/io';
import { Suspense, useState } from 'react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import { Paragraph } from '@/components/common/typography/Paragraph';
import FacilitySign from './FacilitySign';
import { AddressInfo, FacilityInfo, GoogleReview } from '@/types';
import OpenHour from './OpenHour';
import { Text } from '@/components/common/typography/Text';
import GoogleReviewList from '../GoogleReviewList';
import NoItem from '@/components/common/layouts/NoItem';
import VisitModal from '../VisitModal';
import Loading from '@/components/common/layouts/Loading';
import ErrorComponent from '@/components/common/layouts/Error';
import SpeedDialMap from './SpeedDialMap';

type Props = {
  address: AddressInfo;
  category: string;
  facility: FacilityInfo;
  openingHours: string[];
  kakaoPlaceUrl: string;
  naverPlaceUrl: string;
  googlePlaceUrl: string;
  googleReviews: GoogleReview[];
  longitude: string;
  latitude: string;
  rating: number;
  placeId: number;
};
export default function InfoTap({
  address,
  category,
  facility,
  openingHours,
  kakaoPlaceUrl,
  naverPlaceUrl,
  googlePlaceUrl,
  googleReviews,
  longitude,
  latitude,
  rating,
  placeId,
}: Props) {
  const lat = Number(latitude);
  const lng = Number(longitude);
  const [visitModal, setVisitModal] = useState(false);

  return (
    <Wrapper>
      <ButtonWrapper>
        <SpeedDialMap
          kakaoPlaceUrl={kakaoPlaceUrl}
          naverPlaceUrl={naverPlaceUrl}
          googlePlaceUrl={googlePlaceUrl}
          visitModal={visitModal}
          setVisitModal={setVisitModal}
        />
      </ButtonWrapper>
      <Paragraph size="s" weight="bold">
        주소
      </Paragraph>
      <Text size="xs" weight="normal">
        {[address.address1, address.address2, address.address3].join(' ')}
      </Text>
      {googlePlaceUrl ? (
        <>
          <Paragraph size="s" weight="bold">
            시설 정보
          </Paragraph>
          <FacilitySign category={category} facilityInfo={facility} />
          <Paragraph size="s" weight="bold">
            운영 시간
          </Paragraph>
          <OpenHour openHour={openingHours} />
          <GoogleReviewTitle>
            <StyledText size="s" weight="bold">
              Google 리뷰
              <IoMdStar size={20} color="#FBBC04" />
              <Text size="xs" weight="normal">
                {rating}
              </Text>
            </StyledText>
            <GoogleDescription>
              <Text size="xs" weight="normal" variant="grey">
                구글 평점 4점 이상일 경우, 좋아요로 표시됩니다.
              </Text>
              <Text size="xs" weight="normal" variant="grey">
                Google 제공
              </Text>
            </GoogleDescription>
          </GoogleReviewTitle>
          <GoogleReviewContainer>
            <GoogleReviewList lists={googleReviews} />
            <Text size="xs" weight="normal" variant="grey">
              구글 리뷰는 최대 5개까지 표시됩니다.
            </Text>
          </GoogleReviewContainer>
        </>
      ) : (
        <NoItem
          message={`장소 세부정보를 확인할 수 없어요.\n카카오맵에서 확인해주세요.`}
          height={200}
          logo
          alignItems="center"
        />
      )}
      <Paragraph size="s" weight="bold">
        지도 보기
      </Paragraph>
      <MapContainer>
        <Map
          center={{
            lat,
            lng,
          }}
          style={{
            width: '100%',
            height: '100%',
            zIndex: 0,
          }}
          level={3}
        >
          <MapMarker
            position={{
              lat,
              lng,
            }}
          />
        </Map>
      </MapContainer>
      {visitModal && (
        <QueryErrorResetBoundary>
          {({ reset }) => (
            <ErrorBoundary FallbackComponent={ErrorComponent} onReset={reset}>
              <Suspense fallback={<Loading size={50} />}>
                <VisitModal id={placeId} onClose={() => setVisitModal(false)} />{' '}
              </Suspense>
            </ErrorBoundary>
          )}
        </QueryErrorResetBoundary>
      )}
    </Wrapper>
  );
}
const Wrapper = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 50px;

  @media screen and (max-width: 768px) {
    gap: 30px;
  }
`;
const GoogleReviewContainer = styled.div`
  padding: 0px 20px;
  margin-bottom: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  @media screen and (max-width: 768px) {
    width: 100%;
    padding: 0px;
  }
`;

const MapContainer = styled.div`
  display: flex;
  justify-content: center;
  margin: 0 auto;
  width: 95%;
  height: 410px;
  margin-bottom: 120px;

  @media screen and (max-width: 768px) {
    width: 100%;
    height: 300px;
    margin-bottom: 60px;
  }
`;

const GoogleReviewTitle = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 10px;
`;
const StyledText = styled(Text)`
  display: flex;
  gap: 3px;
  align-items: end;
  svg {
    margin-left: 10px;
  }
`;

const GoogleDescription = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
`;

const ButtonWrapper = styled.div`
  position: absolute;
  right: -20px;
  display: flex;
  gap: 16px;
  align-items: start;

  @media screen and (max-width: 768px) {
    gap: 10px;
    right: 46px;
  }
`;
