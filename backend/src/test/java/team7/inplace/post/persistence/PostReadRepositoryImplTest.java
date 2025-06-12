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
        Long userId = 1L;
        Long cursorId = null;
        int size = 5;

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
        assertThat(posts.nextCursorId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("게시글 조회 테스트 / 커서기반 페이징 / 다음페이지 없음")
    void findPostsOrderBy_withoutNextPage() {
        // given
        Long userId = null;
        Long cursorId = 4L;
        int size = 5;

        // when
        var posts = postReadRepository.findPostsOrderBy(
            userId,
            cursorId,
            size,
            "createdAt"
        );

        // then
        assertThat(posts.value().size()).isEqualTo(3);
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