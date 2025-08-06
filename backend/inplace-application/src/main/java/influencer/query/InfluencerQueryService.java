package influencer.query;

import static java.util.stream.Collectors.toMap;

import exception.InplaceException;
import exception.code.InfluencerErrorCode;
import influencer.Influencer;
import influencer.InfluencerReadQueryDslRepository;
import influencer.LikedInfluencer;
import influencer.dto.InfluencerResult;
import influencer.dto.InfluencerNameInfo;
import influencer.jpa.InfluencerJpaRepository;
import influencer.jpa.LikedInfluencerJpaRepository;
import influencer.query.InfluencerQueryResult.Simple;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InfluencerQueryService {

    private final InfluencerJpaRepository influencerRepository;
    private final LikedInfluencerJpaRepository likedInfluencerJpaRepository;
    private final InfluencerReadQueryDslRepository influencerReadRepositoryImpl;

    @Transactional(readOnly = true)
    public Page<Simple> getAllInfluencers(Pageable pageable) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return influencerReadRepositoryImpl.getInfluencerSortedByLikes(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<InfluencerNameInfo> getAllInfluencerNames() {
        List<String> names = influencerRepository.findAllInfluencerNames();
        return names.stream()
            .map(InfluencerNameInfo::new)
            .toList();
    }

    //TODO: 추후 쿼리 한번으로 변경
    @Transactional(readOnly = true)
    public Page<InfluencerResult> getFavoriteInfluencers(Long userId, Pageable pageable) {
        Page<LikedInfluencer> influencerPage = likedInfluencerJpaRepository.findByUserIdAndIsLikedTrue(
            userId, pageable);
        var influencerIds = influencerPage.map(LikedInfluencer::getInfluencerId).toList();
        var influencers = influencerRepository.findAllById(influencerIds).stream()
            .collect(toMap(Influencer::getId, Function.identity()));

        var infos = influencerPage.stream()
            .map(likedInfluencer -> {
                var influencer = influencers.get(likedInfluencer.getInfluencerId());
                return InfluencerResult.from(influencer, true);
            })
            .toList();
        return new PageImpl<>(infos, pageable, influencerPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public InfluencerQueryResult.Detail getInfluencerDetail(Long influencerId) {
        Long userId = AuthorizationUtil.getUserIdOrNull();

        return influencerReadRepositoryImpl.getInfluencerDetail(influencerId, userId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<String> getInfluencerNamesByPlaceId(Long placeId) {
        return influencerReadRepositoryImpl.getInfluencerNamesByPlaceId(placeId);
    }
}
