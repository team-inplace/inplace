package my.inplace.domain.video.query;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoReadRepository {

    Page<VideoQueryResult.DetailedVideo> findSimpleVideosInSurround(
        VideoQueryParam.SquareBound squareBound,
        Pageable pageable
    );

    List<VideoQueryResult.DetailedVideo> findTop10ByViewCountIncrement(Long parentCategoryId);

    List<VideoQueryResult.DetailedVideo> findTop10ByLatestUploadDate();

    List<VideoQueryResult.DetailedVideo> findTop10ByLikedInfluencer(Long userId);

    List<VideoQueryResult.SimpleVideo> findSimpleVideosByPlaceId(Long placeId);

    Map<Long, List<VideoQueryResult.SimpleVideo>> findSimpleVideosByPlaceIds(List<Long> placeIds);

    Page<VideoQueryResult.SimpleVideo> findVideoWithNoPlace(Pageable pageable);

    Page<VideoQueryResult.DetailedVideo> findDetailedVideosWithOneInfluencerId(
        Long influencerId, Pageable pageable);

    Page<VideoQueryResult.AdminVideo> findAdminVideoByCondition(
        VideoQueryParam.Condition condition, Pageable pageable);
}
