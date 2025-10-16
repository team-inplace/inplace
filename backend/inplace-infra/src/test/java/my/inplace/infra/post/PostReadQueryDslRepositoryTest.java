package my.inplace.infra.post;

import my.inplace.common.cursor.CursorResult;
import my.inplace.domain.post.query.PostQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = PostReadQueryDslRepository.class
    ),
    scripts = "/sql/test-post.sql"
)
class PostReadQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    private PostReadQueryDslRepository postReadRepository;

    @Test
    void findPostsOrderBy() {
        // given
        Long userId = 1L;
        Long cursorId = 9L;
        Integer size = 5;
        String orderBy = "default";
        CursorResult<PostQueryResult.DetailedPost> expected = new CursorResult<>(
            List.of(
                new PostQueryResult.DetailedPost(8L, "nickname4", "img_url4", "silver.png", "badge1.png", "여덟 번째 게시글", "여덟 번째 게시글 내용",
                    List.of(new PostQueryResult.Image("https://example.com/image8.jpg", "vwx234")), false, 1, 0, false, LocalDateTime.of(2025, 9, 1, 0, 0, 7)),
                new PostQueryResult.DetailedPost(7L, "nickname4", "img_url4", "silver.png", "badge1.png", "일곱 번째 게시글", "일곱 번째 게시글 내용",
                    List.of(new PostQueryResult.Image("https://example.com/image7.jpg", "stu901")), true, 1, 0, false, LocalDateTime.of(2025, 9, 1, 0, 0, 6)),
                new PostQueryResult.DetailedPost(6L, "nickname3", "img_url3", "silver.png", "badge3.png", "여섯 번째 게시글", "여섯 번째 게시글 내용",
                    List.of(new PostQueryResult.Image("https://example.com/image6.jpg", "pqr678")), false, 2, 0, false, LocalDateTime.of(2025, 9, 1, 0, 0, 5)),
                new PostQueryResult.DetailedPost(5L, "nickname3", "img_url3", "silver.png", "badge3.png", "다섯 번째 게시글", "다섯 번째 게시글 내용",
                    List.of(new PostQueryResult.Image("https://example.com/image5.jpg", "mno345")), true, 2, 0, false, LocalDateTime.of(2025, 9, 1, 0, 0, 4)),
                new PostQueryResult.DetailedPost(4L, "nickname2", "img_url2", "bronze.png", "badge1.png", "네 번째 게시글", "네 번째 게시글 내용",
                    List.of(new PostQueryResult.Image("https://example.com/image4.jpg", "jkl012")), true, 1, 2, false, LocalDateTime.of(2025, 9, 1, 0, 0, 3))
                ),
            true,
            4L
        );
        // when
        CursorResult<PostQueryResult.DetailedPost> actual = postReadRepository.findPostsOrderBy(userId, cursorId, size, orderBy);

        // then
        assertThat(actual.value()).isEqualTo(expected.value());
        assertThat(actual.hasNext()).isEqualTo(expected.hasNext());
        assertThat(actual.nextCursorId()).isEqualTo(expected.nextCursorId());
    }

    @Test
    void findPostById() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        PostQueryResult.DetailedPost expected = new PostQueryResult.DetailedPost(1L, "nickname1", "img_url1", "bronze.png", "badge2.png", "첫 번째 게시글", "첫 번째 게시글 내용",
            List.of(new PostQueryResult.Image("https://example.com/image1.jpg", "abc123")), false, 2, 2, true, LocalDateTime.of(2025, 9, 1, 0, 0, 0));

        // when
        Optional<PostQueryResult.DetailedPost> actual = postReadRepository.findPostById(postId, userId);

        // then
        assertThat(actual).isEqualTo(Optional.of(expected));
    }

    @Test
    void findCommentUserSuggestions() {
        // given
        Long postId = 1L;
        String keyword = "nickname";
        List<PostQueryResult.UserSuggestion> expected = List.of(
            new PostQueryResult.UserSuggestion(1L, "nickname1", "img_url1"),
            new PostQueryResult.UserSuggestion(2L, "nickname2", "img_url2"),
            new PostQueryResult.UserSuggestion(3L, "nickname3", "img_url3")
        );

        // when
        List<PostQueryResult.UserSuggestion> actual = postReadRepository.findCommentUserSuggestions(postId, keyword);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
