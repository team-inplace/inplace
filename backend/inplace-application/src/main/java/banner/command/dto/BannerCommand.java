package banner.command.dto;

import banner.Banner;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

public class BannerCommand {

    public record Create(
        String imgName,
        MultipartFile imageFile,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean isFixed,
        Boolean isMain,
        Boolean isMobile
    ) {

        public Banner toEntity(String imgPath, Long influencerId) {
            return Banner.of(imgName, imgPath, startDate, endDate, isFixed, isMain, isMobile,
                influencerId);
        }
    }
}
