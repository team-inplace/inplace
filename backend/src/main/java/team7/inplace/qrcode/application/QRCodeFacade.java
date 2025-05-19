package team7.inplace.qrcode.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team7.inplace.global.annotation.Facade;
import team7.inplace.place.application.PlaceService;
import team7.inplace.security.util.AuthorizationUtil;

@Facade
@Slf4j
@RequiredArgsConstructor
public class QRCodeFacade {

    private final QRCodeService qrCodeService;
    private final PlaceService placeService;

    public byte[] getQRCode(Long placeId, Integer width, Integer height) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var place = placeService.getPlaceInfo(userId, placeId);

        return qrCodeService.generateQRCode(place.kakaoPlaceId(), width, height);
    }

}
