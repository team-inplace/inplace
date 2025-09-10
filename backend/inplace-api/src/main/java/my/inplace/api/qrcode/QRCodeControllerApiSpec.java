package my.inplace.api.qrcode;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/qrcodes")
@Tag(name = "QRCode 관련 API", description = "QRCode 관련 API")
public interface QRCodeControllerApiSpec {

    @GetMapping()
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "QRCode 이미지를 반환합니다.")
        }
    )
    ResponseEntity<byte[]> getQRCode(
        @RequestParam Long placeId,
        @RequestParam @Min(50) @Max(500) Integer width,
        @RequestParam @Min(50) @Max(500) Integer height
    );
}
