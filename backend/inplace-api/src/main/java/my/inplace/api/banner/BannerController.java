package my.inplace.api.banner;

import my.inplace.application.banner.command.BannerCommandService;
import my.inplace.application.banner.query.BannerQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banners")
public class BannerController {

    private final BannerCommandService bannerCommandService;
    private final BannerQueryService bannerQueryService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')") // Todo 어노테이션 확인
    public void saveBanner(BannerRequest.Create request) {

        bannerCommandService.uploadBanner(request.toCommand());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBanner(@PathVariable Long id) {
        bannerCommandService.deleteBanner(id);
    }

    @GetMapping()
    public ResponseEntity<List<BannerResponse.Info>> getBanners() {
        var banners = bannerQueryService.getBanners();

        var response = banners.stream()
            .map(BannerResponse.Info::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}