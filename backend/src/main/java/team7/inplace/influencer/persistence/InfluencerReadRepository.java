package team7.inplace.influencer.persistence;

import java.util.Optional;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;

public interface InfluencerReadRepository {

    Optional<InfluencerQueryResult.Detail> getInfluencerDetail(Long influencerId, Long userId);
}