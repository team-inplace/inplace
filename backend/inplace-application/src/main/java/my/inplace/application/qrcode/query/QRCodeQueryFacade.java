package my.inplace.application.qrcode.query;

import my.inplace.application.annotation.Facade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.application.place.query.PlaceQueryService;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@Slf4j
@RequiredArgsConstructor
public class QRCodeQueryFacade {

    private final QrCodeQueryService qrCodeCommandService;
    private final PlaceQueryService placeService;

    public byte[] getQRCode(Long placeId, Integer width, Integer height) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var place = placeService.getPlaceInfo(userId, placeId);

        return qrCodeCommandService.generateQRCode(place.kakaoPlaceId(), width, height);
    }

}
