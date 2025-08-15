package banner.command;

import banner.BannerS3Repository;
import banner.command.dto.BannerCommand;
import banner.jpa.BannerJpaRepository;
import exception.InplaceException;
import exception.code.BannerErrorCode;
import influencer.jpa.InfluencerJpaRepository;
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
