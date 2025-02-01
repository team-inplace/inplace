package team7.inplace.place.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;

public class PlaceQueryResult {
    public record DetailedPlaceBulk(
            DetailedPlace detailedPlace,
            List<OpenTime> openTimes,
            List<Menu> menus,
            List<MenuBordPhoto> menuBordPhotos,
            List<OffDay> offDays
    ) {
    }

    public record DetailedPlace(
            Long placeId,
            String placeName,
            String facility,
            String address1,
            String address2,
            String address3,
            Double longitude,
            Double latitude,
            String category,
            String menuImgUrl,
            LocalDateTime menuUpdatedAt,
            Boolean isLiked
    ) {
        @QueryProjection
        public DetailedPlace {
        }
    }

    public record SimplePlace(
            Long placeId,
            String placeName,
            String address1,
            String address2,
            String address3
    ) {
        @QueryProjection
        public SimplePlace {
        }
    }

    public record OpenTime(
            Long placeId,
            String timeName,
            String timeSE,
            String dayOfWeek
    ) {
        @QueryProjection
        public OpenTime {
        }
    }

    public record Menu(
            Long placeId,
            String name,
            String price,
            String imgUrl,
            String description,
            boolean recommend
    ) {
        @QueryProjection
        public Menu {
        }
    }

    public record MenuBordPhoto(
            Long placeId,
            String imgUrl
    ) {
        @QueryProjection
        public MenuBordPhoto {
        }
    }

    public record OffDay(
            Long placeId,
            String holidayName,
            String weekAndDay,
            String temporaryHolidays
    ) {
        @QueryProjection
        public OffDay {
        }
    }

    public record Location(
        Long placeId,
        Double longitude,
        Double latitude
    ) {
        @QueryProjection
        public Location {
        }
    }

    public record Marker(
        Long placeId,
        String placeName,
        String category,
        String address1,
        String address2,
        String address3,
        String menuImgUrl
    ) {
        @QueryProjection
        public Marker {
        }
    }
}
