package team7.inplace.user.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import team7.inplace.user.application.dto.TierConditions.TierCondition;

class TierConditionsTest {

    private TierConditions tierConditions;

    @BeforeEach
    void setUp() {
        tierConditions = new TierConditions(List.of(
            new TierCondition(1L, 0, 0L, 0L),
            new TierCondition(2L, 10, 10L, 10L),
            new TierCondition(3L, 20, 20L, 20L)
        ));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 2, 3, 1",
        "10, 10, 9, 1",
        "10, 10, 10, 2",
        "20, 25, 25, 3"
    })
    @DisplayName("유저 티어 계산 Test")
    void TierConditionsTest(Integer postCount, Long receivedCommentCount, Long receivedLikeCount, Long expectedTierId) {
        Long actualTierId = tierConditions.getCurrentTierId(postCount, receivedCommentCount, receivedLikeCount);

        assertThat(actualTierId).isEqualTo(expectedTierId);
    }
}
