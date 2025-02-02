package team7.inplace.place.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import team7.inplace.place.application.command.PlaceMessageCommand;

import java.util.List;

public record FeedTemplate(
        @JsonProperty("object_type") String objectType,
        Content content,
        @JsonProperty("buttons") List<Button> buttons
) {

    private final static String BUTTON_NAME = "리뷰 남기기";
    private final static String OBJECT_TYPE = "feed";

    public static FeedTemplate of(String frontEndUrl, PlaceMessageCommand placeMessageCommand) {
        Link link = Link.of(frontEndUrl + "/places/" + placeMessageCommand.placeId() + "/reviews");
        return new FeedTemplate(
                OBJECT_TYPE,
                Content.of(placeMessageCommand, "함께 해주셔서 감사합니다! 리뷰 부탁드려요.",
                        link),
                List.of(Button.of(BUTTON_NAME, link))
        );
    }
}
