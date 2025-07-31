package team7.inplace.kakao.domain;

import static lombok.AccessLevel.PROTECTED;

import exception.InplaceException;
import exception.code.KakaoMessageErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class MessageSendHistory {

    private static final Integer MAX_MESSAGE_SEND_COUNT = 10;
    private Long userId;
    private Integer messageSendCount;

    private MessageSendHistory(Long userId) {
        this.userId = userId;
        this.messageSendCount = 0;
    }

    public static MessageSendHistory of(Long userId) {
        return new MessageSendHistory(userId);
    }

    public void sendMessage() {
        if (this.messageSendCount >= MAX_MESSAGE_SEND_COUNT) {
            throw InplaceException.of(KakaoMessageErrorCode.MESSAGE_SEND_COUNT_EXCEEDED);
        }
        this.messageSendCount++;
    }
}
