package team7.inplace.place.persistence;

import static com.querydsl.core.types.ExpressionUtils.count;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.liked.likedPlace.domain.QLikedPlace;
import team7.inplace.place.domain.Category;
import team7.inplace.place.domain.QMenu;
import team7.inplace.place.domain.QMenuBoardPhoto;
import team7.inplace.place.domain.QOffDay;
import team7.inplace.place.domain.QOpenTime;
import team7.inplace.place.domain.QPlace;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.persistence.dto.PlaceQueryResult.Marker;
import team7.inplace.place.persistence.dto.PlaceQueryResult.SimplePlace;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_DetailedPlace;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_Location;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_Marker;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_Menu;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_MenuBordPhoto;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_OffDay;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_OpenTime;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_SimplePlace;
import team7.inplace.video.domain.QVideo;

@Repository
@RequiredArgsConstructor
public class PlaceReadRepositoryImpl implements PlaceReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<PlaceQueryResult.DetailedPlaceBulk> findDetailedPlaceById(Long placeId,
        Long userId) {
        var detailedPlace = jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.facility,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QPlace.place.category.stringValue(),
                QPlace.place.menuImgUrl,
                QPlace.place.menuUpdatedAt,
                QLikedPlace.likedPlace.isLiked.isNotNull()
            ))
            .from(QPlace.place)
            .leftJoin(QLikedPlace.likedPlace).on(QLikedPlace.likedPlace.placeId.eq(QPlace.place.id)
                .and(userId != null ?
                    QLikedPlace.likedPlace.userId.eq(userId) :
                    QLikedPlace.likedPlace.userId.isNull())
                .and(QLikedPlace.likedPlace.isLiked.isTrue()))
            .where(
                QPlace.place.id.eq(placeId),
                QPlace.place.deleteAt.isNull(),
                QLikedPlace.likedPlace.deleteAt.isNull()
            )
            .fetchOne();
        if (detailedPlace == null) {
            return Optional.empty();
        }
        var menus = findMenusByPlaceId(placeId);
        var openTimes = findOpenTimesByPlaceId(placeId);
        var offDays = findOffDaysByPlaceId(placeId);
        var menuBoards = findMenuBoardPhotosByPlaceId(placeId);

        return Optional.of(new PlaceQueryResult.DetailedPlaceBulk(
            detailedPlace,
            openTimes,
            menus,
            menuBoards,
            offDays
        ));
    }

    private List<PlaceQueryResult.Menu> findMenusByPlaceId(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_Menu(
                QMenu.menu.placeId,
                QMenu.menu.menuName,
                QMenu.menu.price,
                QMenu.menu.menuImgUrl,
                QMenu.menu.description,
                QMenu.menu.recommend
            ))
            .from(QMenu.menu)
            .where(QMenu.menu.placeId.eq(placeId))
            .fetch();
    }

    private List<PlaceQueryResult.OpenTime> findOpenTimesByPlaceId(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_OpenTime(
                QOpenTime.openTime.placeId,
                QOpenTime.openTime.timeName,
                QOpenTime.openTime.timeSE,
                QOpenTime.openTime.dayOfWeek
            ))
            .from(QOpenTime.openTime)
            .where(QOpenTime.openTime.placeId.eq(placeId))
            .fetch();
    }

    private List<PlaceQueryResult.OffDay> findOffDaysByPlaceId(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_OffDay(
                QOffDay.offDay.placeId,
                QOffDay.offDay.holidayName,
                QOffDay.offDay.weekAndDay,
                QOffDay.offDay.temporaryHolidays
            ))
            .from(QOffDay.offDay)
            .where(QOffDay.offDay.placeId.eq(placeId))
            .fetch();
    }

    private List<PlaceQueryResult.MenuBordPhoto> findMenuBoardPhotosByPlaceId(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_MenuBordPhoto(
                QMenuBoardPhoto.menuBoardPhoto.placeId,
                QMenuBoardPhoto.menuBoardPhoto.url
            ))
            .from(QMenuBoardPhoto.menuBoardPhoto)
            .where(QMenuBoardPhoto.menuBoardPhoto.placeId.eq(placeId))
            .fetch();
    }

    @Override
    public Page<PlaceQueryResult.DetailedPlace> findPlacesInMapRangeWithPaging(
        Double topLeftLongitude, Double topLeftLatitude,
        Double bottomRightLongitude, Double bottomRightLatitude,
        Double longitude, Double latitude,
        List<Category> categoryFilters, List<String> influencerFilters,
        Pageable pageable,
        Long userId
    ) {
        var filterExpression = createFilters(categoryFilters, influencerFilters);
        /* 조건에 맞는 장소 ID 목록 조회 */
        List<Long> filteredPlaceId = jpaQueryFactory
            .select(QPlace.place.id).distinct()
            .from(QPlace.place)
            .leftJoin(QVideo.video).on(QVideo.video.placeId.eq(QPlace.place.id))
            .leftJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(
                QPlace.place.coordinate.longitude.between(topLeftLongitude, bottomRightLongitude),
                QPlace.place.coordinate.latitude.between(bottomRightLatitude, topLeftLatitude),
                filterExpression,
                QPlace.place.deleteAt.isNull(),
                QVideo.video.deleteAt.isNull(),
                QInfluencer.influencer.deleteAt.isNull()
            ).fetch();
        /* 필터링 된 장소 ID 목록으로 장소 상세 정보 조회 */
        List<PlaceQueryResult.DetailedPlace> places = jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.facility,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QPlace.place.category.stringValue(),
                QPlace.place.menuImgUrl,
                QPlace.place.menuUpdatedAt,
                QLikedPlace.likedPlace.isLiked.isNotNull()
            )).distinct()
            .from(QPlace.place)
            .leftJoin(QVideo.video).on(QVideo.video.placeId.eq(QPlace.place.id))
            .leftJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .leftJoin(QLikedPlace.likedPlace).on(QLikedPlace.likedPlace.placeId.eq(QPlace.place.id)
                .and(userId != null ?
                    QLikedPlace.likedPlace.userId.eq(userId) :
                    QLikedPlace.likedPlace.userId.isNull())
                .and(QLikedPlace.likedPlace.isLiked.isTrue()))
            .where(
                QPlace.place.coordinate.longitude.between(topLeftLongitude, bottomRightLongitude),
                QPlace.place.coordinate.latitude.between(bottomRightLatitude, topLeftLatitude),
                filterExpression,
                QPlace.place.deleteAt.isNull(),
                QVideo.video.deleteAt.isNull(),
                QInfluencer.influencer.deleteAt.isNull(),
                QLikedPlace.likedPlace.deleteAt.isNull()
            )
            .orderBy(QPlace.place.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        if (places.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        return PageableExecutionUtils.getPage(places, pageable, filteredPlaceId::size);
    }

    @Override
    public List<PlaceQueryResult.Location> findPlaceLocationsInMapRange(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude,
        List<Category> categoryFilters,
        List<String> influencerFilters
    ) {
        var filterExpression = createFilters(categoryFilters, influencerFilters);

        List<Long> filteredPlaceId = jpaQueryFactory
            .select(QPlace.place.id).distinct()
            .from(QVideo.video)
            .leftJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .leftJoin(QPlace.place).on(QVideo.video.placeId.eq(QPlace.place.id))
            .where(
                QPlace.place.coordinate.longitude.between(topLeftLongitude, bottomRightLongitude),
                QPlace.place.coordinate.latitude.between(bottomRightLatitude, topLeftLatitude),
                filterExpression,
                QVideo.video.placeId.isNotNull(),
                QVideo.video.deleteAt.isNull(),
                QInfluencer.influencer.deleteAt.isNull(),
                QPlace.place.deleteAt.isNull()
            ).fetch();

        return jpaQueryFactory
            .select(new QPlaceQueryResult_Location(
                    QPlace.place.id,
                    QPlace.place.coordinate.longitude,
                    QPlace.place.coordinate.latitude
                )
            )
            .from(QPlace.place)
            .where(QPlace.place.id.in(filteredPlaceId))
            .fetch();
    }

    private BooleanBuilder createFilters(
        List<Category> categoryFilters, List<String> influencerFilters
    ) {
        BooleanBuilder expression = new BooleanBuilder();
        if (categoryFilters != null && categoryFilters.size() > 0) {
            expression.and(QPlace.place.category.in(categoryFilters));
        }

        if (influencerFilters != null && influencerFilters.size() > 0) {
            expression.and(QInfluencer.influencer.name.in(influencerFilters));
        }

        return expression;
    }

    @Override
    public Optional<SimplePlace> findSimplePlaceById(Long placeId) {
        var simplePlace = jpaQueryFactory
            .select(new QPlaceQueryResult_SimplePlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3
            ))
            .from(QPlace.place)
            .where(
                QPlace.place.id.eq(placeId),
                QPlace.place.deleteAt.isNull()
            )
            .fetchOne();
        if (simplePlace == null) {
            return Optional.empty();
        }
        return Optional.of(simplePlace);
    }


    @Override
    public Page<PlaceQueryResult.DetailedPlaceBulk> findLikedPlacesByUserIdWithPaging(Long userId,
        Pageable pageable) {
        var likedPlaceCount = jpaQueryFactory
            .select(count(QLikedPlace.likedPlace.id))
            .from(QLikedPlace.likedPlace)
            .where(
                QLikedPlace.likedPlace.userId.eq(userId),
                QLikedPlace.likedPlace.isLiked.isTrue(),
                QLikedPlace.likedPlace.deleteAt.isNull()
            )
            .fetchOne();
        if (likedPlaceCount == null || likedPlaceCount == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        var likedPlaces = jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.facility,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QPlace.place.category.stringValue(),
                QPlace.place.menuImgUrl,
                QPlace.place.menuUpdatedAt,
                QLikedPlace.likedPlace.isLiked.isTrue()
            ))
            .from(QLikedPlace.likedPlace)
            .leftJoin(QPlace.place).on(QLikedPlace.likedPlace.placeId.eq(QPlace.place.id))
            .where(
                QLikedPlace.likedPlace.userId.eq(userId),
                QLikedPlace.likedPlace.deleteAt.isNull(),
                QPlace.place.deleteAt.isNull()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        var placeIds = likedPlaces.stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var offdays = findOffDaysByPlaceIds(placeIds);
        var menus = findMenusByPlaceIds(placeIds);
        var openTimes = findOpenTimesByPlaceIds(placeIds);
        var menuBoardPhotos = findMenuBoardPhotosByPlaceIds(placeIds);

        var detailedPlaceBulks = likedPlaces.stream()
            .map(place -> new PlaceQueryResult.DetailedPlaceBulk(
                place,
                openTimes.get(place.placeId()),
                menus.get(place.placeId()),
                menuBoardPhotos.get(place.placeId()),
                offdays.get(place.placeId())
            ))
            .toList();

        return PageableExecutionUtils.getPage(detailedPlaceBulks, pageable,
            likedPlaceCount::intValue);
    }

    @Override
    public Marker findPlaceMarkerById(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_Marker(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.category.stringValue(),
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.menuImgUrl
            ))
            .from(QPlace.place)
            .where(QPlace.place.id.eq(placeId))
            .fetchOne();
    }

    private Map<Long, List<PlaceQueryResult.OffDay>> findOffDaysByPlaceIds(List<Long> placeIds) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_OffDay(
                QOffDay.offDay.placeId,
                QOffDay.offDay.holidayName,
                QOffDay.offDay.weekAndDay,
                QOffDay.offDay.temporaryHolidays
            ))
            .from(QOffDay.offDay)
            .where(QOffDay.offDay.placeId.in(placeIds))
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(PlaceQueryResult.OffDay::placeId));
    }

    private Map<Long, List<PlaceQueryResult.Menu>> findMenusByPlaceIds(List<Long> placeIds) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_Menu(
                QMenu.menu.placeId,
                QMenu.menu.menuName,
                QMenu.menu.price,
                QMenu.menu.menuImgUrl,
                QMenu.menu.description,
                QMenu.menu.recommend
            ))
            .from(QMenu.menu)
            .where(QMenu.menu.placeId.in(placeIds))
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(PlaceQueryResult.Menu::placeId));
    }

    private Map<Long, List<PlaceQueryResult.OpenTime>> findOpenTimesByPlaceIds(
        List<Long> placeIds) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_OpenTime(
                QOpenTime.openTime.placeId,
                QOpenTime.openTime.timeName,
                QOpenTime.openTime.timeSE,
                QOpenTime.openTime.dayOfWeek
            ))
            .from(QOpenTime.openTime)
            .where(QOpenTime.openTime.placeId.in(placeIds))
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(PlaceQueryResult.OpenTime::placeId));
    }

    private Map<Long, List<PlaceQueryResult.MenuBordPhoto>> findMenuBoardPhotosByPlaceIds(
        List<Long> placeIds) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_MenuBordPhoto(
                QMenuBoardPhoto.menuBoardPhoto.placeId,
                QMenuBoardPhoto.menuBoardPhoto.url
            ))
            .from(QMenuBoardPhoto.menuBoardPhoto)
            .where(QMenuBoardPhoto.menuBoardPhoto.placeId.in(placeIds))
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(PlaceQueryResult.MenuBordPhoto::placeId));
    }
}
