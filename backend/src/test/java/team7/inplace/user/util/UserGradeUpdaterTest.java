package team7.inplace.user.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserJpaRepository;
import team7.inplace.user.persistence.UserWriteRepositoryImpl;

@DataJpaTest
@ActiveProfiles("test")
@Import({ObjectMapper.class, UserGradeUpdater.class, UserWriteRepositoryImpl.class})
@Sql("/sql/test-user.sql")
class UserGradeUpdaterTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserGradeUpdater userGradeUpdater;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    void updateGradesByUserIds() {
        // given
        Long userId1 = 1L;
        User user1 = userJpaRepository.findById(userId1).orElse(null);
        assert user1 != null;
        user1.updateReceivedCommentCount(5L);

        Long userId2 = 2L;
        User user2 = userJpaRepository.findById(userId2).orElse(null);
        assert user2 != null;
        user2.updatePostCount(0);
        user2.updateReceivedCommentCount(0L);
        user2.updateReceivedLikeCount(0L);

        Long expected1 = 1L;
        Long expected2 = 1L;

        em.flush();
        em.clear();
        // when
        userGradeUpdater.updateGradesByUserIds(Set.of(userId1, userId2));

        // then
        User actualUser1 = userJpaRepository.findById(userId1).get();
        User actualUser2 = userJpaRepository.findById(userId2).get();
        assertThat(actualUser1.getTierId()).isEqualTo(expected1);
        assertThat(actualUser2.getTierId()).isEqualTo(expected2);
    }
}
