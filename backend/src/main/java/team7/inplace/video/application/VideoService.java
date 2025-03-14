package team7.inplace.video.application;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.VideoErrorCode;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.application.command.VideoCommand;
import team7.inplace.video.application.command.VideoCommand.Create;
import team7.inplace.video.domain.Video;
import team7.inplace.video.persistence.CoolVideoRepository;
import team7.inplace.video.persistence.RecentVideoRepository;
import team7.inplace.video.persistence.VideoReadRepository;
import team7.inplace.video.persistence.VideoRepository;
import team7.inplace.video.persistence.dto.VideoQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult.SimpleVideo;
import team7.inplace.video.presentation.dto.VideoSearchParams;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoReadRepository videoReadRepository;
    private final VideoRepository videoRepository;
    private final CoolVideoRepository coolVideoRepository;
    private final RecentVideoRepository recentVideoRepository;

    //TODO: Facade에서 호출로 변경해야함.
    @Transactional(readOnly = true)
    public List<VideoQueryResult.SimpleVideo> getVideosBySurround(
        VideoSearchParams videoSearchParams,
        Pageable pageable
    ) {
        // 토큰 정보에 대한 검증
        if (AuthorizationUtil.isNotLoginUser()) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        var surroundVideos = videoReadRepository.findSimpleVideosInSurround(
            Double.valueOf(videoSearchParams.topLeftLongitude()),
            Double.valueOf(videoSearchParams.topLeftLatitude()),
            Double.valueOf(videoSearchParams.bottomRightLongitude()),
            Double.valueOf(videoSearchParams.bottomRightLatitude()),
            Double.valueOf(videoSearchParams.longitude()),
            Double.valueOf(videoSearchParams.latitude()),
            pageable
        );

        return surroundVideos.getContent();
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.SimpleVideo> getAllVideosDesc() {
        var top10Videos = recentVideoRepository.findAll();

        return top10Videos.stream().map(SimpleVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.SimpleVideo> getCoolVideo() {
        var top10Videos = coolVideoRepository.findAll();

        return top10Videos.stream().map(SimpleVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.SimpleVideo> getMyInfluencerVideos(Long userId) {
        var top10Videos = videoReadRepository.findTop10ByLikedInfluencer(userId);

        return top10Videos.stream().toList();
    }

    @Transactional(readOnly = true)
    public Page<VideoQueryResult.SimpleVideo> getOneInfluencerVideos(
        Long influencerId, Pageable pageable) {
        var videos = videoReadRepository.findSimpleVideosWithOneInfluencerId(influencerId,
            pageable);
        return videos;
    }

    @Transactional(readOnly = true)
    public Map<Long, List<SimpleVideo>> getVideosByPlaceId(List<Long> placeIds) {
        return videoReadRepository.findSimpleVideosByPlaceIds(placeIds);
    }

    @Transactional(readOnly = true)
    public List<SimpleVideo> getVideosByPlaceId(Long placeId) {
        return videoReadRepository.findSimpleVideosByPlaceId(placeId);
    }

    @Transactional
    public void createVideos(List<Create> videoCommands, Long influencerId) {
        var videos = videoCommands.stream()
            .filter(command -> !videoRepository.existsByUuid(command.videoId()))
            .map(Create::toEntity)
            .toList();

        videoRepository.saveAll(videos);
    }

    @Transactional(readOnly = true)
    public Page<VideoQueryResult.SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoReadRepository.findVideoWithNoPlace(pageable);
    }

    @Transactional
    public void updateVideoViews(List<VideoCommand.UpdateViewCount> videoCommands) {
        for (VideoCommand.UpdateViewCount videoCommand : videoCommands) {
            Video video = videoRepository.findById(videoCommand.videoId())
                .orElseThrow(() -> InplaceException.of(VideoErrorCode.NOT_FOUND));
            video.updateViewCount(videoCommand.viewCount());
        }
    }

    @Transactional
    public void addPlaceInfo(Long videoId, Long placeId) {
        var video = videoRepository.findById(videoId)
            .orElseThrow(() -> InplaceException.of(VideoErrorCode.NOT_FOUND));

        video.addPlace(placeId);
    }

    @Transactional
    public void deleteVideo(Long videoId) {
        videoRepository.deleteById(videoId);
    }

    @Transactional
    public void updateCoolVideos() {
        // 인기순 top 10 video 가져오기
        List<SimpleVideo> coolVideos = videoReadRepository.findTop10ByViewCountIncrement();

        // coolVideo table 업데이트하기
        coolVideoRepository.deleteAll();
        coolVideoRepository.flush(); // delete 후 save 하려면 flush를 해야함.
        coolVideoRepository.saveAll(
            coolVideos.stream()
                .map(SimpleVideo::toCoolVideo).toList()
        );
    }

    @Transactional
    public void updateRecentVideos() {
        //최신순 top 10 video 가져오기
        List<SimpleVideo> recentVideos = videoReadRepository.findTop10ByLatestUploadDate();

        // recentVideo table 업데이트하기
        recentVideoRepository.deleteAll();
        recentVideoRepository.flush();
        recentVideoRepository.saveAll(
            recentVideos.stream()
                .map(SimpleVideo::toRecentVideo).toList()
        );
    }
}
