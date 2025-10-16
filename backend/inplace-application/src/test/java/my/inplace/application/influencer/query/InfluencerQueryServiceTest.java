package my.inplace.application.influencer.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import my.inplace.application.influencer.query.dto.InfluencerResult;
import my.inplace.application.influencer.query.dto.InfluencerResult.Simple;
import my.inplace.domain.base.BaseEntity;
import my.inplace.domain.influencer.Influencer;
import my.inplace.domain.influencer.LikedInfluencer;
import my.inplace.domain.influencer.query.InfluencerQueryResult;
import my.inplace.domain.influencer.query.InfluencerQueryResult.Detail;
import my.inplace.domain.influencer.query.InfluencerReadRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class InfluencerQueryServiceTest {

    @Mock
    private InfluencerJpaRepository influencerRepository;

    @Mock
    private LikedInfluencerJpaRepository likedInfluencerJpaRepository;

    @Mock
    private InfluencerReadRepository influencerReadRepository;

    @InjectMocks
    private InfluencerQueryService influencerQueryService;

    @Test
    @DisplayName("좋아요한 인플루언서 페이지 조회")
    void getFavoriteInfluencers() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 2);

        LikedInfluencer likedInfluencer1 = new LikedInfluencer(userId, 1L, true);
        LikedInfluencer likedInfluencer2 = new LikedInfluencer(userId, 2L, true);

        Page<LikedInfluencer> likedPage = new PageImpl<>(List.of(likedInfluencer1, likedInfluencer2), pageable, 2);
        given(likedInfluencerJpaRepository.findByUserIdAndIsLikedTrue(userId, pageable))
            .willReturn(likedPage);

        Influencer influencer1 = new Influencer("influencer1", "imgUrl1", "job1", "title1", "channelId1");
        Influencer influencer2 = new Influencer("influencer2", "imgUrl2", "job2", "title2", "channelId2");
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(influencer1, 1L);
        idField.set(influencer2, 2L);

        given(influencerRepository.findAllById(List.of(1L, 2L)))
            .willReturn(List.of(influencer1, influencer2));

        // when
        Page<Simple> result = influencerQueryService.getFavoriteInfluencers(userId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
            .extracting(Simple::name)
            .containsExactly("influencer1", "influencer2");

        verify(likedInfluencerJpaRepository).findByUserIdAndIsLikedTrue(userId, pageable);
        verify(influencerRepository).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("인플루언서 상세 조회")
    void getInfluencerDetail() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long influencerId = 1L;
        Long userId = 1L;

        given(AuthorizationUtil.getUserIdOrNull()).willReturn(userId);
        InfluencerQueryResult.Detail queryDetail = new Detail(influencerId, "influencer1", "imgUrl", "job", true, 1L, 1L);
        given(influencerReadRepository.getInfluencerDetail(influencerId, userId))
            .willReturn(Optional.of(queryDetail));

        // when
        InfluencerResult.Detail detail = influencerQueryService.getInfluencerDetail(influencerId);

        // then
        assertThat(detail.name()).isEqualTo("influencer1");
        verify(influencerReadRepository).getInfluencerDetail(influencerId, userId);

        authorizationUtil.close();
    }
}