package team7.inplace.kakao.application.command;

import java.util.stream.Collectors;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.video.persistence.dto.VideoQueryResult.SimpleVideo;

public record PlaceMessageCommand(
    Long placeId,
    String title,
    String address,
    String imageUrl,
    String description
) {

    public static PlaceMessageCommand from(PlaceInfo.Simple place) {

        String influencerName = place.video().stream()
            .map(SimpleVideo::influencerName).distinct()
            .collect(Collectors.joining(", "));
        String videoUUID = place.video().get(0).videoUUID();
        return new PlaceMessageCommand(
            place.place().placeId(),
            place.place().placeName(),
            place.place().address1() + " " + place.place().address2() + " " + place.place()
                .address3(),
            String.format("https://img.youtube.com/vi/%s/maxresdefault.jpg", videoUUID),
            influencerName + "|" + place.place().placeName()
        );
    }
}
