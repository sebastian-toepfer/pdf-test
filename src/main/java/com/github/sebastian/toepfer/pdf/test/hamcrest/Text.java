package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.awt.Color;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.github.sebastian.toepfer.pdf.test.hamcrest.AlwaysMatch.alwaysMatch;

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
    private final Font font;

    private Text(final Point point, final String content, final float leading, final Color color, final Font font) {
        this.point = point;
        this.content = content;
        this.leading = leading;
        this.color = color;
        this.font = font;
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
        private final org.hamcrest.Matcher<Font> fontMatcher;

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher) {
            this(contentMatcher, positionMatcher, alwaysMatch());
        }

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher,
                final org.hamcrest.Matcher<Float> leadingMatcher) {
            this(contentMatcher, positionMatcher, leadingMatcher, alwaysMatch());
        }

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher,
                final org.hamcrest.Matcher<Float> leadingMatcher,
                final org.hamcrest.Matcher<Color> colorMatcher) {
            this(contentMatcher, positionMatcher, leadingMatcher, colorMatcher, alwaysMatch());
        }

        public Matcher(final org.hamcrest.Matcher<String> contentMatcher,
                final org.hamcrest.Matcher<Point> positionMatcher, final org.hamcrest.Matcher<Float> leadingMatcher,
                final org.hamcrest.Matcher<Color> colorMatcher,
                final org.hamcrest.Matcher<Font> fontMatcher) {
            this.contentMatcher = contentMatcher;
            this.positionMatcher = positionMatcher;
            this.leadingMatcher = leadingMatcher;
            this.colorMatcher = colorMatcher;
            this.fontMatcher = fontMatcher;
        }

        @Override
        protected boolean matchesSafely(final Text t) {
            return contentMatcher.matches(t.content)
                    && positionMatcher.matches(t.point)
                    && leadingMatcher.matches(t.leading)
                    && colorMatcher.matches(t.color)
                    && fontMatcher.matches(t.font);
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
                    .appendDescriptionOf(colorMatcher)
                    .appendText(" with font ")
                    .appendDescriptionOf(fontMatcher);
        }

    }

    static final class Builder {

        private final StringBuilder content;
        private Point point;
        private float leading;
        private Color color;
        private Font font;

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

        public Builder color(final float red, final float green, final float blue) {
            this.color = new Color(red, green, blue);
            return this;
        }

        public Builder font(final Font.Builder fontBuilder) {
            return font(fontBuilder.build());
        }

        public Builder font(final Font font) {
            this.font = font;
            return this;
        }

        public Text build() {
            return new Text(point, content.toString(), leading, color, font);
        }

    }

}
