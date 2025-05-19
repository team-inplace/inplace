package team7.inplace.video.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.video.persistence.dto.VideoFilterCondition;
import team7.inplace.video.persistence.dto.VideoQueryResult;

@DataJpaTest
@ActiveProfiles("test")
@Sql("/sql/test-video.sql")
public class VideoReadRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    private VideoReadRepository videoReadRepository;

    /**
     * 테스트 데이터 : 총 20개의 장소 좌표 범위 : (126.0, 36.0) ~ (128.0, 38.0) 자세한 내용은 src/test/resources/sql/test-video.sql 참고
     */
    @BeforeEach
    void setUp() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(testEntityManager.getEntityManager());
        this.videoReadRepository = new VideoReadRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 범위 내에 있는 장소에 대한 비디오 조회")
    void findVideo_InRange() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.4;
        Double bottomRightLatitude = 36.4;
        Double longitude = 127.00;
        Double latitude = 37.00;
        Pageable pageable = PageRequest.of(0, 5);

        final int expectedTotalContent = 7;
        final int expectedContentSize = 5;

        // when
        Page<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findSimpleVideosInSurround(
                topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude, latitude, pageable
        );

        // then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 조회수 증가량 순으로 비디오 10개 조회 (상위 카테고리 적용)")
    void findVideo_ViewCountDesc() {
        // given
        final int expectedTotalContent = 10;
        final List<Long> expectedVideoIds = List.of(20L, 19L, 18L, 17L, 15L, 14L, 13L, 12L, 10L, 9L);
        Long parentCategoryId = 1L;

        // when
        List<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findTop10ByViewCountIncrement(parentCategoryId);

        // then
        assertThat(videos.size()).isEqualTo(expectedTotalContent);
        assertThat(videos.stream().map(VideoQueryResult.DetailedVideo::videoId).toList())
                .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 업로드 날짜 순으로 비디오 10개 조회")
    void findVideo_UploadDateAsc() {
        // given
        final int expectedTotalContent = 10;
        final List<Long> expectedVideoIds = List.of(20L, 19L, 18L, 17L, 16L, 15L, 14L, 13L, 12L, 11L);

        // when
        List<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findTop10ByLatestUploadDate();

        // then
        assertThat(videos.size()).isEqualTo(expectedTotalContent);
        assertThat(videos.stream().map(VideoQueryResult.DetailedVideo::videoId).toList())
                .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 좋아요 한 인플루언서의 비디오 10개 조회")
    void findVideo_LikedInfluencer() {
        // given
        final int expectedTotalContent = 10;
        final List<Long> expectedVideoIds = List.of(20L, 19L, 18L, 17L, 8L, 7L, 6L, 5L, 4L, 3L);

        // when
        List<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findTop10ByLikedInfluencer(1L);

        // then
        assertThat(videos.size()).isEqualTo(expectedTotalContent);
        assertThat(videos.stream().map(VideoQueryResult.DetailedVideo::videoId).toList())
                .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 특정 장소의 비디오 조회")
    void findVideo_PlaceInfo() {
        // given
        final int expectedTotalContent = 1;
        final List<Long> expectedVideoIds = List.of(1L);

        // when
        List<VideoQueryResult.SimpleVideo> videos = videoReadRepository.findSimpleVideosByPlaceId(1L);

        // then
        assertThat(videos.size()).isEqualTo(expectedTotalContent);
        assertThat(videos.stream().map(VideoQueryResult.SimpleVideo::videoId).toList())
                .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 특정 장소들의 비디오 조회")
    void findVideo_PlacesInfo() {
        // given
        final int expectedTotalContent = 10;
        final List<Long> searchPlaceIds = List.of(1L, 2L, 3L ,4L, 5L, 6L, 7L, 8L, 9L, 10L);
        Long count = 1L;

        // when
        Map<Long, List<VideoQueryResult.SimpleVideo>> videoMap =
                videoReadRepository.findSimpleVideosByPlaceIds(searchPlaceIds);

        // then
        assertThat(videoMap.size()).isEqualTo(expectedTotalContent);
        for (Long l : videoMap.keySet()) {
            assertThat(l).isEqualTo(count);
            assertThat(videoMap.get(l).get(0).videoId()).isEqualTo(count);
            count += 1L;
        }
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 장소 지정 안된 비디오 조회")
    void findVideo_NormalCase() {
        // given
        Pageable pageable = PageRequest.of(0, 5);

        final int expectedTotalContent = 5;
        final int expectedContentSize = 5;
        final List<Long> expectedVideoIds = List.of(21L, 22L, 23L ,24L, 25L);

        // when
        Page<VideoQueryResult.SimpleVideo> videos = videoReadRepository.findVideoWithNoPlace(pageable);

        // then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.SimpleVideo::videoId).toList())
                .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 인플루언서 id를 통해 비디오 조회 - 기본(날짜 순) 정렬")
    void findVideo_InfluencerId() {
        // given
        Pageable pageable = PageRequest.of(0, 5);

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedVideoIds = List.of(4L, 3L, 2L, 1L);

        // when
        Page<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findDetailedVideosWithOneInfluencerId(
                1L, pageable);

        // then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.DetailedVideo::videoId).toList())
                .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 인플루언서 id를 통해 비디오 조회 - 인기순(조회수 증가량) 정렬")
    void findVideo_InfluencerId_popularity() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by("popularity"));

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedVideoIds = List.of(4L, 3L, 2L, 1L);

        // when
        Page<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findDetailedVideosWithOneInfluencerId(
                1L, pageable);

        // then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.DetailedVideo::videoId).toList())
            .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 인플루언서 id를 통해 비디오 조회 - 장소 좋아요순 정렬")
    void findVideo_InfluencerId_likes() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by("likes"));

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedVideoIds = List.of(3L, 2L, 1L, 4L);

        // when
        Page<VideoQueryResult.DetailedVideo> videos = videoReadRepository.findDetailedVideosWithOneInfluencerId(
                1L, pageable);

        // then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.DetailedVideo::videoId).toList())
            .isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 비디오 등록 여부 null, Influencer null 인 경우")
    void findAdminVideoByCondition_With_RegistrationNull_And_InfluencerNull() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        VideoFilterCondition videoFilterCondition = new VideoFilterCondition(null, null);
        final int expectedTotalElements = 5;
        final int expectedContentSize = 5;
        final List<Long> expectedVideoIds = List.of(21L, 22L, 23L ,24L, 25L);
        //when
        Page<VideoQueryResult.AdminVideo> videos = videoReadRepository.findAdminVideoByCondition(videoFilterCondition, pageable);

        //then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.AdminVideo::videoId).toList()).isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 비디오 등록 여부 true, Influencer 존재인 경우")
    void findAdminVideoByCondition_With_RegistrationTrue_And_InfluencerExist() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        VideoFilterCondition videoFilterCondition = new VideoFilterCondition(true, 5L);
        final int expectedTotalElements = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedVideoIds = List.of(17L, 18L, 19L, 20L);
        //when
        Page<VideoQueryResult.AdminVideo> videos = videoReadRepository.findAdminVideoByCondition(videoFilterCondition, pageable);

        //then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.AdminVideo::videoId).toList()).isEqualTo(expectedVideoIds);
    }

    @Test
    @DisplayName("비디오 조회 테스트 - 비디오 등록 여부 false, Influencer 존재인 경우")
    void findAdminVideoByCondition_With_RegistrationFalse_And_InfluencerExist() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        VideoFilterCondition videoFilterCondition = new VideoFilterCondition(false, 5L);
        final int expectedTotalElements = 5;
        final int expectedContentSize = 5;
        final List<Long> expectedVideoIds = List.of(21L, 22L, 23L ,24L, 25L);
        //when
        Page<VideoQueryResult.AdminVideo> videos = videoReadRepository.findAdminVideoByCondition(videoFilterCondition, pageable);

        //then
        assertThat(videos.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(videos.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(videos.getContent().stream().map(VideoQueryResult.AdminVideo::videoId).toList()).isEqualTo(expectedVideoIds);
    }
}
