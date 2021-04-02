package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.awt.Color;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.github.sebastian.toepfer.pdf.test.hamcrest.AllwaysMatch.allwaysMatch;

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
    private final Color color;

    private Text(final Point point, final String content, final float leading, final Color color) {
        this.point = point;
        this.content = content;
        this.leading = leading;
        this.color = color;
    }

    Builder toBuilder() {
        return builder().atPosition(point)
                .withText(content);
    }

    static final class Matcher extends TypeSafeMatcher<Text> {

        private final org.hamcrest.Matcher<String> contentMatcher;
        private final org.hamcrest.Matcher<Point> positionMatcher;
        private final org.hamcrest.Matcher<Float> leadingMatcher;
        private final org.hamcrest.Matcher<Color> colorMatcher;

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher) {
            this(contentMatcher, positionMatcher, allwaysMatch());
        }

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher,
                final org.hamcrest.Matcher<Float> leadingMatcher) {
            this(contentMatcher, positionMatcher, leadingMatcher, allwaysMatch());
        }

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher,
                final org.hamcrest.Matcher<Float> leadingMatcher,
                final org.hamcrest.Matcher<Color> colorMatcher) {
            this.contentMatcher = contentMatcher;
            this.positionMatcher = positionMatcher;
            this.leadingMatcher = leadingMatcher;
            this.colorMatcher = colorMatcher;
        }

        @Override
        protected boolean matchesSafely(final Text t) {
            return contentMatcher.matches(t.content)
                    && positionMatcher.matches(t.point)
                    && leadingMatcher.matches(t.leading)
                    && colorMatcher.matches(t.color);
        }

        @Override
        public void describeTo(final Description d) {
            d.appendText(" text with content")
                    .appendDescriptionOf(contentMatcher)
                    .appendText(" at position ")
                    .appendDescriptionOf(positionMatcher)
                    .appendText(" with leading ")
                    .appendDescriptionOf(leadingMatcher)
                    .appendText(" with color ")
                    .appendDescriptionOf(colorMatcher);
        }

    }

    static final class Builder {

        private final StringBuilder content;
        private Point point;
        private float leading;
        private Color color;

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

        void color(final float red, final float green, final float blue) {
            this.color = new Color(red, green, blue);
        }

        public Text build() {
            return new Text(point, content.toString(), leading, color);
        }

    }

}
