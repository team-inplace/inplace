package team7.inplace.video.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team7.inplace.place.domain.Category;
import team7.inplace.video.application.AliasUtil;

public class VideoUtilTest {
    @Test
    @DisplayName("받침 없을 때")
    void test1() {
        String alias = AliasUtil.makeAlias("풍자", Category.CAFE);
        System.out.println("alias = " + alias);
        Assertions.assertThat(alias.charAt(2)).isEqualTo('가');
    }

    @Test
    @DisplayName("받침 있을 때")
    void test2() {
        String alias = AliasUtil.makeAlias("성시경", Category.CAFE);
        System.out.println("alias = " + alias);
        Assertions.assertThat(alias.charAt(3)).isEqualTo('이');
    }
}
