package team7.inplace.user.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TierConditionsTest {

    @ParameterizedTest
    @CsvSource({
        "1L, 2L, 3L"
    })
    @DisplayName("유저 티어 계산 Test")
    void TierConditionsTest(Integer postCount, Long receivedCommentCount, Long receivedLikeCount) {
        assertThat(postCount).isEqualTo(1L);
    }
}
