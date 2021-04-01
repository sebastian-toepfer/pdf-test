package com.github.sebastian.toepfer.pdf.test.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author sebastian
 */
final class AllwaysMatch<T> extends TypeSafeMatcher<T> {

    static <T> Matcher<T> allwaysMatch() {
        return new AllwaysMatch<>();
    }

    private AllwaysMatch() {
    }

    @Override
    protected boolean matchesSafely(final T t) {
        return true;
    }

    @Override
    public void describeTo(final Description d) {
    }

}
