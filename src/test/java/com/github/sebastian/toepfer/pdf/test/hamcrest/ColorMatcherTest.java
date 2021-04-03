package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.awt.Color;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ColorMatcherTest {

    @Test
    void should_match_same_color() {
        final Color color = Color.yellow;
        assertThat(ColorMatcher.hasColor(color).matches(color), is(true));
    }

    @Test
    void should_match_equals_color() {
        assertThat(ColorMatcher.hasColor(new Color(12, 24, 48)).matches(new Color(12, 24, 48)), is(true));
    }

    @Test
    void should_not_match_non_equals() {
        assertThat(ColorMatcher.hasColor(new Color(12, 24, 48)).matches(new Color(1, 24, 1)), is(false));
    }

}
