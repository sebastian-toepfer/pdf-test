package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.awt.Color;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author sebastian
 */
class ColorMatcher extends TypeSafeMatcher<Color> {

    public static Matcher<Color> hasColor(final Color color) {
        return new ColorMatcher(color);
    }

    private final Color color;

    public ColorMatcher(final Color color) {
        this.color = color;
    }

    @Override
    protected boolean matchesSafely(final Color color) {
        return color.equals(this.color);
    }

    @Override
    public void describeTo(final Description d) {
        d.appendText("color is ")
                .appendValue(color);
    }

}
