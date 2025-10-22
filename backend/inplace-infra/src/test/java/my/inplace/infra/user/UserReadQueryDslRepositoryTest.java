package my.inplace.infra.user;

import my.inplace.domain.user.query.UserQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = UserReadQueryDslRepository.class
    ),
    scripts = "/sql/test-user.sql"
)
class UserReadQueryDslRepositoryTest extends AbstractMySQLContainer {

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

    @Test
    void getAllBadgesWithOwnerShip() {
        // given
        Long userId = 1L;
        List<UserQueryResult.BadgeWithOwnerShip> expected = List.of(
            new UserQueryResult.BadgeWithOwnerShip(1L, "글쟁이", "badge1.png", "5개 글 작성", true, true),
            new UserQueryResult.BadgeWithOwnerShip(2L, "수다쟁이", "badge2.png", "10개 댓글 작성", true, false),
            new UserQueryResult.BadgeWithOwnerShip(3L, "인기인", "badge3.png", "50개 좋아요", false, false)
        );

        // when
        List<UserQueryResult.BadgeWithOwnerShip> actual = userReadRepository.getAllBadgesWithOwnerShip(userId);

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
