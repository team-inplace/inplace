package team7.inplace.influencer.application;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.InfluencerErrorCode;
import team7.inplace.influencer.application.dto.InfluencerCommand;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.application.dto.InfluencerNameInfo;
import team7.inplace.influencer.application.dto.LikedInfluencerCommand;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.persistence.InfluencerReadRepositoryImpl;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult.Detail;
import team7.inplace.liked.likedInfluencer.domain.LikedInfluencer;
import team7.inplace.liked.likedInfluencer.persistent.LikedInfluencerRepository;
import team7.inplace.security.util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class InfluencerService {

    private final InfluencerRepository influencerRepository;
    private final LikedInfluencerRepository likedInfluencerRepository;
    private final InfluencerReadRepositoryImpl influencerReadRepositoryImpl;

    //TODO: 추후 리팩토링 필요
    @Transactional(readOnly = true)
    public Page<InfluencerInfo> getAllInfluencers(Pageable pageable) {
        Page<Influencer> influencersPage = influencerRepository.findAll(pageable);

        // 로그인 안된 경우, likes를 모두 false로 설정
        if (AuthorizationUtil.isNotLoginUser()) {
            return influencersPage.map(influencer -> InfluencerInfo.from(influencer, false));
        }

        // 로그인 된 경우
        Long userId = AuthorizationUtil.getUserId();
        Set<Long> likedInfluencerIds = likedInfluencerRepository.findLikedInfluencerIdsByUserId(
            userId);

        List<InfluencerInfo> influencerInfos = influencersPage.stream()
            .map(influencer -> {
                boolean isLiked = likedInfluencerIds.contains(influencer.getId());
                return InfluencerInfo.from(influencer, isLiked);
            })
            .sorted((a, b) -> Boolean.compare(b.likes(), a.likes()))
            .toList();

        return new PageImpl<>(influencerInfos, pageable, influencersPage.getTotalElements());
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
        Influencer influencer = influencerRepository.findById(id).orElseThrow();
        influencer.update(command.influencerName(), command.influencerImgUrl(),
            command.influencerJob());

        return influencer.getId();
    }

    @Transactional
    public Long updateVisibility(Long id) {
        var influencer = influencerRepository.findById(id)
            .orElseThrow();
        influencer.changeVisibility();

        return influencer.getId();
    }

    @Transactional
    public void deleteInfluencer(Long id) {
        Influencer influencer = influencerRepository.findById(id).orElseThrow();

        influencerRepository.delete(influencer);
    }

    @Transactional
    public void likeToInfluencer(LikedInfluencerCommand.Single command) {
        Long userId = AuthorizationUtil.getUserId();
        if (userId == null) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }
        upsertFavoriteInfluencer(userId, command);
    }

    @Transactional
    public void likeToManyInfluencer(LikedInfluencerCommand.Multiple command) {
        Long userId = AuthorizationUtil.getUserId();
        if (userId == null) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        for (LikedInfluencerCommand.Single single : command.likes()) {
            upsertFavoriteInfluencer(userId, single);
        }
    }

    private void upsertFavoriteInfluencer(Long userId, LikedInfluencerCommand.Single command) {
        var likedInfluencer = likedInfluencerRepository
            .findByUserIdAndInfluencerId(userId, command.influencerId())
            .orElseGet(() -> {
                var newLikedInfluencer = command.toEntity(userId);
                return likedInfluencerRepository.save(newLikedInfluencer);
            });

        if (command.like()) {
            likedInfluencer.like();
            return;
        }
        likedInfluencer.unlike();
    }

    //TODO: 추후 쿼리 한번으로 변경
    @Transactional(readOnly = true)
    public Page<InfluencerInfo> getFavoriteInfluencers(Long userId, Pageable pageable) {
        Page<LikedInfluencer> influencerPage = likedInfluencerRepository.findByUserIdAndIsLikedTrue(
            userId, pageable);
        var influencerIds = influencerPage.map(LikedInfluencer::getInfluencerId).toList();
        var influencers = influencerRepository.findAllById(influencerIds).stream()
            .collect(toMap(Influencer::getId, Function.identity()));

        var infos = influencerPage.stream()
            .map(likedInfluencer -> {
                var influencer = influencers.get(likedInfluencer.getInfluencerId());
                return InfluencerInfo.from(influencer, true);
            })
            .toList();
        return new PageImpl<>(infos, pageable, influencerPage.getTotalElements());
    }

    @Transactional()
    public void updateLastVideo(Long influencerId, String lastVideo) {
        var influencer = influencerRepository.findById(influencerId)
            .orElseThrow();
        influencer.updateLastVideo(lastVideo);
    }

    @Transactional(readOnly = true)
    public Detail getInfluencerDetail(Long influencerId) {
        Long userId = AuthorizationUtil.getUserId();

        return influencerReadRepositoryImpl.getInfluencerDetail(influencerId, userId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
    }

    @Transactional
    public List<String> getInfluencerNamesByPlaceId(Long placeId) {
        return influencerReadRepositoryImpl.getInfluencerNamesByPlaceId(placeId);
    }
}
