package team7.inplace.liked.likedInfluencer.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.liked.likedInfluencer.application.dto.FavoriteInfluencerListCommand;
import team7.inplace.liked.likedInfluencer.domain.FavoriteInfluencer;
import team7.inplace.liked.likedInfluencer.application.dto.FavoriteInfluencerCommand;
import team7.inplace.liked.likedInfluencer.persistent.FavoriteInfluencerRepository;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.security.application.CurrentUserProvider;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.domain.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteInfluencerService {

    private final InfluencerRepository influencerRepository;
    private final FavoriteInfluencerRepository favoriteRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public void likeToInfluencer(FavoriteInfluencerCommand command) {
        if (AuthorizationUtil.isNotLoginUser()) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        User user = currentUserProvider.getCurrentUser();
        Influencer influencer = influencerRepository.findById(command.influencerId()).orElseThrow();

        processFavoriteInfluencer(user, influencer, command.likes());
    }

    @Transactional
    public void likeToManyInfluencer(FavoriteInfluencerListCommand command) {
        if (AuthorizationUtil.isNotLoginUser()) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        User user = currentUserProvider.getCurrentUser();
        List<Influencer> influencers = influencerRepository.findAllById(command.influencerIds());

        for (Influencer influencer : influencers) {
            processFavoriteInfluencer(user, influencer, command.likes());
        }
    }

    private void processFavoriteInfluencer(User user, Influencer influencer, Boolean likes) {
        FavoriteInfluencer favorite = favoriteRepository
                .findByUserIdAndInfluencerId(user.getId(), influencer.getId())
                .orElseGet(() -> new FavoriteInfluencer(user, influencer)); // 존재하지 않으면 새로 생성

        favorite.updateLike(likes);
        if (favorite.getId() == null) {
            favoriteRepository.save(favorite);
        }
    }

    @Transactional(readOnly = true)
    public Page<InfluencerInfo> getFavoriteInfluencers(Long userId, Pageable pageable) {
        Page<FavoriteInfluencer> influencerPage = favoriteRepository.findByUserIdAndIsLikedTrue(
                userId, pageable);

        return influencerPage.map(
                favorite -> InfluencerInfo.from(favorite.getInfluencer(), favorite.isLiked()));
    }
}

