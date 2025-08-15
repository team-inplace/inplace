package banner.query;

import banner.query.dto.BannerResult;
import banner.query.dto.BannerResult.Detail;
import banner.jpa.BannerJpaRepository;
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
}
