package my.inplace.api.banner;

import my.inplace.application.banner.query.dto.BannerResult;
import java.time.LocalDateTime;

public class BannerResponse {

    public record Info(
        Long id,
        String imageUrl,
        Boolean isMain,
        Boolean isMobile,
        Long influencerId
    ) {

        public static Info from(BannerResult.Detail banner) {
            return new Info(banner.id(), banner.imageUrl(), banner.isMain(), banner.isMobile(),
                banner.influencerId());
        }
    }

    public record Admin(
        Long id,
        String imgPath,
        String imgName,
        Boolean isFixed,
        Boolean isMain,
        Boolean isMobile,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        public static Admin from(BannerResult.Admin banner) {
            return new Admin(
                banner.id(),
                banner.imgPath(),
                banner.imgName(),
                banner.isFixed(),
                banner.isMain(),
                banner.isMobile(),
                banner.startDate(),
                banner.endDate()
            );
        }
    }
}
