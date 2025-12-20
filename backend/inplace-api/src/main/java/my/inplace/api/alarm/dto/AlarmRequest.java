package my.inplace.api.alarm.dto;

public record AlarmRequest(
    String fcmToken,
    String expoToken
) {

}
