package team7.inplace.influencer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.application.dto.LikedInfluencerCommand;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.liked.likedInfluencer.domain.LikedInfluencer;
import team7.inplace.liked.likedInfluencer.persistent.LikedInfluencerRepository;
import team7.inplace.security.util.AuthorizationUtil;

@ExtendWith(MockitoExtension.class)
public class InfluencerServiceTest {

    @Mock
    private InfluencerRepository influencerRepository;

    @Mock
    private LikedInfluencerRepository likedInfluencerRepository;

    @InjectMocks
    private InfluencerService influencerService;

    @Test
    @DisplayName("로그인 안한 사용자가 인플루언서 조회")
    void getAllInfluencersTest_NotLoggedIn() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);

        var influencer1 = new Influencer("influencer1", "imgUrl1", "job1", "title1", "channelId1");
        var influencer2 = new Influencer("influencer2", "imgUrl2", "job2", "title2", "channelId2");
        Page<Influencer> influencersPage = new PageImpl<>(List.of(influencer1, influencer2));

        given(influencerRepository.findAll(pageable))
            .willReturn(influencersPage);
        given(AuthorizationUtil.isNotLoginUser())
            .willReturn(true);

        // when
        Page<InfluencerInfo> influencerInfoPage = influencerService.getAllInfluencers(pageable);

        // then
        assertThat(influencerInfoPage).hasSize(2);
        assertThat(influencerInfoPage.getContent().get(0))
            .extracting("influencerName", "likes")
            .containsExactly(influencer1.getName(), false);
        assertThat(influencerInfoPage.getContent().get(1))
            .extracting("influencerName", "likes")
            .containsExactly(influencer2.getName(), false);

        authorizationUtil.close();
    }

    @Test
    @DisplayName("로그인 한 사용자가 인플루언서 목록 조회 테스트")
    void getAllInfluencersTest_LoggedIn() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);

        var influencer1 = new Influencer("influencer1", "imgUrl1", "job1", "title1", "channelId1");
        var influencer2 = new Influencer("influencer2", "imgUrl2", "job2", "title2", "channelId2");
        var influencer3 = new Influencer("influencer3", "imgUrl3", "job3", "title2", "channelId3");
        Page<Influencer> influencersPage = new PageImpl<>(
            List.of(influencer1, influencer2, influencer3));

        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(influencer1, 1L);
        idField.set(influencer2, 2L);
        idField.set(influencer3, 3L);

        given(influencerRepository.findAll(pageable)).willReturn(influencersPage);
        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        // 2, 3번째 인플루언서 좋아요로 설정
        given(likedInfluencerRepository.findLikedInfluencerIdsByUserId(userId))
            .willReturn(Set.of(2L, 3L));

        // when
        Page<InfluencerInfo> influencerInfoPage = influencerService.getAllInfluencers(pageable);

        // then
        assertThat(influencerInfoPage.getContent())
            .hasSize(3)
            .extracting("influencerId", "likes")
            .containsExactly(
                tuple(2L, true),
                tuple(3L, true),
                tuple(1L, false)
            );

        authorizationUtil.close();
    }

    @Test
    @DisplayName("좋아요한 인플루언서 페이징 조회")
    void getFavoriteInfluencersTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        var likedInfluencer1 = new LikedInfluencer(userId, 1L, true);
        var likedInfluencer2 = new LikedInfluencer(userId, 2L, true);
        var likedInfluencerPage = new PageImpl<>(List.of(likedInfluencer1, likedInfluencer2));

        var influencer1 = new Influencer("influencer1", "imgUrl1", "job1", "title1", "channelId1");
        var influencer2 = new Influencer("influencer2", "imgUrl2", "job2", "title2", "channelId2");

        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(influencer1, 1L);
        idField.set(influencer2, 2L);

        given(likedInfluencerRepository.findByUserIdAndIsLikedTrue(userId, pageable))
            .willReturn(likedInfluencerPage);
        given(influencerRepository.findAllById(List.of(1L, 2L)))
            .willReturn(List.of(influencer1, influencer2));

        // when
        Page<InfluencerInfo> result = influencerService.getFavoriteInfluencers(userId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).influencerName()).isEqualTo("influencer1");
        assertThat(result.getContent().get(1).influencerName()).isEqualTo("influencer2");

        verify(likedInfluencerRepository).findByUserIdAndIsLikedTrue(userId, pageable);
        verify(influencerRepository).findAllById(List.of(1L, 2L));
    }


    @Test
    @DisplayName("한명 인플루언서 좋아요 - 새로 등록")
    void likeToInfluencerTest_insert() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long influencerId = 1L;
        var commandSingle = new LikedInfluencerCommand.Single(influencerId, true);

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(likedInfluencerRepository.findByUserIdAndInfluencerId(anyLong(), anyLong()))
            .willReturn(Optional.empty());  // 새 객체 생성되도록

        final LikedInfluencer[] savedInfluencer = {null};
        given(likedInfluencerRepository.save(any(LikedInfluencer.class)))
            .willAnswer(invocation -> {
                savedInfluencer[0] = invocation.getArgument(0);
                return savedInfluencer[0];
            });

        // when
        influencerService.likeToInfluencer(commandSingle);

        // then
        verify(likedInfluencerRepository, times(1)).save(
            any(LikedInfluencer.class));  // LikedInfluencer가 저장되었는지 확인
        assertThat(savedInfluencer[0])
            .extracting(LikedInfluencer::getUserId, LikedInfluencer::getInfluencerId,
                LikedInfluencer::isLiked)
            .containsExactly(userId, influencerId, true);

        authorizationUtil.close();
    }

    @Test
    @DisplayName("한명 인플루언서 좋아요 취소")
    void likeToInfluencerTest_update() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long influencerId = 1L;
        var commandSingle = new LikedInfluencerCommand.Single(influencerId, false);
        var existinglikedInfluencer = new LikedInfluencer(userId, influencerId, true);

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(likedInfluencerRepository.findByUserIdAndInfluencerId(anyLong(), anyLong()))
            .willReturn(Optional.of(existinglikedInfluencer));

        // when
        influencerService.likeToInfluencer(commandSingle);

        // then
        assertThat(existinglikedInfluencer.isLiked()).isFalse();
        verify(likedInfluencerRepository, never()).save(any(LikedInfluencer.class));

        authorizationUtil.close();
    }

    @Test
    @DisplayName("여러명 인플루언서 좋아요 - 새로 등록")
    void likeToManyInfluencersTest() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long influencerId1 = 1L;
        Long influencerId2 = 2L;
        var commandSingle1 = new LikedInfluencerCommand.Single(influencerId1, true);
        var commandSingle2 = new LikedInfluencerCommand.Single(influencerId2, true);
        var commandMultiple = new LikedInfluencerCommand.Multiple(
            List.of(commandSingle1, commandSingle2));

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(likedInfluencerRepository.findByUserIdAndInfluencerId(anyLong(), anyLong()))
            .willReturn(Optional.empty()); // 새 객체 생성되도록

        List<LikedInfluencer> savedInfluencers = new ArrayList<>();
        given(likedInfluencerRepository.save(any(LikedInfluencer.class)))
            .willAnswer(invocation -> {
                LikedInfluencer saved = invocation.getArgument(0);
                savedInfluencers.add(saved);
                return saved;
            });

        // when
        influencerService.likeToManyInfluencer(commandMultiple);

        // then
        verify(likedInfluencerRepository, times(2)).save(any(LikedInfluencer.class));
        assertThat(savedInfluencers)
            .hasSize(2)
            .extracting(LikedInfluencer::getUserId, LikedInfluencer::getInfluencerId,
                LikedInfluencer::isLiked)
            .containsExactly(
                tuple(userId, influencerId1, true),
                tuple(userId, influencerId2, true)
            );

        authorizationUtil.close();
    }

}

