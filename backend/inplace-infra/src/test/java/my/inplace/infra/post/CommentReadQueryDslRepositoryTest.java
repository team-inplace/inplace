package my.inplace.infra.post;

import my.inplace.domain.post.query.CommentQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = CommentReadQueryDslRepository.class
    ),
    scripts = "/sql/test-post.sql"
)
class CommentReadQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    CommentReadQueryDslRepository commentReadRepository;

    @Test
    void findCommentsByPostId() {
        // given
        Long postId = 2L;
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        List<CommentQueryResult.DetailedComment> expected = List.of(
            new CommentQueryResult.DetailedComment(3L, "nickname4", "img_url4", "silver.png", "badge1.png", "세 번째 댓글", true, 1, false, LocalDateTime.of(2025, 9, 1, 0, 0, 3)),
            new CommentQueryResult.DetailedComment(4L, "nickname1", "img_url1", "bronze.png", "badge2.png", "네 번째 댓글", false, 1, true, LocalDateTime.of(2025, 9, 1, 0, 0, 4))
        );

        // when
        Page<CommentQueryResult.DetailedComment> actual = commentReadRepository.findCommentsByPostId(postId, userId, pageable);

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
        assertThat(actual.getContent()).isEqualTo(expected);
    }
}
