package my.inplace.infra.search;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import my.inplace.domain.video.query.VideoQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = VideoSearchNativeRepository.class
    ),
    scripts = "/sql/test-search-video.sql"
)
class VideoSearchNativeRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    VideoSearchNativeRepository videoSearchRepository;

    @Test
    void search() {
        // given
        String keyword = "search";
        Pageable pageable = Pageable.ofSize(5);
        Long userId = 1L;
        List<VideoQueryResult.DetailedVideo> expected = List.of(
            new VideoQueryResult.DetailedVideo(26L, "Video26", "searchInfluencer", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(29L, "Video29", "influencer1", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(30L, "Video30", "influencer2", 21L, "searchPlace", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(27L, "Video27", "searchInfluencer", 14L, "testPlace14", "eats", 1L, "add1", "add2", "add3"),
            new VideoQueryResult.DetailedVideo(28L, "Video28", "searchInfluencer", 6L, "testPlace6", "eats", 1L, "add1", "add2", "add3")
        );

        // when
        Page<VideoQueryResult.DetailedVideo> actual = videoSearchRepository.search(keyword, pageable, userId);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }
}
