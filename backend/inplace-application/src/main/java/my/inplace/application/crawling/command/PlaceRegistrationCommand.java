package my.inplace.application.crawling.command;

public record PlaceRegistrationCommand(
    Long videoId,
    Long placeUUID,
    String category
) {

}
