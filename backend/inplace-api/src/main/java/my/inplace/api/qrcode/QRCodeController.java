package my.inplace.api.qrcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import my.inplace.application.qrcode.query.QRCodeQueryFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qrcodes")
public class QRCodeController implements QRCodeControllerApiSpec {

    private final QRCodeQueryFacade qrCodeQueryFacade;

    @Override
    @GetMapping()
    public ResponseEntity<byte[]> getQRCode(
        @RequestParam Long placeId,
        @RequestParam(required = false, defaultValue = "200")
        Integer width,
        @RequestParam(required = false, defaultValue = "200")
        Integer height
    ) {
        var qrCode = qrCodeQueryFacade.getQRCode(placeId, width, height);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }
}
