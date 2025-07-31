package team7.inplace.user.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.user.application.dto.TierConditions;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserJpaRepository;
import team7.inplace.user.persistence.UserTierJpaRepository;
import team7.inplace.user.persistence.UserWriteRepository;

@Service
@RequiredArgsConstructor
public class UserGradeUpdater {

    private final UserTierJpaRepository userTierJpaRepository;
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

        TierConditions tierConditions = TierConditions.of(userTierJpaRepository.findAll());

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
