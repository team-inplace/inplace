package team7.inplace.user.persistence;

import jakarta.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserWriteRepositoryImpl implements UserWriteRepository {

    private final EntityManager em;

    public void updateBatchReceivedCommentCount(Map<Long, Long> counts) {
        em.unwrap(Session.class)
            .doWork(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(
                        "update users u set u.received_comment_count = ? where u.id = ?")) {
                        for (Entry<Long, Long> e : counts.entrySet()) {
                            ps.setLong(1, e.getValue());
                            ps.setLong(2, e.getKey());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
            );

        // commentCount 는 조회 하는 로직이 존재하지 않기 때문에 em.clear() 하지 않음.
    }

    @Override
    public void updateBatchUserTiers(Map<Long, Long> tiers) {
        em.unwrap(Session.class)
            .doWork(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(
                        "update users u set u.tier_id = ? where u.id = ?"
                    )) {
                        for (Entry<Long, Long> e : tiers.entrySet()) {
                            ps.setLong(1, e.getValue());
                            ps.setLong(2, e.getKey());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
            );

        em.clear();
    }
}
