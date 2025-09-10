package my.inplace.application.banner.query;

import my.inplace.application.banner.query.dto.BannerResult;
import my.inplace.application.banner.query.dto.BannerResult.Detail;
import my.inplace.infra.banner.jpa.BannerJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerQueryService {

    private final BannerJpaRepository bannerJpaRepository;

    public List<Detail> getBanners() {
        var now = LocalDateTime.now();
        var banners = bannerJpaRepository.findActiveBanner(now);

        return banners.stream()
            .sorted((a, b) -> Boolean.compare(b.getIsFixed(), a.getIsFixed()))
            .map(BannerResult.Detail::from)
            .toList();
    }

    public List<BannerResult.Admin> getAdminBanners() {
        return bannerJpaRepository.findAll()
            .stream()
            .map(BannerResult.Admin::from)
            .toList();
    }
}
