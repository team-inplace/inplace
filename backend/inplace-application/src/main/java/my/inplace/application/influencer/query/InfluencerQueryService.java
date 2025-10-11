package my.inplace.application.influencer.query;

import static java.util.stream.Collectors.toMap;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.InfluencerErrorCode;
import my.inplace.domain.influencer.Influencer;
import my.inplace.domain.influencer.LikedInfluencer;
import my.inplace.domain.influencer.query.InfluencerReadRepository;
import my.inplace.infra.influencer.jpa.InfluencerJpaRepository;
import my.inplace.infra.influencer.jpa.LikedInfluencerJpaRepository;
import my.inplace.application.influencer.query.dto.InfluencerResult;
import my.inplace.application.influencer.query.dto.InfluencerResult.Detail;
import my.inplace.application.influencer.query.dto.InfluencerResult.Name;
import my.inplace.application.influencer.query.dto.InfluencerResult.Simple;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.security.util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class InfluencerQueryService {

    private final InfluencerJpaRepository influencerRepository;
    private final LikedInfluencerJpaRepository likedInfluencerJpaRepository;
    private final InfluencerReadRepository influencerReadRepository;

    public Page<Simple> getAllInfluencers(Pageable pageable) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var influencers = influencerReadRepository.getInfluencerSortedByLikes(userId, pageable);
        return influencers.map(Simple::from);
    }

    public List<Name> getAllInfluencerNames() {
        List<String> names = influencerRepository.findAllInfluencerNames();
        return names.stream()
            .map(Name::new)
            .toList();
    }

    //TODO: 추후 쿼리 한번으로 변경
    public Page<Simple> getFavoriteInfluencers(Long userId, Pageable pageable) {
        Page<LikedInfluencer> influencerPage = likedInfluencerJpaRepository.findByUserIdAndIsLikedTrue(
            userId, pageable);
        var influencerIds = influencerPage.map(LikedInfluencer::getInfluencerId).toList();
        var influencers = influencerRepository.findAllById(influencerIds).stream()
            .collect(toMap(Influencer::getId, Function.identity()));

        var infos = influencerPage.stream()
            .map(likedInfluencer -> {
                var influencer = influencers.get(likedInfluencer.getInfluencerId());
                return Simple.from(influencer, true);
            })
            .toList();
        return new PageImpl<>(infos, pageable, influencerPage.getTotalElements());
    }

    public Detail getInfluencerDetail(Long influencerId) {
        Long userId = AuthorizationUtil.getUserIdOrNull();
        var detail = influencerReadRepository.getInfluencerDetail(influencerId, userId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));

        return Detail.from(detail);
    }

    public List<String> getInfluencerNamesByPlaceId(Long placeId) {
        return influencerReadRepository.getInfluencerNamesByPlaceId(placeId);
    }

    public List<InfluencerResult.Admin> findAll() {
        return influencerRepository.findAll()
            .stream()
            .map(InfluencerResult.Admin::from)
            .toList();
    }
}
