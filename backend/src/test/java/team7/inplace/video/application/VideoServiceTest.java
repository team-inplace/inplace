package team7.inplace.video.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.video.domain.CoolVideo;
import team7.inplace.video.persistence.CoolVideoRepository;
import team7.inplace.video.persistence.VideoReadRepository;
import team7.inplace.video.persistence.VideoRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql("/sql/test-cool-video.sql")
class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoReadRepository videoReadRepository;

    @Autowired
    private CoolVideoRepository coolVideoRepository;

    @Autowired
    VideoService videoService;

    @Test
    @DisplayName("Cool Video 업데이트 Test")
    void updateCoolVideo() {
        //given
        List<CoolVideo> coolVideos = coolVideoRepository.findAll();
        assertThat(coolVideos)
            .hasSize(10)
            .extracting("videoUUID", "influencerName", "placeName")
            .containsExactly(
                tuple("CoolVideo1", "CoolInfluencer1", "CoolPlace1"),
                tuple("CoolVideo2", "CoolInfluencer2", "CoolPlace2"),
                tuple("CoolVideo3", "CoolInfluencer3", "CoolPlace3"),
                tuple("CoolVideo4", "CoolInfluencer4", "CoolPlace4"),
                tuple("CoolVideo5", "CoolInfluencer5", "CoolPlace5"),
                tuple("CoolVideo6", "CoolInfluencer6", "CoolPlace6"),
                tuple("CoolVideo7", "CoolInfluencer7", "CoolPlace7"),
                tuple("CoolVideo8", "CoolInfluencer8", "CoolPlace8"),
                tuple("CoolVideo9", "CoolInfluencer9", "CoolPlace9"),
                tuple("CoolVideo10", "CoolInfluencer10", "CoolPlace10")
            );
        //when

        videoService.updateCoolVideo();
        //then

        List<CoolVideo> updatedCoolVideos = coolVideoRepository.findAll();
        assertThat(updatedCoolVideos)
            .hasSize(10)
            .extracting("videoUUID", "influencerName", "placeName")
            .containsExactly(
                tuple("Video20", "influencer5", "testPlace20"),
                tuple("Video19", "influencer5", "testPlace19"),
                tuple("Video18", "influencer5", "testPlace18"),
                tuple("Video17", "influencer5", "testPlace17"),
                tuple("Video16", "influencer4", "testPlace16"),
                tuple("Video15", "influencer4", "testPlace15"),
                tuple("Video14", "influencer4", "testPlace14"),
                tuple("Video13", "influencer4", "testPlace13"),
                tuple("Video12", "influencer3", "testPlace12"),
                tuple("Video11", "influencer3", "testPlace11")
            );
    }
}
