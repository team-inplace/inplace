package team7.inplace.user.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.user.persistence.dto.UserQueryResult.Badge;
import team7.inplace.user.persistence.dto.UserQueryResult.Simple;

@DataJpaTest
@Import({ObjectMapper.class})
@ActiveProfiles("test")
@Sql("/sql/test-user.sql")
class UserReadRepositoryImplTest {

    @PersistenceContext
    EntityManager entityManager;

    UserReadRepository userReadRepository;

    @BeforeEach
    void setUp() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        userReadRepository = new UserReadRepositoryImpl(jpaQueryFactory);
    }

    @Test
    @DisplayName("유저 정보 가져오기 테스트")
    void getUserInfoTest() {
        //given
        Long userId = 1L;
        String expectedNickname = "유저1";
        String expectedProfileImgUrl = "img1.png";
        String expectedTierName = "브론즈";
        String expectedTierImgUrl = "bronze.png";
        String expectedBadgeName = "글쟁이";
        String expectedBadgeImgUrl = "badge1.png";

        //when
        Simple actual = userReadRepository.findUserInfoById(userId).get();
        //then

        assertThat(actual.nickname()).isEqualTo(expectedNickname);
        assertThat(actual.imgUrl()).isEqualTo(expectedProfileImgUrl);
        assertThat(actual.tierName()).isEqualTo(expectedTierName);
        assertThat(actual.tierImgUrl()).isEqualTo(expectedTierImgUrl);
        assertThat(actual.mainBadgeName()).isEqualTo(expectedBadgeName);
        assertThat(actual.mainBadgeImgUrl()).isEqualTo(expectedBadgeImgUrl);
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
