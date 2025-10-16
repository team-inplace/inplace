package my.inplace.application.video.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.util.List;
import my.inplace.application.video.query.dto.VideoParam.SquareBound;
import my.inplace.application.video.query.dto.VideoResult.DetailedVideo;
import my.inplace.domain.video.query.VideoQueryResult;
import my.inplace.domain.video.query.VideoReadRepository;
import my.inplace.infra.video.jpa.CoolVideoJpaRepository;
import my.inplace.infra.video.jpa.RecentVideoJpaRepository;
import my.inplace.security.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class VideoQueryServiceTest {

    @Mock
    private VideoReadRepository videoReadRepository;

    @Mock
    private CoolVideoJpaRepository coolVideoJpaRepository;

    @Mock
    private RecentVideoJpaRepository recentVideoJpaRepository;

    @InjectMocks
    private VideoQueryService videoQueryService;

    @Test
    @DisplayName("주변 비디오 조회")
    void getVideosBySurround() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        SquareBound bound = new SquareBound(
            "128.6", "35.8",
            "128.6", "35.8",
            "128.6", "35.8"
        );
        Pageable pageable = PageRequest.of(0, 2);

        VideoQueryResult.DetailedVideo queryVideo = new VideoQueryResult.DetailedVideo(
            1L,
            "video uuid",
            "influencer name",
            1L,
            "place name",
            "category name",
            1L,
            "address 1",
            "address 2",
            "address 3"
        );
        Page<VideoQueryResult.DetailedVideo> page = new PageImpl<>(List.of(queryVideo));
        given(videoReadRepository.findSimpleVideosInSurround(any(), any())).willReturn(page);
        given(AuthorizationUtil.isNotLoginUser()).willReturn(false);

        // when
        List<DetailedVideo> videos = videoQueryService.getVideosBySurround(bound, pageable, true);

        // then
        verify(videoReadRepository).findSimpleVideosInSurround(bound.toQueryParam(), pageable);
        assertThat(videos).hasSize(1);
        assertThat(videos.get(0).videoId()).isEqualTo(queryVideo.videoId());

        authorizationUtil.close();
    }
}