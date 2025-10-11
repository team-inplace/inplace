package my.inplace.application.place.command;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.CategoryErrorCode;
import my.inplace.common.exception.code.PlaceErrorCode;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.place.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.domain.place.LikedPlace;
import my.inplace.domain.place.Place;
import my.inplace.domain.place.PlaceVideo;
import my.inplace.application.place.command.dto.PlaceCommand;
import my.inplace.infra.place.jpa.CategoryJpaRepository;
import my.inplace.infra.place.jpa.LikedPlaceJpaRepository;
import my.inplace.infra.place.jpa.PlaceJpaRepository;
import my.inplace.infra.place.jpa.PlaceVideoJpaRepository;
import my.inplace.security.util.AuthorizationUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceCommandService {

    private final PlaceJpaRepository placeJpaRepository;
    private final LikedPlaceJpaRepository likedPlaceJPARepository;
    private final PlaceVideoJpaRepository placeVideoJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    /*
     * 장소 및 장소 좋아요 관련 기능
     */
    public void createPlace(PlaceCommand.Create placeCommand) {
        var place = placeJpaRepository.findPlaceByKakaoPlaceId(placeCommand.kakaoPlaceId())
            .orElseGet(() -> {
                var newPlace = placeCommand.toEntity();
                return placeJpaRepository.save(newPlace);
            });

        var placeVideo = new PlaceVideo(place.getId(), placeCommand.videoId());
        placeVideoJpaRepository.save(placeVideo);
    }

    public Long updatePlaceInfo(PlaceCommand.Update command) {
        var placeId = command.placeId();
        Place place = placeJpaRepository.findById(placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        place.update(
            command.placeName(),
            command.category(),
            command.address(),
            command.x(),
            command.y(),
            command.googlePlaceId(),
            command.kakaoPlaceId()
        );

        return place.getId();
    }

    public void deletePlaceById(Long placeId) {
        placeJpaRepository.deleteById(placeId);
    }

    public void updateLikedPlace(PlaceCommand.Like command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        var placeId = command.placeId();

        LikedPlace likedPlace = likedPlaceJPARepository.findByUserIdAndPlaceId(userId, placeId)
            .orElseGet(() -> LikedPlace.from(userId, placeId));

        likedPlace.updateLike(command.likes());
        likedPlaceJPARepository.save(likedPlace);
    }


    /*
     * 카테고리 관련 기능
     */

    public void createCategory(PlaceCommand.CategoryCreate command) {
        var category = command.toEntity();
        categoryJpaRepository.save(category);
    }

    public void updateCategory(PlaceCommand.CategoryUpdate command) {
        var categoryId = command.categoryId();

        Category category = categoryJpaRepository.findById(categoryId)
            .orElseThrow(() -> InplaceException.of(CategoryErrorCode.NOT_FOUND));

        category.updateInfo(command.name(), command.engName(), command.parentId());
    }

    public void deleteCategoryById(Long categoryId) {
        if (!categoryJpaRepository.existsById(categoryId)) {
            throw InplaceException.of(CategoryErrorCode.NOT_FOUND);
        }
        categoryJpaRepository.deleteById(categoryId);
    }
}
