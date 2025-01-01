package team7.inplace.placeMessage.application;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import team7.inplace.global.annotation.Facade;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.oauthToken.application.OauthTokenService;
import team7.inplace.place.application.PlaceService;
import team7.inplace.placeMessage.application.command.PlaceMessageCommand;
import team7.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class PlaceMessageFacade {

    private final PlaceService placeService;
    private final OauthTokenService oauthTokenService;
    private final KakaoMessageService kakaoMessageService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final UserReviewUuidService userReviewLinkService;

    // 여기서 로그인된 사용자 id가져오고 uuid로 리뷰 요청 링크 만들어야 하는듯?
    public void sendPlaceMessage(Long placeId) throws InplaceException {
        if (AuthorizationUtil.isNotLoginUser()) {
            throw InplaceException.of(UserErrorCode.NOT_FOUND);
        }

        String oauthToken = oauthTokenService.findOAuthTokenByUserId(AuthorizationUtil.getUserId());
        PlaceMessageCommand placeMessageCommand = placeService.getPlaceMessageCommand(placeId);
        kakaoMessageService.sendLocationMessageToMe(oauthToken, placeMessageCommand);

        String uuid = userReviewLinkService.generateReviewUuid(AuthorizationUtil.getUserId(),
            placeId);
        scheduledExecutorService.schedule(
            () -> kakaoMessageService.sendFeedMessageToMe(oauthToken, placeMessageCommand, uuid), 1,
            TimeUnit.MINUTES);
    }
}
