package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.util.Objects;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author sebastian
 */
class Point {

    private final float x;
    private final float y;

    Point(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Float.floatToIntBits(this.x);
        hash = 37 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        return Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y);
    }

    static class Matcher extends TypeSafeMatcher<Point> {

        static org.hamcrest.Matcher<Point> atPosition(final org.hamcrest.Matcher<Float> x,
                final org.hamcrest.Matcher<Float> y) {
            return new Matcher(x, y);
        }

        private final org.hamcrest.Matcher<Float> x;
        private final org.hamcrest.Matcher<Float> y;

        private Matcher(final org.hamcrest.Matcher<Float> x, final org.hamcrest.Matcher<Float> y) {
            this.x = Objects.requireNonNull(x);
            this.y = Objects.requireNonNull(y);
        }

        @Override
        protected boolean matchesSafely(final Point t) {
            return x.matches(t.x)
                    && y.matches(t.y);
        }

        @Override
        public void describeTo(final Description d) {
            d.appendText(" at position x ")
                    .appendDescriptionOf(x)
                    .appendText(" and y ")
                    .appendDescriptionOf(y);
        }

        @Override
        protected void describeMismatchSafely(final Point item, final Description mismatchDescription) {
            mismatchDescription.appendText("x was ");
            x.describeMismatch(item.x, mismatchDescription);
            mismatchDescription.appendText(" and y was");
            y.describeMismatch(item.y, mismatchDescription);
        }

    }

}
