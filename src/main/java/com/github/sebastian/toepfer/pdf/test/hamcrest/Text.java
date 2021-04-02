package com.github.sebastian.toepfer.pdf.test.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author sebastian
 */
class Text {

    public static Builder builder() {
        return new Builder();
    }

    private final Point point;
    private final String content;

    public Text(final Point point, final String content) {
        this.point = point;
        this.content = content;
    }

    Builder toBuilder() {
        return builder().atPosition(point)
                .withText(content);
    }

    static final class Matcher extends TypeSafeMatcher<Text> {

        private final org.hamcrest.Matcher<String> contentMatcher;
        private final org.hamcrest.Matcher<Point> positionMatcher;

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher) {
            this.contentMatcher = contentMatcher;
            this.positionMatcher = positionMatcher;
        }

        @Override
        protected boolean matchesSafely(final Text t) {
            return contentMatcher.matches(t.content)
                    && positionMatcher.matches(t.point);
        }

        @Override
        public void describeTo(final Description d) {
            d.appendText(" text with content")
                    .appendDescriptionOf(contentMatcher)
                    .appendText(" at position ")
                    .appendDescriptionOf(positionMatcher);
        }

    }

    static final class Builder {

        private final StringBuilder content;
        private Point point;

        private Builder() {
            content = new StringBuilder();
        }

        public Builder atPosition(final Point point) {
            this.point = point;
            return this;
        }

        public Builder withText(final String content) {
            this.content.setLength(0);
            this.content.append(content);
            return this;
        }

        public Builder appendText(final char character) {
            this.content.append(character);
            return this;
        }

        public Builder appendText(final CharSequence charSequence) {
            this.content.append(charSequence);
            return this;
        }

        public Text build() {
            return new Text(point, content.toString());
        }

    }

}
