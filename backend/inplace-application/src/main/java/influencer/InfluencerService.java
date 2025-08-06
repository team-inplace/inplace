package influencer;

import static java.util.stream.Collectors.toMap;

import exception.InplaceException;
import exception.code.InfluencerErrorCode;
import influencer.Influencer;
import influencer.LikedInfluencer;
import influencer.dto.InfluencerCommand;
import influencer.dto.InfluencerNameInfo;
import influencer.dto.InfluencerResult;
import influencer.dto.LikedInfluencerCommand;
import influencer.jpa.LikedInfluencerJpaRepository;
import influencer.jpa.InfluencerJpaRepository;
import influencer.query.InfluencerQueryResult.Detail;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.security.util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class InfluencerService {

    private final InfluencerJpaRepository influencerRepository;
    private final LikedInfluencerJpaRepository likedInfluencerJpaRepository;
    private final InfluencerReadQueryDslRepository influencerReadRepositoryImpl;

    @Transactional(readOnly = true)
    public Page<InfluencerQueryResult.Simple> getAllInfluencers(Pageable pageable) {
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

    @Transactional
    public Long createInfluencer(InfluencerCommand command) {
        var influencer = command.toEntity();
        influencerRepository.save(influencer);

        return influencer.getId();
    }

    @Transactional
    public Long updateInfluencer(Long id, InfluencerCommand command) {
        Influencer influencer = influencerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.update(command.influencerName(), command.influencerImgUrl(),
            command.influencerJob());

        return influencer.getId();
    }

    @Transactional
    public Long updateVisibility(Long id) {
        Influencer influencer = influencerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.changeVisibility();

        return influencer.getId();
    }

    @Transactional
    public void deleteInfluencer(Long id) {
        Influencer influencer = influencerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));

        influencerRepository.delete(influencer);
    }

    @Transactional
    public void likeToInfluencer(LikedInfluencerCommand.Single command) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        upsertFavoriteInfluencer(userId, command);
    }

    @Transactional
    public void likeToManyInfluencer(LikedInfluencerCommand.Multiple command) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        for (LikedInfluencerCommand.Single single : command.likes()) {
            upsertFavoriteInfluencer(userId, single);
        }
    }

    private void upsertFavoriteInfluencer(Long userId, LikedInfluencerCommand.Single command) {
        var likedInfluencer = likedInfluencerJpaRepository
            .findByUserIdAndInfluencerId(userId, command.influencerId())
            .orElseGet(() -> {
                var newLikedInfluencer = command.toEntity(userId);
                return likedInfluencerJpaRepository.save(newLikedInfluencer);
            });

        if (command.like()) {
            likedInfluencer.like();
            return;
        }
        likedInfluencer.unlike();
    }

    @Transactional()
    public void updateLastMediumVideo(Long influencerId, String lastVideo) {
        Influencer influencer = influencerRepository.findById(influencerId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.updateLastMediumVideo(lastVideo);
    }

    @Transactional()
    public void updateLastLongVideo(Long influencerId, String lastLongVideo) {
        Influencer influencer = influencerRepository.findById(influencerId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.updateLastLongVideo(lastLongVideo);
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
    public Detail getInfluencerDetail(Long influencerId) {
        Long userId = AuthorizationUtil.getUserIdOrNull();

        return influencerReadRepositoryImpl.getInfluencerDetail(influencerId, userId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<String> getInfluencerNamesByPlaceId(Long placeId) {
        return influencerReadRepositoryImpl.getInfluencerNamesByPlaceId(placeId);
    }
}
