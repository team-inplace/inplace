package my.inplace.application.place.command.dto;

import my.inplace.domain.place.Category;
import my.inplace.domain.place.Place;

public class PlaceCommand {

    public record Create(
        Long videoId,
        String placeName,
        Long category,
        String address,
        String x,
        String y,
        String googlePlaceId,
        Long kakaoPlaceId
    ) {
        public Place toEntity() {

            return new Place(
                placeName,
                category,
                address,
                x,
                y,
                googlePlaceId,
                kakaoPlaceId
            );
        }
    }

    public record Update(
        Long placeId,
        String placeName,
        Long category,
        String address,
        String x,
        String y,
        String googlePlaceId,
        Long kakaoPlaceId
    ) {

    }

    public record Like(
        Long placeId,
        Boolean likes
    ) {

    }

    public record CategoryCreate(
        String name,
        String engName,
        Long parentId
    ) {

        public Category toEntity() {
            return new Category(name, engName, parentId);
        }
    }

    public record CategoryUpdate(
        Long categoryId,
        String name,
        String engName,
        Long parentId
    ) {

    }
}
