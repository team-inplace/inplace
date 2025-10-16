package my.inplace.application.place.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import my.inplace.application.place.command.dto.PlaceCommand;
import my.inplace.domain.place.LikedPlace;
import my.inplace.domain.place.Place;
import my.inplace.infra.place.jpa.CategoryJpaRepository;
import my.inplace.infra.place.jpa.LikedPlaceJpaRepository;
import my.inplace.infra.place.jpa.PlaceJpaRepository;
import my.inplace.infra.place.jpa.PlaceVideoJpaRepository;
import my.inplace.security.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaceCommandServiceTest {

    @Mock
    private PlaceJpaRepository placeJpaRepository;

    @Mock
    private PlaceVideoJpaRepository placeVideoJpaRepository;

    @Mock
    private LikedPlaceJpaRepository likedPlaceJPARepository;

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @InjectMocks
    private PlaceCommandService placeCommandService;

    @Test
    @DisplayName("새로운 장소 추가")
    void createPlace_new() {
        // given
        Long videoId = 1L;
        Long categoryId = 1L;
        Long kakaoPlaceId = 1L;
        PlaceCommand.Create command = new PlaceCommand.Create(
            videoId,
            "name",
            categoryId,
            "테스트 장소 주소",
            "127.0",
            "37.0",
            "googleId",
            kakaoPlaceId
        );

        given(placeJpaRepository.findPlaceByKakaoPlaceId(kakaoPlaceId))
            .willReturn(Optional.empty());

        ArgumentCaptor<Place> placeCaptor = ArgumentCaptor.forClass(Place.class);
        given(placeJpaRepository.save(placeCaptor.capture()))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        placeCommandService.createPlace(command);

        // then
        verify(placeJpaRepository).save(any(Place.class));
        Place savedPlace = placeCaptor.getValue();
        assertThat(savedPlace.getName()).isEqualTo("name");
        assertThat(savedPlace.getAddress().toString()).contains("테스트 장소 주소");
    }

    @Test
    @DisplayName("새로운 장소 좋아요")
    void updateLikedPlace_new() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long placeId = 1L;
        PlaceCommand.Like command = new PlaceCommand.Like(placeId, true);

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(likedPlaceJPARepository.findByUserIdAndPlaceId(userId, placeId))
            .willReturn(Optional.empty());

        ArgumentCaptor<LikedPlace> captor = ArgumentCaptor.forClass(LikedPlace.class);
        given(likedPlaceJPARepository.save(captor.capture()))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        placeCommandService.updateLikedPlace(command);

        // then
        verify(likedPlaceJPARepository).save(any(LikedPlace.class));
        LikedPlace savedPlace = captor.getValue();
        assertThat(savedPlace.getIsLiked()).isTrue();

        authorizationUtil.close();
    }
}