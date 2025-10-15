package my.inplace.infra.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import my.inplace.domain.user.query.UserQueryResult;
import my.inplace.domain.user.query.UserQueryResult.Badge;
import my.inplace.domain.user.query.UserQueryResult.Simple;
import my.inplace.infra.config.TestQueryDslConfig;
import my.inplace.infra.influencer.InfluencerReadQueryDslRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = UserReadQueryDslRepository.class
    )
)
@Sql(scripts = "/sql/test-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import(TestQueryDslConfig.class)
@EntityScan("my.inplace.domain")
class UserReadQueryDslRepositoryTest {

    @Autowired
    UserReadQueryDslRepository userReadRepository;

    @Test
    void findUserInfoById() {
        // given
        Long userId = 1L;
        UserQueryResult.Simple expected = new UserQueryResult.Simple("유저1", "img1.png", "브론즈", "bronze.png", "글쟁이", "badge1.png");

        // when
        Optional<UserQueryResult.Simple> actual = userReadRepository.findUserInfoById(userId);

        // then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findAllBadgeByUserId() {
        // given
        Long userId = 1L;
        List<UserQueryResult.Badge> expected = List.of(
            new UserQueryResult.Badge(1L, "글쟁이", "badge1.png", "5개 글 작성"),
            new UserQueryResult.Badge(2L, "수다쟁이", "badge2.png", "10개 댓글 작성")
        );

        // when
        List<UserQueryResult.Badge> actual = userReadRepository.findAllBadgeByUserId(userId);

        // then
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
