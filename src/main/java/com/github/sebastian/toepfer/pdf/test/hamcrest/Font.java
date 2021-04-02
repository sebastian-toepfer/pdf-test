package com.github.sebastian.toepfer.pdf.test.hamcrest;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.github.sebastian.toepfer.pdf.test.hamcrest.AlwaysMatch.alwaysMatch;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author sebastian
 */
class Font {

    static Builder builder() {
        return new Builder();
    }

    private final PDFont font;
    private final float size;

    private Font(final PDFont font, final float size) {
        this.font = font;
        this.size = size;
    }

    Matcher asMatcher() {
        return new Matcher();
    }

    class Matcher extends TypeSafeMatcher<Font> {

        @Override
        protected boolean matchesSafely(final Font t) {
            return fontMatcher().matches(t.font)
                    && sizeMatcher().matches(t.size);
        }

        @Override
        public void describeTo(final Description d) {
            d.appendDescriptionOf(fontMatcher())
                    .appendText(" and with size ")
                    .appendDescriptionOf(sizeMatcher());
        }

        private org.hamcrest.Matcher<Float> sizeMatcher() {
            final org.hamcrest.Matcher<Float> result;
            if (size <= 0) {
                result = alwaysMatch();
            } else {
                result = is(size);
            }
            return result;
        }

        private org.hamcrest.Matcher<PDFont> fontMatcher() {
            final org.hamcrest.Matcher<PDFont> result;
            if (font == null) {
                result = alwaysMatch();
            } else {
                result = hasName(font);
            }
            return result;
        }

        private org.hamcrest.Matcher<PDFont> hasName(final PDFont font) {
            return new TypeSafeMatcher<>() {

                @Override
                protected boolean matchesSafely(final PDFont t) {
                    return t.getName().equals(font.getName());
                }

                @Override
                public void describeTo(final Description d) {
                    d.appendText("with name ")
                            .appendValue(font.getName());
                }

            };
        }

    }

    static class Builder {

        private PDFont font;
        private float size;

        public Builder size(final float size) {
            this.size = size;
            return this;
        }

        public Builder font(final PDFont font) {
            this.font = font;
            return this;
        }

        public Font build() {
            return new Font(font, size);
        }

    }

}
