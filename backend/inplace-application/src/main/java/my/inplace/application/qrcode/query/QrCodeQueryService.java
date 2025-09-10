package my.inplace.application.qrcode.query;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class QrCodeQueryService {

    public byte[] generateQRCode(Long kakaoPlaceId, Integer width, Integer height) {
        String qrCodeData = "http://place.map.kakao.com/" + kakaoPlaceId;

        width = (width == null) ? 200 : width;
        height = (height == null) ? 200 : height;
        Map<EncodeHintType, Object> hints = new HashMap<>();

        hints.put(EncodeHintType.MARGIN, 0);

        BitMatrix bitMatrix;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            bitMatrix = multiFormatWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, width, height, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (IOException | WriterException e) {
            throw new IllegalArgumentException("QR 코드 생성 중 오류가 발생했습니다.", e);
        }
    }
}
