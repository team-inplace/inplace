package team7.inplace.kakao.application;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import team7.inplace.global.annotation.Facade;
import team7.inplace.kakao.application.command.PlaceMessageCommand;
import team7.inplace.place.application.PlaceService;
import team7.inplace.review.application.ReviewInvitationService;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.token.application.OauthTokenService;

@Facade
@RequiredArgsConstructor
public class PlaceMessageFacade {

    private final OauthTokenService oauthTokenService;
    private final ScheduledExecutorService scheduledExecutorService;

    private final PlaceService placeService;
    private final ReviewInvitationService userReviewLinkService;
    private final KakaoMessageService kakaoMessageService;

    public Mono<Void> sendPlaceMessage(Long placeId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();

        String oauthToken = oauthTokenService.findOAuthTokenByUserId(userId);

        var placeInfo = placeService.getPlaceMessageCommand(placeId, userId);

        var placeMessageCommand = PlaceMessageCommand.from(placeInfo);
        Mono<Void> placeMessageMono = kakaoMessageService.sendLocationMessageToMe(userId,
            oauthToken, placeMessageCommand);

        String uuid = userReviewLinkService.generateReviewUuid(userId,
            placeId);
        if (uuid == null) {
            return placeMessageMono;
        }

        return placeMessageMono.doOnSuccess(response -> {
            scheduledExecutorService.schedule(
                () -> kakaoMessageService.sendFeedMessageToMe(oauthToken, placeMessageCommand,
                    uuid), 1,
                TimeUnit.MINUTES);
        });
    }
}
