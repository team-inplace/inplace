package my.inplace.api.crawling.dto;


import my.inplace.application.crawling.command.PlaceRegistrationCommand;

public record PlaceRegistrationRequest(
    Long videoId,
    Long placeUUID,
    String category
) {

    public PlaceRegistrationCommand toCommand() {
        return new PlaceRegistrationCommand(videoId, placeUUID, category);
    }
}
