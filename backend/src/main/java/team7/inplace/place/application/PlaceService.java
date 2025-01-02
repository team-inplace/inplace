package team7.inplace.place.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.PlaceErrorCode;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.liked.likedPlace.domain.LikedPlace;
import team7.inplace.liked.likedPlace.persistence.LikedPlaceRepository;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand.Create;
import team7.inplace.place.application.command.PlacesCommand.PlacesCoordinateCommand;
import team7.inplace.place.application.command.PlacesCommand.PlacesFilterParamsCommand;
import team7.inplace.place.application.dto.LikedPlaceInfo;
import team7.inplace.place.application.dto.PlaceDetailInfo;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.domain.Category;
import team7.inplace.place.domain.Place;
import team7.inplace.place.domain.PlaceBulk;
import team7.inplace.place.persistence.PlaceRepository;
import team7.inplace.place.persistence.PlaceSaveRepository;
import team7.inplace.placeMessage.application.command.PlaceMessageCommand;
import team7.inplace.review.persistence.ReviewRepository;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;
import team7.inplace.video.domain.Video;
import team7.inplace.video.persistence.VideoRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    private final VideoRepository videoRepository;

    private final UserRepository userRepository;

    private final LikedPlaceRepository likedPlaceRepository;

    private final ReviewRepository reviewRepository;

    private final PlaceSaveRepository placeSaveRepository;

    public Page<PlaceInfo> getPlacesWithinRadius(
            PlacesCoordinateCommand placesCoordinateCommand,
            PlacesFilterParamsCommand placesFilterParamsCommand
    ) {
        // categories와 influencers 필터 처리
        List<String> categoryFilters = placesFilterParamsCommand.isCategoryFilterExists()
                ? Arrays.stream(placesFilterParamsCommand.categories().split(","))
                .map(Category::of)
                .map(Category::name)
                .toList()
                : null;

        List<String> influencerFilters = placesFilterParamsCommand.isInfluencerFilterExists()
                ? Arrays.stream(placesFilterParamsCommand.influencers().split(",")).toList()
                : null;

        // 주어진 좌표로 장소를 찾고, 해당 페이지의 결과를 가져옵니다.
        Page<PlaceBulk> placesPage = getPlacesByDistance(placesCoordinateCommand, categoryFilters,
                influencerFilters);

        // Place ID 목록 추출
        List<Long> placeIds = getPlaceIds(placesPage);

        // influencer 조회 => video->Map(placeId, influencerName)
        List<Video> videos = videoRepository.findByPlaceIdIn(placeIds);
        Map<Long, String> placeIdToInfluencerName = getMapPlaceIdToInfluencerName(
                videos);

        // PlaceInfo 생성
        List<PlaceInfo> placeInfos = convertToPlaceInfos(placesPage, placeIdToInfluencerName);

        // PlaceInfo 리스트를 Page로 변환하여 반환
        return new PageImpl<>(placeInfos, placesPage.getPageable(), placesPage.getTotalElements());
    }

    private List<PlaceInfo> convertToPlaceInfos(
            Page<PlaceBulk> placesPage,
            Map<Long, String> placeIdToInfluencerName
    ) {
        return placesPage.getContent().stream()
                .map(place -> {
                    // map에서 조회되지 않은 placeId는 null로 처리
                    String influencerName = placeIdToInfluencerName.getOrDefault(place.getPlace().getId(), null);
                    return PlaceInfo.of(place, influencerName, isLikedPlace(place.getPlace().getId()));
                })
                .toList();
    }

    private Map<Long, String> getMapPlaceIdToInfluencerName(List<Video> videos) {
        return videos.stream()
                .collect(Collectors.toMap(
                        video -> video.getPlace().getId(),
                        video -> video.getInfluencer().getName(),
                        (existing, replacement) -> existing
                ));
    }

    private List<Long> getPlaceIds(Page<PlaceBulk> placesPage) {
        return placesPage.getContent().stream()
                .map(placeBulk -> placeBulk.getPlace().getId())
                .toList();
    }

    private Page<PlaceBulk> getPlacesByDistance(
            PlacesCoordinateCommand placesCoordinateCommand,
            List<String> categoryFilters,
            List<String> influencerFilters
    ) {
        return placeRepository.findPlacesByDistanceAndFilters(
                placesCoordinateCommand.topLeftLongitude(),
                placesCoordinateCommand.topLeftLatitude(),
                placesCoordinateCommand.bottomRightLongitude(),
                placesCoordinateCommand.bottomRightLatitude(),
                placesCoordinateCommand.longitude(),
                placesCoordinateCommand.latitude(),
                categoryFilters,
                influencerFilters,
                placesCoordinateCommand.pageable()
        );
    }

    public PlaceDetailInfo getPlaceDetailInfo(Long placeId) {
        var place = placeRepository.findPlaceById(placeId)
                .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        Video video = null;
        List<Video> videos = videoRepository.findByPlaceId(placeId);

        if (!videos.isEmpty()) {
            video = videos.get(0);
        }
        Influencer influencer = (video != null) ? video.getInfluencer() : null;

        Integer numOfLikes = reviewRepository.countByPlaceIdAndIsLikedTrue(placeId);
        Integer numOfDislikes = reviewRepository.countByPlaceIdAndIsLikedFalse(placeId);
        return PlaceDetailInfo.from(place, influencer, video, isLikedPlace(place.getPlace().getId()),
                numOfLikes, numOfDislikes);
    }

    public List<Long> createPlaces(List<Create> placeCommands) {
        var places = placeCommands.stream()
                .map(command -> {
                    if (Objects.isNull(command)) {
                        return null;
                    }
                    return command.toEntity();
                })
                .toList();
        var nonNullPlaces = places.stream()
                .filter(Objects::nonNull)
                .toList();
//        placeRepository.saveAll(nonNullPlaces);

        var savedPlacesId = places.stream()
                .map(place -> {
                    if (Objects.isNull(place)) {
                        return -1L;
                    }
                    return place.getPlace().getId();
                })
                .toList();

        return savedPlacesId;
    }

    public void likeToPlace(PlaceLikeCommand comm) {
        findOrCreateLikedPlace(comm.placeId(), comm.likes());
    }

    private void findOrCreateLikedPlace(Long placeId, boolean likes) {

        Long userId = getUserId().orElseThrow(
                () -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY)
        );

        LikedPlace likedPlace = likedPlaceRepository.findByUserIdAndPlaceId(userId, placeId)
                .orElseGet(() -> {
                    // 존재하지 않는 경우에만 Place 조회 후 likedPlace 생성
                    Place place = placeRepository.findById(placeId)
                            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
                    return new LikedPlace(user, place);
                });

        likedPlace.updateLike(likes);
        likedPlaceRepository.save(likedPlace);

    }

    private Optional<Long> getUserId() {
        Long userId = AuthorizationUtil.getUserId();
        return userId != null ? Optional.of(userId) : Optional.empty();
    }

    private boolean isLikedPlace(Long placeId) {
        Long userId = getUserId().orElseThrow(
                () -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY)
        );
        Optional<LikedPlace> likedPlace = likedPlaceRepository.findByUserIdAndPlaceId(userId, placeId);
        return likedPlace.map(LikedPlace::isLiked).orElse(false);
    }

    public Long createPlace(Create placeCommand) {
        var placeBulk = placeCommand.toEntity();
        return placeSaveRepository.save(placeBulk);
    }

    public PlaceMessageCommand getPlaceMessageCommand(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        Video video = videoRepository.findByPlaceId(placeId)
                .stream().findFirst().orElse(null);

        Influencer influencer = (video != null) ? video.getInfluencer() : null;

        return PlaceMessageCommand.of(place, influencer, video);
    }

    public Page<LikedPlaceInfo> getLikedPlaceInfo(Long userId, Pageable pageable) {
        Page<LikedPlace> placePage = likedPlaceRepository.findByUserIdAndIsLikedTrueWithPlace(
                userId, pageable);
        List<Long> placeIds = placePage.map(likedPlace -> likedPlace.getPlace().getId()).toList();
        List<Video> videos = videoRepository.findByPlaceIdInWithInfluencer(placeIds);
        Map<Long, String> placeIdToInfluencerName = getMapPlaceIdToInfluencerName(videos);

        List<LikedPlaceInfo> likedPlaceInfos = placePage.getContent().stream()
                .map(likedPlace -> {
                    String influencerName = placeIdToInfluencerName.getOrDefault(
                            likedPlace.getPlace().getId(), null);
                    return LikedPlaceInfo.of(likedPlace, influencerName);
                })
                .toList();

        return new PageImpl<>(likedPlaceInfos, pageable, placePage.getTotalElements());
    }
}
