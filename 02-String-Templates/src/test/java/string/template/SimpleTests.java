package string.template;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTests {
    @Test
    void testStringInterpolation() {

    }

    @Test
    void testTextBlocks() {
        String without = STR."""
                Das sind
                mehrere Zeilen!""";
        Assertions.assertThat(without).isEqualTo("Das sind\nmehrere Zeilen!");

        var with = STR."""
                Das sind
                \{"mehrere"} Zeilen!""";
        Assertions.assertThat(with).isEqualTo("Das sind\nmehrere Zeilen!");
    }
}
