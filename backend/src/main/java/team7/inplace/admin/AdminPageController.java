package team7.inplace.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team7.inplace.admin.banner.persistence.BannerRepository;
import team7.inplace.global.properties.GoogleApiProperties;
import team7.inplace.global.properties.KakaoApiProperties;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.place.domain.Category;
import team7.inplace.video.domain.Video;
import team7.inplace.video.persistence.VideoRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final KakaoApiProperties kakaoApiProperties;
    private final GoogleApiProperties googleApiProperties;
    private final VideoRepository videoRepository;
    private final BannerRepository bannerRepository;
    private final InfluencerRepository influencerRepository;

    @GetMapping("/video")
    public String getVideos(
        @RequestParam(required = false) Long influencerId,
        @PageableDefault Pageable pageable, Model model
    ) {
        List<Influencer> influencers = influencerRepository.findAll();
        model.addAttribute("influencers", influencers);
        model.addAttribute("selectedInfluencerId", influencerId);
        Page<Video> videoPage = (influencerId != null)
            ? videoRepository.findAllByPlaceIsNullAndInfluencerId(pageable, influencerId)
            : videoRepository.findAllByPlaceIdIsNull(pageable);
        model.addAttribute("videos", videoPage.getContent());
        model.addAttribute("currentPage", videoPage.getNumber());
        model.addAttribute("totalPages", videoPage.getTotalPages());
        model.addAttribute("isFirst", videoPage.isFirst());
        model.addAttribute("isLast", videoPage.isLast());
        model.addAttribute("kakaoApiKey", kakaoApiProperties.jsKey());
        model.addAttribute("googleApiKey", googleApiProperties.placeKey1());

        var categories = Category.values();
        model.addAttribute("categories", categories);
        return "admin/video.html";
    }

    @GetMapping("/banner")
    public String getBanners(Model model) {
        var banners = bannerRepository.findAll();

        model.addAttribute("banners", banners);
        return "admin/banner.html";
    }

    @GetMapping("/influencer/new")
    public String getIncluencers(Model model) {
        model.addAttribute("youtubeApiKey", googleApiProperties.crawlingKey());
        return "admin/influencer/new.html";
    }

    @GetMapping("/influencer/list")
    public String getInfluencerList(Model model) {
        model.addAttribute("influencers", influencerRepository.findAll());
        return "admin/influencer/list.html";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "admin/main.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "admin/login.html";
    }
}
