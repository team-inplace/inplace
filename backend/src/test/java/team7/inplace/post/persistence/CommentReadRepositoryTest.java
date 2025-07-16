package team7.inplace.post.persistence;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import team7.inplace.container.AbstractMySQLContainerTest;

@DataJpaTest
@ActiveProfiles("test-mysql")
@Import(PostReadRepositoryImplTest.TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/test-comment.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class CommentReadRepositoryTest extends AbstractMySQLContainerTest {

    @Autowired
    private TestEntityManager entityManager;
    private CommentReadRepository commentReadRepository;

    @BeforeEach
    void setUp() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager.getEntityManager());
        commentReadRepository = new CommentReadRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void findCommentsByPostId() {
        // given
        final Long postId = 1L;
        final Long userId = null;
        final Pageable pageable = Pageable.ofSize(5);

        final int expectedCommentSize = 5;
        final int expectedTotalPages = 2;

        // when
        var comments = commentReadRepository.findCommentsByPostId(postId, userId,
            pageable);

        // then
        assertThat(comments).hasSize(expectedCommentSize);
        assertThat(comments.getTotalPages()).isEqualTo(expectedTotalPages);
    }

    @Test
    @DisplayName("댓글 좋아요 조회 테스트")
    void findCommentsByPostIdWithUserId() {
        final Long postId = 1L;
        final Long userId = 1L;
        final Pageable pageable = Pageable.ofSize(5);

        final int expectedCommentSize = 5;
        final int expectedTotalPages = 2;

        // when
        var comments = commentReadRepository.findCommentsByPostId(postId, userId,
            pageable);

        // then
        assertThat(comments).hasSize(expectedCommentSize);
        assertThat(comments.getTotalPages()).isEqualTo(expectedTotalPages);
        assertThat(comments.getContent().get(0).selfLike()).isTrue();
    }
}