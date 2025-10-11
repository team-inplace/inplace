package my.inplace.infra.place.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record LocationTemplate(
    @NonNull @JsonProperty("object_type") String objectType,
    @NonNull String address,
    @Nullable @JsonProperty("address_title") String addressTitle,
    @NonNull Content content,
    @Nullable @JsonProperty("buttons") List<Button> buttons
) {

    private final static String BUTTON_NAME = "자세히 보기";
    private final static String OBJECT_TYPE = "location";

//    public static LocationTemplate of(String frontEndUrl, PlaceC placeMessageCommand) {
//        Link link = Link.of(frontEndUrl + "/detail/" + placeMessageCommand.placeId());
//        return new LocationTemplate(
//            OBJECT_TYPE,
//            placeMessageCommand.address(),
//            placeMessageCommand.title(),
//            Content.of(placeMessageCommand, placeMessageCommand.description(), link),
//            List.of(Button.of(BUTTON_NAME, link))
//        );
//    }
}
