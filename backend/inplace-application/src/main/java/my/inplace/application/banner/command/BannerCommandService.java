package my.inplace.application.banner.command;

import my.inplace.infra.banner.BannerS3Repository;
import my.inplace.application.banner.command.dto.BannerCommand;
import my.inplace.infra.banner.jpa.BannerJpaRepository;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.BannerErrorCode;
import my.inplace.infra.influencer.jpa.InfluencerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerCommandService {

    private final BannerS3Repository bannerS3Repository;
    private final BannerJpaRepository bannerJpaRepository;
    private final InfluencerJpaRepository influencerJpaRepository;

    public void uploadBanner(BannerCommand.Create command) {
        var imgPath = bannerS3Repository.uploadBanner(command.imageFile());
        Long influencerId = null;
        if (!command.isMain()) {
            influencerId = influencerJpaRepository.findIdByName(command.imgName())
                .orElseThrow(() -> InplaceException.of(BannerErrorCode.INFLUENCER_NOT_FOUND));
        }
        var banner = command.toEntity(imgPath, influencerId);
        bannerJpaRepository.save(banner);
    }

    public void deleteBanner(Long id) {
        var banner = bannerJpaRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(BannerErrorCode.NOT_FOUND));
        bannerS3Repository.deleteBanner(banner.getImgPath());
        bannerJpaRepository.delete(banner);
    }
}
