package team7.inplace.post.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import team7.inplace.container.AbstractMySQLContainerTest;
import team7.inplace.global.converter.JsonNodeConverter;

@DataJpaTest
@ActiveProfiles("test-mysql")
@Import(PostReadRepositoryImplTest.TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/test-post.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class PostReadRepositoryImplTest extends AbstractMySQLContainerTest {

    @Autowired
    private TestEntityManager entityManager;
    private PostReadRepository postReadRepository;

    @BeforeEach
    void setUp() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager.getEntityManager());
        postReadRepository = new PostReadRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("게시글 조회 테스트 / 커서기반 페이징 / 다음페이지 있음")
    void findPostsOrderBy_withNextPage() {
        // given
        final Long userId = 1L;
        final Long cursorId = null;
        final int size = 5;
        final long expectedCursorId = 2L;
        // when
        var posts = postReadRepository.findPostsOrderBy(
            userId,
            cursorId,
            size,
            "createdAt"
        );

        // then
        assertThat(posts.value().size()).isEqualTo(size);
        assertThat(posts.hasNext()).isTrue();
        assertThat(posts.nextCursorId()).isEqualTo(expectedCursorId);
    }

    @Test
    @DisplayName("게시글 조회 테스트 / 커서기반 페이징 / 다음페이지 없음")
    void findPostsOrderBy_withoutNextPage() {
        // given
        final Long userId = null;
        final Long cursorId = 2L;
        final int size = 5;
        final int expectedSize = 1;

        // when
        var posts = postReadRepository.findPostsOrderBy(
            userId,
            cursorId,
            size,
            "createdAt"
        );

        // then
        assertThat(posts.value().size()).isEqualTo(expectedSize);
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.nextCursorId()).isNull();
    }

    @Test
    @DisplayName("게시글 QueryResult 사진 변환 테스트")
    void detailedPostPhotosConversionTest() {
        // given
        Long userId = 1L;
        Long cursorId = null;
        int size = 5;
        int photoSize = 1;

        // when
        var posts = postReadRepository.findPostsOrderBy(
            userId,
            cursorId,
            size,
            "createdAt"
        );

        // then
        assertThat(posts.value().get(0).imageInfos()).isNotNull();
        assertThat(posts.value().get(0).getImageUrls().size()).isEqualTo(photoSize);
    }

    @Test
    @DisplayName("댓글 언급기능 사용자 추천 조회 테스트 - 검색어가 빈칸 인 경우")
    void findCommentUserSuggestions_NoKeyword() {
        // given
        Long postId = 1L;
        String keyword = "";
        final int expectedSize = 3;
        // when
        var suggestions = postReadRepository.findCommentUserSuggestions(postId, keyword);

        // then
        assertThat(suggestions).hasSize(expectedSize);
    }

    @Test
    @DisplayName("댓글 언급기능 사용자 추천 조회 테스트 - 검색어가 null인 경우")
    void findCommentUserSuggestions_NullKeyword() {
        // given
        Long postId = 1L;
        String keyword = null;
        final int expectedSize = 3;
        // when
        var suggestions = postReadRepository.findCommentUserSuggestions(postId, keyword);

        // then
        assertThat(suggestions).hasSize(expectedSize);
    }

    @Test
    @DisplayName("댓글 언급기능 사용자 추천 조회 테스트 - 검색어가 존재하는 경우")
    void findCommentUserSuggestions_WithKeyword() {
        // given
        Long postId = 1L;
        String keyword = "1";
        final int expectedSize = 1;
        final int expectedUserId = 1;

        // when
        var suggestions = postReadRepository.findCommentUserSuggestions(postId, keyword);

        // then
        assertThat(suggestions).hasSize(expectedSize);
        assertThat(suggestions.get(0).userId()).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("댓글 언급기능 사용자 추천 조회 테스트 - 검색 결과가 없는 경우")
    void findCommentUserSuggestions_NoResults() {
        // given
        Long postId = 1L;
        String keyword = "nonexistent";
        final int expectedSize = 0;

        // when
        var suggestions = postReadRepository.findCommentUserSuggestions(postId, keyword);

        // then
        assertThat(suggestions).hasSize(expectedSize);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public JsonNodeConverter jsonNodeConverter(ObjectMapper objectMapper) {
            return new JsonNodeConverter(objectMapper);
        }
    }

}