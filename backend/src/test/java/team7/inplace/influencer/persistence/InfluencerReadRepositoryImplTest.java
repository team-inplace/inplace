package team7.inplace.influencer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;

@DataJpaTest
@Import(ObjectMapper.class)
@ActiveProfiles("test")
@Sql("/sql/test-influencer.sql")
class InfluencerReadRepositoryImplTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private InfluencerReadRepository influencerReadRepository;

    /**
     * 테스트 데이터 src/test/resources/sql/test-influencer.sql 참고
     * 1. 인플루언서1 : 비디오 없음, 팔로워 없음
     * 2. 인플루언서2 : 비디오 없음, 팔로워 4명(모두 좋아요)
     * 3. 인플루언서3 : 비디오 없음, 팔로워 3명(일부 좋아요)
     * 4. 인플루언서4 : 비디오 4개, 팔로워 2명(일부 좋아요)
     */
    @BeforeEach
    void setUp() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(testEntityManager.getEntityManager());
        this.influencerReadRepository = new InfluencerReadRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("인플루언서의 비디오가 존재하지 않는경우(Place가 등록되지 않은 경우 포함) 조회 테스트")
    void findInfluencer_NoVideo() {
        //given
        final Long influencerId = 1L;
        final Long userId = null;
        final InfluencerQueryResult.Detail expected = new InfluencerQueryResult.Detail(
            influencerId,
            "인플루언서1",
            "img1",
            "직업1",
            false,
            0L,
            0L
        );

        //when
        final InfluencerQueryResult.Detail actual = influencerReadRepository.getInfluencerDetail(
            influencerId, null).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("인플루언서의 팔로워가 존재하는 경우(모두 True 일 경우), 비디오가 존재하지 않는 경우 조회 테스트")
    void findInfluencer_NoVideo_LikeAllTrue() {
        //given
        final Long influencerId = 2L;
        final Long userId = null;
        final InfluencerQueryResult.Detail expected = new InfluencerQueryResult.Detail(
            influencerId,
            "인플루언서2",
            "img2",
            "직업2",
            false,
            4L,
            0L
        );

        //when
        final InfluencerQueryResult.Detail actual = influencerReadRepository.getInfluencerDetail(
            influencerId, userId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("인플루언서의 팔로워가 존재하는 경우(일부 True일 경우), 비디오가 존재하지 않는 경우 조회 테스트")
    void findInfluencer_NoVideo_LikeSomeTrue() {
        //given
        final Long influencerId = 3L;
        final Long userId = null;
        final InfluencerQueryResult.Detail expected = new InfluencerQueryResult.Detail(
            influencerId,
            "인플루언서3",
            "img3",
            "직업3",
            false,
            3L,
            0L
        );

        //when
        final InfluencerQueryResult.Detail actual = influencerReadRepository.getInfluencerDetail(
            influencerId, userId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("인플루언서의 팔로워가 존재하는 경우(일부 True 경우), 비디오가 존재하는 경우 조회 테스트")
    void findInfluencer_HasVideo_LikeSomeTrue() {
        //given
        final Long influencerId = 4L;
        final Long userId = null;
        final InfluencerQueryResult.Detail expected = new InfluencerQueryResult.Detail(
            influencerId,
            "인플루언서4",
            "img4",
            "직업4",
            false,
            2L,
            4L
        );

        //when
        final InfluencerQueryResult.Detail actual = influencerReadRepository.getInfluencerDetail(
            influencerId, userId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("인플루언서 전체 조회 테스트 - 로그인 하지 않았을 경우")
    void findInfluencer_All_NoLogin() {
        //given
        final Long userId = null;
        final Pageable pageable = Pageable.ofSize(5);
        final boolean expectedLike = false;

        //when
        var result = influencerReadRepository.getInfluencerSortedByLikes(userId, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(4L);
        assertThat(result.getContent().size()).isEqualTo(4);
        result.getContent()
            .forEach(influencer -> {
                assertThat(influencer.isLiked()).isFalse();
            });
    }

    @Test
    @DisplayName("인플루언서 전체 조회 테스트 - 로그인 했을 경우(2, 3번 인플루언서 좋아요 / 페이지네이션 2개씩")
    void findAllInfluencer_liked_Test_Pagination() {
        //given
        final Long userId = 1L;
        final List<Long> expectedIds = List.of(2L, 3L);
        final Pageable pageable = Pageable.ofSize(2);
        final boolean expectedLike = true;

        //when
        var result = influencerReadRepository.getInfluencerSortedByLikes(userId, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(4L);
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().stream().map(InfluencerQueryResult.Simple::id).toList())
            .isEqualTo(expectedIds);
        result.getContent()
            .forEach(influencer -> {
                assertThat(influencer.isLiked()).isTrue();
            });
    }
}
