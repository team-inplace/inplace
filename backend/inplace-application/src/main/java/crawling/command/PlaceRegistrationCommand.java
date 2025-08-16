package crawling.command;

public record PlaceRegistrationCommand(
    Long videoId,
    Long placeUUID,
    String category
) {

}
