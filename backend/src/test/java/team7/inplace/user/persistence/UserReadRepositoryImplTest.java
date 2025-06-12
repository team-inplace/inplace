package team7.inplace.user.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.user.persistence.dto.UserQueryResult;
import team7.inplace.user.persistence.dto.UserQueryResult.Badge;
import team7.inplace.user.persistence.dto.UserQueryResult.Info;

@DataJpaTest
@Import(ObjectMapper.class)
@ActiveProfiles("test")
@Sql("/sql/test-user.sql")
class UserReadRepositoryImplTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private UserReadRepository userReadRepository;

    @BeforeEach
    void setUp() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(testEntityManager.getEntityManager());
        this.userReadRepository = new UserReadRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("유저 정보 가져오기 테스트")
    void getUserInfoTest() {
        //given
        Long userId = 1L;
        Long expectedUserId = 1L;
        String expectedNickname = "유저1";
        String expectedProfileImgUrl = "img1.png";
        String expectedBadgeImgUrl = "badge1.png";
        String expectedTierImgUrl = "bronze.png";

        //when
        Info acutal = userReadRepository.findUserInfoById(userId).get();
        //then

        assertThat(acutal.userId()).isEqualTo(expectedUserId);
        assertThat(acutal.nickname()).isEqualTo(expectedNickname);
        assertThat(acutal.imgUrl()).isEqualTo(expectedProfileImgUrl);
        assertThat(acutal.badgeImgUrl()).isEqualTo(expectedBadgeImgUrl);
        assertThat(acutal.tierImgUrl()).isEqualTo(expectedTierImgUrl);
    }

    @Test
    @DisplayName("유저가 보유한 모든 칭호 가져오기 Test")
    void getAllBadgeByUserId() {
        //given
        Long userId = 1L;
        Integer expectedListSize = 2;
        List<Long> expectedBadgeIds = List.of(1L, 2L);
        //when
        List<Badge> actual = userReadRepository.findAllBadgeByUserId(userId);
        //then

        assertThat(actual).hasSize(expectedListSize);
        assertThat(actual.stream().map(Badge::id).toList()).isEqualTo(expectedBadgeIds);
    }
}
