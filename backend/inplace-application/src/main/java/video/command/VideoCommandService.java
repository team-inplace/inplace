package video.command;

import exception.InplaceException;
import exception.code.VideoErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import video.Video;
import video.command.dto.VideoCommand.Create;
import video.command.dto.VideoCommand.UpdateViewCount;
import video.jpa.CoolVideoJpaRepository;
import video.jpa.RecentVideoJpaRepository;
import video.jpa.VideoJpaRepository;
import video.query.VideoQueryResult.DetailedVideo;
import video.query.VideoReadRepository;

@Service
@RequiredArgsConstructor
public class VideoCommandService {

    private final VideoReadRepository videoReadRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final CoolVideoJpaRepository coolVideoRepository;
    private final RecentVideoJpaRepository recentVideoRepository;

    @Transactional
    public void createVideos(List<Create> videoCommands) {
        var videos = videoCommands.stream()
            .filter(command -> !videoJpaRepository.existsByUuid(command.videoId()))
            .map(Create::toEntity)
            .toList();

        videoJpaRepository.saveAll(videos);
    }

    @Transactional
    public void updateVideoViews(List<UpdateViewCount> videoCommands) {
        for (UpdateViewCount videoCommand : videoCommands) {
            Video video = videoJpaRepository.findById(videoCommand.videoId())
                .orElseThrow(() -> InplaceException.of(VideoErrorCode.NOT_FOUND));
            video.updateViewCount(videoCommand.viewCount());
        }
    }

    @Transactional
    public void deleteVideo(Long videoId) {
        Video video = videoJpaRepository.findById(videoId)
            .orElseThrow(() -> InplaceException.of(VideoErrorCode.NOT_FOUND));

        video.deleteSoftly();
    }

    @Transactional
    public void updateCoolVideos(List<Long> parentCategoryIds) {
        List<DetailedVideo> coolVideos = new ArrayList<>();

        // 상위 카테고리별 인기순 top 10 video 가져오기
        for (Long parentCategoryId : parentCategoryIds) {
            List<DetailedVideo> top10 = videoReadRepository.findTop10ByViewCountIncrement(
                parentCategoryId);
            coolVideos.addAll(top10);
        }

        // coolVideo table 업데이트하기
        coolVideoRepository.deleteAll();
        coolVideoRepository.flush(); // delete 후 save 하려면 flush를 해야함.
        coolVideoRepository.saveAll(
            coolVideos.stream()
                .map(DetailedVideo::toCoolVideo).toList()
        );
    }

    @Transactional
    public void updateRecentVideos() {
        //최신순 top 10 video 가져오기
        List<DetailedVideo> recentVideos = videoReadRepository.findTop10ByLatestUploadDate();

        // recentVideo table 업데이트하기
        recentVideoRepository.deleteAll();
        recentVideoRepository.flush();
        recentVideoRepository.saveAll(
            recentVideos.stream()
                .map(DetailedVideo::toRecentVideo).toList()
        );
    }
}
