package my.inplace.infra.video;

import my.inplace.domain.video.query.VideoQueryParam;
import my.inplace.domain.video.query.VideoQueryParam.SquareBound;
import my.inplace.domain.video.query.VideoQueryResult;
import my.inplace.domain.video.query.VideoQueryResult.DetailedVideo;
import my.inplace.domain.video.query.VideoQueryResult.SimpleVideo;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = VideoReadQueryDslRepository.class
    ),
    scripts = "/sql/test-video.sql"
)
class VideoReadQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    VideoReadQueryDslRepository videoReadRepository;

    @Test
    void findSimpleVideosInSurround() {
        // given
        VideoQueryParam.SquareBound squareBound = new SquareBound(null, null, null, null, 126.95, 36.95);
        Pageable pageable = Pageable.ofSize(5);
        List<VideoQueryResult.DetailedVideo> expected = List.of(
            new VideoQueryResult.DetailedVideo(28L, "Video28", "searchInfluencer", 6L, "testPlace6", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(8L, "Video8", "influencer2", 8L, "testPlace8", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(7L, "Video7", "influencer2", 7L, "testPlace7", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(6L, "Video6", "influencer2", 6L, "testPlace6", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(5L, "Video5", "influencer2", 5L, "testPlace5", "eats", 1L, "add1", "add2", "add3")
        );

        // when
        Page<VideoQueryResult.DetailedVideo> actual = videoReadRepository.findSimpleVideosInSurround(squareBound, pageable);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }

    @Test
    void findTop10ByViewCountIncrement() {
        // given
        long parentCategoryId = 1L;
        List<VideoQueryResult.DetailedVideo> expected = List.of(
            new VideoQueryResult.DetailedVideo(28L, "Video28", "searchInfluencer", 6L, "testPlace6", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(29L, "Video29", "influencer1", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(30L, "Video30", "influencer2", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(20L, "Video20", "influencer5", 20L, "testPlace20", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(19L, "Video19", "influencer5", 19L, "testPlace19", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(18L, "Video18", "influencer5", 18L, "testPlace18", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(17L, "Video17", "influencer5", 17L, "testPlace17", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(16L, "Video16", "influencer4", 16L, "testPlace16", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(15L, "Video15", "influencer4", 15L, "testPlace15", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(27L, "Video27", "searchInfluencer", 14L, "testPlace14", "eats", 1L, "add1", "add2", "add3")
        );

        // when
        List<VideoQueryResult.DetailedVideo> actual = videoReadRepository.findTop10ByViewCountIncrement(parentCategoryId);

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findTop10ByLatestUploadDate() {
        // given
        List<VideoQueryResult.DetailedVideo> expected = List.of(
            new VideoQueryResult.DetailedVideo(30L, "Video30", "influencer2", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(29L, "Video29", "influencer1", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(28L, "Video28", "searchInfluencer", 6L, "testPlace6", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(27L, "Video27", "searchInfluencer", 14L, "testPlace14", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(26L, "Video26", "searchInfluencer", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(20L, "Video20", "influencer5", 20L, "testPlace20", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(19L, "Video19", "influencer5", 19L, "testPlace19", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(18L, "Video18", "influencer5", 18L, "testPlace18", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(17L, "Video17", "influencer5", 17L, "testPlace17", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(16L, "Video16", "influencer4", 16L, "testPlace16", "eats", 1L, "add1", "add2", "add3")
        );

        // when
        List<VideoQueryResult.DetailedVideo> actual = videoReadRepository.findTop10ByLatestUploadDate();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void findTop10ByLikedInfluencer() {
        // given
        Long userId = 1L;
        List<VideoQueryResult.DetailedVideo> expected = List.of(
            new VideoQueryResult.DetailedVideo(29L, "Video29", "influencer1", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(4L, "Video4", "influencer1", 4L, "testPlace4", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(3L, "Video3", "influencer1", 3L, "testPlace3", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(3L, "Video3", "influencer1", 2L, "testPlace2", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(3L, "Video3", "influencer1", 1L, "testPlace1", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(2L, "Video2", "influencer1", 1L, "testPlace1", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(2L, "Video2", "influencer1", 2L, "testPlace2", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(2L, "Video2", "influencer1", 3L, "testPlace3", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(1L, "Video1", "influencer1", 1L, "testPlace1", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(1L, "Video1", "influencer1", 2L, "testPlace2", "eats", 1L, "add1", "add2", "add3")
        );

        // when
        List<VideoQueryResult.DetailedVideo> actual = videoReadRepository.findTop10ByLikedInfluencer(userId);

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findSimpleVideosByPlaceId() {
        // given
        Long placeId = 1L;
        List<VideoQueryResult.SimpleVideo> expected = List.of(
            new VideoQueryResult.SimpleVideo(1L, "Video1", "influencer1", 1L, "testPlace1", "카페"),
            new VideoQueryResult.SimpleVideo(2L, "Video2", "influencer1", 1L, "testPlace1", "카페"),
            new VideoQueryResult.SimpleVideo(3L, "Video3", "influencer1", 1L, "testPlace1", "카페")
        );

        // when
        List<VideoQueryResult.SimpleVideo> actual = videoReadRepository.findSimpleVideosByPlaceId(placeId);

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findSimpleVideosByPlaceIds() {
        // given
        Long placeId1 = 1L;
        Long placeId2 = 6L;
        List<Long> placeIds = List.of(placeId1, placeId2);
        Map<Long, List<VideoQueryResult.SimpleVideo>> expected = Map.of(
            1L, List.of(
                new VideoQueryResult.SimpleVideo(1L, "Video1", "influencer1", 1L, "testPlace1", "카페"),
                new VideoQueryResult.SimpleVideo(2L, "Video2", "influencer1", 1L, "testPlace1", "카페"),
                new VideoQueryResult.SimpleVideo(3L, "Video3", "influencer1", 1L, "testPlace1", "카페")
            ),
            6L, List.of(
                new VideoQueryResult.SimpleVideo(6L, "Video6", "influencer2", 6L, "testPlace6", "카페"),
                new VideoQueryResult.SimpleVideo(28L, "Video28", "searchInfluencer", 6L, "testPlace6", "카페")
                )
        );

        // when
        Map<Long, List<SimpleVideo>> actual = videoReadRepository.findSimpleVideosByPlaceIds(placeIds);

        // then
        assertThat(actual.get(placeId1)).containsExactlyInAnyOrderElementsOf(expected.get(placeId1));
        assertThat(actual.get(placeId2)).containsExactlyInAnyOrderElementsOf(expected.get(placeId2));
    }

    @Test
    void findVideoWithNoPlace() {
        // given
        Pageable pageable = Pageable.ofSize(5);
        List<VideoQueryResult.SimpleVideo> expected = List.of(
            new VideoQueryResult.SimpleVideo(21L, "Video21", "influencer5", null, null, null),
            new VideoQueryResult.SimpleVideo(22L, "Video22", "influencer5", null, null, null),
            new VideoQueryResult.SimpleVideo(23L, "Video23", "influencer5", null, null, null),
            new VideoQueryResult.SimpleVideo(24L, "Video24", "influencer5", null, null, null),
            new VideoQueryResult.SimpleVideo(25L, "Video25", "influencer5", null, null, null)
        );

        // when
        Page<VideoQueryResult.SimpleVideo> actual = videoReadRepository.findVideoWithNoPlace(pageable);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }

    @Test
    void findDetailedVideosWithOneInfluencerId() {
        // given
        Long influencerId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        List<VideoQueryResult.DetailedVideo> expected = List.of(
            new VideoQueryResult.DetailedVideo(29L, "Video29", "influencer1", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(4L, "Video4", "influencer1", 4L, "testPlace4", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(3L, "Video3", "influencer1", 2L, "testPlace2", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(3L, "Video3", "influencer1", 1L, "testPlace1", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(3L, "Video3", "influencer1", 3L, "testPlace3", "eats", 1L, "add1", "add2", "add3")
        );

        // when
        Page<VideoQueryResult.DetailedVideo> actual = videoReadRepository.findDetailedVideosWithOneInfluencerId(
            influencerId, pageable);

        // then
        assertThat(actual.getContent()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findAdminVideoByCondition() {
        // given
        VideoQueryParam.Condition condition = new VideoQueryParam.Condition(false, 5L);
        Pageable pageable = Pageable.ofSize(5);
        List<VideoQueryResult.AdminVideo> expected = List.of(
            new VideoQueryResult.AdminVideo(21L, "Video21", false),
            new VideoQueryResult.AdminVideo(22L, "Video22", false),
            new VideoQueryResult.AdminVideo(23L, "Video23", false),
            new VideoQueryResult.AdminVideo(24L, "Video24", false),
            new VideoQueryResult.AdminVideo(25L, "Video25", false)
        );

        // when
        Page<VideoQueryResult.AdminVideo> actual = videoReadRepository.findAdminVideoByCondition(condition, pageable);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }

}
