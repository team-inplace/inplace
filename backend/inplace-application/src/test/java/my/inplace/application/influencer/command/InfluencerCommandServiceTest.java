package my.inplace.application.influencer.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import my.inplace.application.influencer.command.dto.InfluencerCommand.LikeMultiple;
import my.inplace.application.influencer.command.dto.InfluencerCommand.LikeSingle;
import my.inplace.domain.influencer.LikedInfluencer;
import my.inplace.infra.influencer.jpa.InfluencerJpaRepository;
import my.inplace.infra.influencer.jpa.LikedInfluencerJpaRepository;
import my.inplace.security.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InfluencerCommandServiceTest {

    @Mock
    private InfluencerJpaRepository influencerJpaRepository;

    @Mock
    private LikedInfluencerJpaRepository likedInfluencerJpaRepository;

    @InjectMocks
    private InfluencerCommandService influencerCommandService;

    @Test
    @DisplayName("새로운 인플루언서 한명 좋아요")
    void likeToInfluencer_new() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long influencerId = 1L;
        LikeSingle single = new LikeSingle(influencerId, true);

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(likedInfluencerJpaRepository.findByUserIdAndInfluencerId(userId, influencerId))
            .willReturn(Optional.empty());

        LikedInfluencer newInfluencer = single.toEntity(userId);
        given(likedInfluencerJpaRepository.save(any(LikedInfluencer.class)))
            .willReturn(newInfluencer);

        // when
        influencerCommandService.likeToInfluencer(single);

        // then
        verify(likedInfluencerJpaRepository).save(any(LikedInfluencer.class));
        assertThat(newInfluencer.isLiked()).isTrue();

        authorizationUtil.close();
    }

    @Test
    @DisplayName("기존 인플루언서 한명 좋아요 취소")
    void likeToInfluencer_update() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long influencerId = 1L;
        LikeSingle single = new LikeSingle(influencerId, false);

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        LikedInfluencer existed = new LikedInfluencer(userId, influencerId, true); // 기존 상태 좋아요로 가정
        given(likedInfluencerJpaRepository.findByUserIdAndInfluencerId(userId, influencerId))
            .willReturn(Optional.of(existed));

        // when
        influencerCommandService.likeToInfluencer(single);

        // then
        verify(likedInfluencerJpaRepository, never()).save(any());
        assertThat(existed.isLiked()).isFalse();

        authorizationUtil.close();
    }

    @Test
    @DisplayName("새로운 인플루언서 여러명 좋아요")
    void likeToManyInfluencer_new() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long influencerId1 = 1L;
        Long influencerId2 = 2L;
        LikeMultiple multiple = new LikeMultiple(List.of(
            new LikeSingle(influencerId1, true),
            new LikeSingle(influencerId2, true)
        ));

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(likedInfluencerJpaRepository.findByUserIdAndInfluencerId(anyLong(), anyLong()))
            .willReturn(Optional.empty()); // 항상 없다고 가정

        List<LikedInfluencer> newEntities = new ArrayList<>();
        given(likedInfluencerJpaRepository.save(any(LikedInfluencer.class)))
            .willAnswer(invocation -> {
                LikedInfluencer entity = invocation.getArgument(0);
                newEntities.add(entity);
                return entity;
            });
        // when
        influencerCommandService.likeToManyInfluencer(multiple);

        // then
        verify(likedInfluencerJpaRepository, times(2))
            .save(any(LikedInfluencer.class));
        assertThat(newEntities)
            .extracting(LikedInfluencer::isLiked)
            .containsExactly(true, true);

        authorizationUtil.close();
    }
}