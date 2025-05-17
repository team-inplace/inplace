package team7.inplace.video.persistence;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.video.persistence.dto.VideoFilterCondition;
import team7.inplace.video.persistence.dto.VideoQueryResult;

public interface VideoReadRepository {
    Page<VideoQueryResult.DetailedVideo> findSimpleVideosInSurround(
            Double topLeftLongitude,
            Double topLeftLatitude,
            Double bottomRightLongitude,
            Double bottomRightLatitude,
            Double longitude,
            Double latitude,
            Pageable pageable
    );

    List<VideoQueryResult.DetailedVideo> findTop10ByViewCountIncrement();

    List<VideoQueryResult.DetailedVideo> findTop10ByLatestUploadDate();

    List<VideoQueryResult.DetailedVideo> findTop10ByLikedInfluencer(Long userId);

    List<VideoQueryResult.SimpleVideo> findSimpleVideosByPlaceId(Long placeId);

    Map<Long, List<VideoQueryResult.SimpleVideo>> findSimpleVideosByPlaceIds(List<Long> placeIds);

    Page<VideoQueryResult.SimpleVideo> findVideoWithNoPlace(Pageable pageable);

    Page<VideoQueryResult.DetailedVideo> findDetailedVideosWithOneInfluencerId(Long influencerId, Pageable pageable);

    Page<VideoQueryResult.AdminVideo> findAdminVideoByCondition(VideoFilterCondition condition, Pageable pageable);
}
