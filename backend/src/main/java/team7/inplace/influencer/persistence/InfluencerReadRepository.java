package team7.inplace.influencer.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;

public interface InfluencerReadRepository {

    Optional<InfluencerQueryResult.Detail> getInfluencerDetail(Long influencerId, Long userId);

    List<String> getInfluencerNamesByPlaceId(Long placeId);

    Page<InfluencerQueryResult.Simple> getInfluencerSortedByLikes(Long userId, Pageable pageable);
}
