package team7.inplace.place.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import team7.inplace.global.annotation.Facade;
import team7.inplace.influencer.application.InfluencerService;
import team7.inplace.place.application.dto.PlaceQueryInfo;
import team7.inplace.place.application.dto.PlaceQueryInfo.Marker;
import team7.inplace.place.persistence.dto.PlaceQueryResult;

@Facade
@RequiredArgsConstructor
public class PlaceFacade {
    private final PlaceService placeService;
    private final InfluencerService influencerService;

    public Marker getMarkerInfo(Long placeId) {
        PlaceQueryResult.Marker marker = placeService.getMarkerInfo(placeId);
        List<String> influencerNames = influencerService.getInfluencerNamesByPlaceId(placeId);
        return PlaceQueryInfo.Marker.from(marker, influencerNames);
    }
}
