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
    private final float leading;

    private Text(final Point point, final String content, final float leading) {
        this.point = point;
        this.content = content;
        this.leading = leading;
    }

    Builder toBuilder() {
        return builder().atPosition(point)
                .withText(content);
    }

    static final class Matcher extends TypeSafeMatcher<Text> {

        private final org.hamcrest.Matcher<String> contentMatcher;
        private final org.hamcrest.Matcher<Point> positionMatcher;
        private final org.hamcrest.Matcher<Float> leadingMatcher;

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher) {
            this(contentMatcher, positionMatcher, AllwaysMatch.allwaysMatch());
        }

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher,
                final org.hamcrest.Matcher<Float> leadingMatcher) {
            this.contentMatcher = contentMatcher;
            this.positionMatcher = positionMatcher;
            this.leadingMatcher = leadingMatcher;
        }

        @Override
        protected boolean matchesSafely(final Text t) {
            return contentMatcher.matches(t.content)
                    && positionMatcher.matches(t.point)
                    && leadingMatcher.matches(t.leading);
        }

        @Override
        public void describeTo(final Description d) {
            d.appendText(" text with content")
                    .appendDescriptionOf(contentMatcher)
                    .appendText(" at position ")
                    .appendDescriptionOf(positionMatcher)
                    .appendText(" with leading ")
                    .appendDescriptionOf(leadingMatcher);
        }

    }

    static final class Builder {

        private final StringBuilder content;
        private Point point;
        private float leading;

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

        public Builder leading(final float leading) {
            this.leading = leading;
            return this;
        }

        public Text build() {
            return new Text(point, content.toString(), leading);
        }

    }

}
