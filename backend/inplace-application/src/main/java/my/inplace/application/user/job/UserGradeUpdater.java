package my.inplace.application.user.job;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.domain.user.User;
import my.inplace.infra.user.UserWriteRepository;
import my.inplace.application.user.dto.TierConditions;
import my.inplace.infra.user.jpa.TierJpaRepository;
import my.inplace.infra.user.jpa.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserGradeUpdater {

    private final TierJpaRepository tierJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserWriteRepository userWriteRepository;

    @Transactional
    public void updateGradesByUserIds(Set<Long> userIds) {
        if (Objects.isNull(userIds) || userIds.isEmpty()) {
            return;
        }

        List<User> usersToCheck = userIds.stream()
            .map(userId
                -> userJpaRepository.findById(userId)
                .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND)))
            .toList();

        TierConditions tierConditions = TierConditions.of(tierJpaRepository.findAll());

        Map<Long, Long> usersToUpdate = new HashMap<>();
        for (User user : usersToCheck) {
            Long calculatedTierId = tierConditions.getCurrentTierId(
                user.getPostCount(),
                user.getReceivedCommentCount(),
                user.getReceivedLikeCount()
            );

            if (!Objects.equals(user.getTierId(), calculatedTierId)) {
                usersToUpdate.put(user.getId(), calculatedTierId);
            }
        }

        userWriteRepository.updateBatchUserTiers(usersToUpdate);
    }
}
