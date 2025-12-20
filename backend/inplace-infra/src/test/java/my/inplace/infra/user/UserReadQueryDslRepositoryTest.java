package my.inplace.infra.user;

import my.inplace.domain.user.query.UserQueryResult;
import my.inplace.domain.user.query.UserQueryResult.Info;
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
        Info expected = new Info("유저1", "img1.png", "브론즈", "bronze.png", "글쟁이", "badge1.png");

        // when
        Optional<Info> actual = userReadRepository.findUserInfoById(userId);

        // then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void getAllBadgesWithOwnerShip() {
        // given
        Long userId = 1L;
        List<UserQueryResult.BadgeWithOwnerShip> expected = List.of(
            new UserQueryResult.BadgeWithOwnerShip(1L, "글쟁이", "badge1.png", "5개 글 작성", true),
            new UserQueryResult.BadgeWithOwnerShip(2L, "수다쟁이", "badge2.png", "10개 댓글 작성", true),
            new UserQueryResult.BadgeWithOwnerShip(3L, "인기인", "badge3.png", "50개 좋아요", false)
        );

        // when
        List<UserQueryResult.BadgeWithOwnerShip> actual = userReadRepository.getAllBadgesWithOwnerShip(userId);

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
