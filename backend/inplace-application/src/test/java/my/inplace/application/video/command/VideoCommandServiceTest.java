package my.inplace.application.video.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import my.inplace.application.video.command.dto.VideoCommand;
import my.inplace.application.video.command.dto.VideoCommand.Create;
import my.inplace.domain.video.CoolVideo;
import my.inplace.domain.video.RecentVideo;
import my.inplace.domain.video.Video;
import my.inplace.domain.video.query.VideoQueryResult.DetailedVideo;
import my.inplace.domain.video.query.VideoReadRepository;
import my.inplace.infra.video.jpa.CoolVideoJpaRepository;
import my.inplace.infra.video.jpa.RecentVideoJpaRepository;
import my.inplace.infra.video.jpa.VideoJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VideoCommandServiceTest {

    @Mock
    private VideoReadRepository videoReadRepository;

    @Mock
    private VideoJpaRepository videoJpaRepository;

    @Mock
    private CoolVideoJpaRepository coolVideoJpaRepository;

    @Mock
    private RecentVideoJpaRepository recentVideoJpaRepository;

    @InjectMocks
    private VideoCommandService videoCommandService;

    @Test
    @DisplayName("새로운 비디오 & 중복 비디오 저장")
    void createVideos() {
        // given
        VideoCommand.Create command1 = new Create("1", "title1", LocalDateTime.now(), 1L);
        VideoCommand.Create command2 = new Create("2", "title2", LocalDateTime.now(), 1L);
        List<Create> commands = List.of(command1, command2);

        given(videoJpaRepository.existsByUuid(command1.videoId())).willReturn(false);
        given(videoJpaRepository.existsByUuid(command2.videoId())).willReturn(true);

        ArgumentCaptor<List<Video>> captor = ArgumentCaptor.forClass(List.class);

        // when
        videoCommandService.createVideos(commands);

        // then
        verify(videoJpaRepository).saveAll(captor.capture());
        List<Video> savedVideos = captor.getValue();
        assertThat(savedVideos).hasSize(1);
        assertThat(savedVideos.get(0).getUuid()).isEqualTo(command1.videoId());
    }

    @Test
    @DisplayName("비디오 조회수 업데이트")
    void updateVideoViews() {
        // given
        Long videoId = 1L;
        Long viewCount = 100L;
        VideoCommand.UpdateViewCount cmd = new VideoCommand.UpdateViewCount(videoId, viewCount);

        Video video = mock(Video.class);
        given(videoJpaRepository.findById(videoId)).willReturn(Optional.of(video));

        // when
        videoCommandService.updateVideoViews(List.of(cmd));

        // then
        verify(video).updateViewCount(viewCount);
    }

    @Test
    @DisplayName("쿨한 비디오 업데이트")
    void updateCoolVideos() {
        // given
        Long categoryId1 = 1L;
        Long categoryId2 = 2L;
        List<Long> parentCategoryIds = List.of(categoryId1, categoryId2);

        DetailedVideo dv1 = mock(DetailedVideo.class);
        DetailedVideo dv2 = mock(DetailedVideo.class);
        given(videoReadRepository.findTop10ByViewCountIncrement(categoryId1)).willReturn(List.of(dv1));
        given(videoReadRepository.findTop10ByViewCountIncrement(categoryId2)).willReturn(List.of(dv2));

        CoolVideo cool1 = mock(CoolVideo.class);
        CoolVideo cool2 = mock(CoolVideo.class);
        given(dv1.toCoolVideo()).willReturn(cool1);
        given(dv2.toCoolVideo()).willReturn(cool2);

        ArgumentCaptor<List<CoolVideo>> captor = ArgumentCaptor.forClass(List.class);

        // when
        videoCommandService.updateCoolVideos(parentCategoryIds);

        // then
        verify(coolVideoJpaRepository).deleteAll();
        verify(coolVideoJpaRepository).flush();
        verify(coolVideoJpaRepository).saveAll(captor.capture());
        List<CoolVideo> savedVideos = captor.getValue();
        assertThat(savedVideos).containsExactlyInAnyOrder(cool1, cool2);
    }


    @Test
    @DisplayName("최신 비디오 업데이트")
    void updateRecentVideos() {
        // given
        DetailedVideo dv1 = mock(DetailedVideo.class);
        DetailedVideo dv2 = mock(DetailedVideo.class);
        given(videoReadRepository.findTop10ByLatestUploadDate()).willReturn(List.of(dv1, dv2));

        RecentVideo recent1 = mock(RecentVideo.class);
        RecentVideo recent2 = mock(RecentVideo.class);
        given(dv1.toRecentVideo()).willReturn(recent1);
        given(dv2.toRecentVideo()).willReturn(recent2);

        ArgumentCaptor<List<RecentVideo>> captor = ArgumentCaptor.forClass(List.class);

        // when
        videoCommandService.updateRecentVideos();

        // then
        verify(recentVideoJpaRepository).deleteAll();
        verify(recentVideoJpaRepository).flush();
        verify(recentVideoJpaRepository).saveAll(captor.capture());
        List<RecentVideo> savedVideos = captor.getValue();
        assertThat(savedVideos).containsExactlyInAnyOrder(recent1, recent2);
    }
}