package team7.inplace.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team7.inplace.admin.banner.persistence.BannerRepository;
import team7.inplace.admin.dto.CategoryForm;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.CategoryErrorCode;
import team7.inplace.global.exception.code.PlaceErrorCode;
import team7.inplace.global.properties.GoogleApiProperties;
import team7.inplace.global.properties.KakaoApiProperties;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.place.application.PlaceService;
import team7.inplace.place.domain.Category;
import team7.inplace.place.persistence.CategoryRepository;
import team7.inplace.video.application.VideoService;
import team7.inplace.video.domain.Video;
import team7.inplace.video.persistence.VideoRepository;
import team7.inplace.video.persistence.dto.VideoFilterCondition;
import team7.inplace.video.presentation.dto.VideoResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final KakaoApiProperties kakaoApiProperties;
    private final GoogleApiProperties googleApiProperties;
    private final VideoRepository videoRepository;
    private final BannerRepository bannerRepository;
    private final InfluencerRepository influencerRepository;
    private final CategoryRepository categoryRepository;
    private final VideoService videoService;
    private final PlaceService placeService;

    @GetMapping("/video")
    public String getVideos(
        @RequestParam(required = false) Long influencerId,
        @RequestParam(required = false, defaultValue = "false") Boolean videoRegistration,
        @PageableDefault Pageable pageable, Model model
    ) {
        Page<VideoResponse.Admin> videoPage = videoService
            .getAdminVideosByCondition(VideoFilterCondition.of(videoRegistration, influencerId), pageable)
            .map(VideoResponse.Admin::from);

        model.addAttribute("videoPage", videoPage);
        model.addAttribute("influencers", influencerRepository.findAll());
        model.addAttribute("selectedInfluencerId", influencerId);
        model.addAttribute("videoRegistration", videoRegistration);
        model.addAttribute("kakaoApiKey", kakaoApiProperties.jsKey());
        model.addAttribute("googleApiKey", googleApiProperties.placeKey1());
        model.addAttribute("categories", categoryRepository.findSubCategories());
        return "admin/video.html";
    }

    @GetMapping("/banner")
    public String getBanners(Model model) {
        var banners = bannerRepository.findAll();

        model.addAttribute("banners", banners);
        return "admin/banner.html";
    }

    @GetMapping("/influencer/new")
    public String getInfluencers(Model model) {
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

    @GetMapping("/category")
    public String getCategoryPage(Model model) {
        model.addAttribute("parentCategories", categoryRepository.findParentCategories());
        return "admin/category/list.html";
    }

    @GetMapping("/category/{categoryId}/edit")
    public String getCategoryEditForm(@PathVariable Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> InplaceException.of(CategoryErrorCode.NOT_FOUND));
        CategoryForm categoryForm = CategoryForm.of(category);
        model.addAttribute("categoryForm", categoryForm);
        return "admin/category/edit.html";
    }

    @PostMapping("/category/{categoryId}/edit")
    public String editCategory(@PathVariable Long categoryId, @ModelAttribute CategoryForm categoryForm) {
        placeService.updateCategory(categoryId, categoryForm);
        return "redirect:/admin/category";
    }
}
