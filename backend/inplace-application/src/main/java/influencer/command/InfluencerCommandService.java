package influencer.command;

import exception.InplaceException;
import exception.code.InfluencerErrorCode;
import influencer.Influencer;
import influencer.command.dto.InfluencerCommand.InfluencerCreate;
import influencer.command.dto.InfluencerCommand.InfluencerUpdate;
import influencer.command.dto.InfluencerCommand.LikeMultiple;
import influencer.command.dto.InfluencerCommand.LikeSingle;
import influencer.jpa.InfluencerJpaRepository;
import influencer.jpa.LikedInfluencerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class InfluencerCommandService {

    private final InfluencerJpaRepository influencerJpaRepository;
    private final LikedInfluencerJpaRepository likedInfluencerJpaRepository;

    @Transactional
    public Long createInfluencer(InfluencerCreate command) {
        var influencer = command.toEntity();
        influencerJpaRepository.save(influencer);

        return influencer.getId();
    }

    @Transactional
    public Long updateInfluencer(InfluencerUpdate command) {
        Influencer influencer = influencerJpaRepository.findById(command.id())
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.update(command.influencerName(), command.influencerImgUrl(),
            command.influencerJob());

        return influencer.getId();
    }

    @Transactional
    public Long updateVisibility(Long id) {
        Influencer influencer = influencerJpaRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.changeVisibility();

        return influencer.getId();
    }

    @Transactional
    public void deleteInfluencer(Long id) {
        Influencer influencer = influencerJpaRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));

        influencerJpaRepository.delete(influencer);
    }

    @Transactional
    public void likeToInfluencer(LikeSingle command) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        upsertFavoriteInfluencer(userId, command);
    }

    @Transactional
    public void likeToManyInfluencer(LikeMultiple command) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        for (LikeSingle single : command.likes()) {
            upsertFavoriteInfluencer(userId, single);
        }
    }

    private void upsertFavoriteInfluencer(Long userId, LikeSingle command) {
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
        Influencer influencer = influencerJpaRepository.findById(influencerId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.updateLastMediumVideo(lastVideo);
    }

    @Transactional()
    public void updateLastLongVideo(Long influencerId, String lastLongVideo) {
        Influencer influencer = influencerJpaRepository.findById(influencerId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.updateLastLongVideo(lastLongVideo);
    }
}
