package my.inplace.application.banner.query.dto;


import my.inplace.domain.banner.Banner;
import java.time.LocalDateTime;

public class BannerResult {

    public record Detail(
        Long id,
        String imageUrl,
        Boolean isMain,
        Boolean isMobile,
        Long influencerId
    ) {// TODO
        public static Detail from(Banner banner) {
            return new Detail(
                banner.getId(),
                banner.getImgPath(),
                banner.getIsMain(),
                banner.getIsMobile(),
                banner.getInfluencerId()
            );
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
        public static Admin from(Banner banner) {
            return new Admin(
                banner.getId(),
                banner.getImgPath(),
                banner.getImgName(),
                banner.getIsFixed(),
                banner.getIsMain(),
                banner.getIsMobile(),
                banner.getStartDate(),
                banner.getEndDate()
            );
        }
    }
}
