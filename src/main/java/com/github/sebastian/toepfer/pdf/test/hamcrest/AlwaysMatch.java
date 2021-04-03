package com.github.sebastian.toepfer.pdf.test.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 *
 * @author sebastian
 */
final class AlwaysMatch<T> extends BaseMatcher<T> {

    static <T> Matcher<T> alwaysMatch() {
        return new AlwaysMatch<>();
    }

    private AlwaysMatch() {
    }

    @Override
    public boolean matches(final Object o) {
        return true;
    }

    @Override
    public void describeTo(final Description d) {
    }

}
