package team7.inplace.place.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team7.inplace.liked.likedPlace.persistence.LikedPlaceRepository;
import team7.inplace.video.persistence.VideoRepository;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private LikedPlaceRepository likedPlaceRepository;
    @InjectMocks
    private PlaceService placeService;


    @Deprecated
    /**
     * @Author: sanghee0820
     * @Description: Mapping 변경 후 테스트 코드 수정예정
     **/
    @Test
    @DisplayName("좋아요한 장소 정보 조회 테스트")
    void getLikedPlaceInfoTest() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Long userId = 1L;
//        LikedPlace likedPlace = new LikedPlace(user1, place1);
//
//        Page<LikedPlace> likedPlacePage = new PageImpl<>(List.of(likedPlace));
//
//        given(likedPlaceRepository.findByUserIdAndIsLikedTrueWithPlace(userId, pageable))
//                .willReturn(likedPlacePage);
//        given(videoRepository.findByPlaceIdInWithInfluencer(anyList()))
//                .willReturn(List.of(video1));
//
//        Page<LikedPlaceInfo> result = placeService.getLikedPlaceInfo(userId, pageable);
//
//        verify(likedPlaceRepository).findByUserIdAndIsLikedTrueWithPlace(userId, pageable);
//        assertThat(result.getContent().get(0)).isInstanceOf(LikedPlaceInfo.class);
//        assertThat(result.getContent().get(0).influencerName()).isEqualTo(influencer1.getName());
    }
}
