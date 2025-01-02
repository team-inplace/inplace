package team7.inplace.likedInfluencer;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team7.inplace.liked.likedInfluencer.application.FavoriteInfluencerService;
import team7.inplace.liked.likedInfluencer.domain.FavoriteInfluencer;
import team7.inplace.liked.likedInfluencer.persistent.FavoriteInfluencerRepository;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.user.domain.Role;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserType;

@ExtendWith(MockitoExtension.class)
public class FavoriteInfluencerServiceTest {

    @Mock
    private FavoriteInfluencerRepository favoriteRepository;

    @InjectMocks
    private FavoriteInfluencerService favoriteInfluencerService;

    @Test
    void getFavoriteInfluencers() {
        Pageable pageable = PageRequest.of(0, 10);
        Long userId = 1L;
        User user = new User("name", "password", "nickname", UserType.KAKAO, Role.USER);
        Influencer influencer1 = new Influencer("influencer1", "imgUrl1", "job1");
        FavoriteInfluencer favoriteInfluencer1 = new FavoriteInfluencer(user, influencer1);
        favoriteInfluencer1.updateLike(true);
        Page<FavoriteInfluencer> favoriteInfluencersPage = new PageImpl<>(
            List.of(favoriteInfluencer1));

        given(favoriteRepository.findByUserIdAndIsLikedTrue(userId, pageable))
            .willReturn(favoriteInfluencersPage);

        Page<InfluencerInfo> result = favoriteInfluencerService.getFavoriteInfluencers(userId,
            pageable);

        verify(favoriteRepository).findByUserIdAndIsLikedTrue(userId, pageable);
        assertThat(result.getContent().get(0)).isInstanceOf(InfluencerInfo.class);
        assertThat(result.getContent().get(0).influencerName()).isEqualTo(influencer1.getName());
        assertThat(result.getContent().get(0).likes()).isEqualTo(true);
    }
}
