package banner.query.dto;


import banner.Banner;

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
}
