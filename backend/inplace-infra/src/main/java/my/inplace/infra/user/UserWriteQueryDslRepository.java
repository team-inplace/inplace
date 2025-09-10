package my.inplace.infra.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserWriteQueryDslRepository implements UserWriteRepository {

    private final EntityManager em;

    public void updateBatchReceivedCommentCount(Map<Long, Long> counts) {
        if (counts.isEmpty()) {
            return;
        }

        StringBuilder queryBuilder = new StringBuilder(
            "UPDATE User u SET u.receivedCommentCount = CASE u.id ");

        for (Entry<Long, Long> entry : counts.entrySet()) {
            queryBuilder.append("WHEN ")
                .append(entry.getKey())
                .append(" THEN ")
                .append(entry.getValue())
                .append(" ");
        }

        queryBuilder
            .append("END WHERE u.id IN (")
            .append(counts.keySet().stream().map(String::valueOf).collect(Collectors.joining(",")))
            .append(")");

        Query query = em.createQuery(queryBuilder.toString());
        query.executeUpdate();
        // commentCount 는 조회 하는 로직이 존재하지 않기 때문에 em.clear() 하지 않음.
    }

    @Override
    public void updateBatchUserTiers(Map<Long, Long> tiers) {
        if (tiers.isEmpty()) {
            return;
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE User u SET u.tierId = CASE u.id ");

        for (Entry<Long, Long> entry : tiers.entrySet()) {
            queryBuilder.append("WHEN ")
                .append(entry.getKey())
                .append(" THEN ")
                .append(entry.getValue())
                .append(" ");
        }

        queryBuilder
            .append("END WHERE u.id IN (")
            .append(tiers.keySet().stream().map(String::valueOf).collect(Collectors.joining(",")))
            .append(")");

        Query query = em.createQuery(queryBuilder.toString());
        query.executeUpdate();

        em.clear();
    }
}
