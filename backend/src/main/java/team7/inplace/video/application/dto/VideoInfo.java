package team7.inplace.video.application.dto;

import team7.inplace.place.application.dto.PlaceForVideo;
import team7.inplace.video.application.AliasUtil;
import team7.inplace.video.domain.Video;

// Video 도메인의 Controller와 Service 사이의 정보 전달을 담당하는 클래스 ( Service Return )
public record VideoInfo(
        Long videoId,
        String videoAlias,
        String videoUrl,
        PlaceForVideo place
) {
    public static VideoInfo from(Video video) {
        var alias = AliasUtil.makeAlias(video.getInfluencer().getName(), video.getPlace().getCategory());

        return new VideoInfo(
                video.getId(),
                alias,
                video.getVideoUrl(),
                PlaceForVideo.of(video.getPlace().getId(), video.getPlace().getName())
        );
    }
}
