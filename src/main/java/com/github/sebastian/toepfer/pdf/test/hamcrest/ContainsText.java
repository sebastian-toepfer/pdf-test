package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.github.sebastian.toepfer.pdf.test.hamcrest.AlwaysMatch.alwaysMatch;
import static com.github.sebastian.toepfer.pdf.test.hamcrest.ColorMatcher.hasColor;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author sebastian
 *
 * @since 0.1.0
 */
class ContainsText {

    static PDDocumentContainsTextMatcher contains(final Matcher<String> text) {
        return new PDDocumentContainsTextMatcher(text);
    }

    public static class PDDocumentContainsTextMatcher extends TypeSafeMatcher<PDDocument> {

        private final Matcher<String> text;
        private final Matcher<Point> position;
        private final Matcher<Float> leading;
        private final Matcher<Color> color;
        private final Matcher<Font> font;

        PDDocumentContainsTextMatcher(final Matcher<String> text) {
            this(text, alwaysMatch(), alwaysMatch(), alwaysMatch(), alwaysMatch());
        }

        PDDocumentContainsTextMatcher(final Matcher<String> text, final Matcher<Point> position,
                final Matcher<Float> leading, final Matcher<Color> color, final Matcher<Font> font) {
            this.text = Objects.requireNonNull(text);
            this.position = Objects.requireNonNull(position);
            this.leading = Objects.requireNonNull(leading);
            this.color = Objects.requireNonNull(color);
            this.font = Objects.requireNonNull(font);
        }

        public PDDocumentContainsTextMatcher atPosition(final float x, final float y) {
            return atPosition(is(x), is(y));
        }

        public PDDocumentContainsTextMatcher atPosition(final Matcher<Float> x, final Matcher<Float> y) {
            return new PDDocumentContainsTextMatcher(text, Point.Matcher.atPosition(x, y), leading, color, font);
        }

        public PDDocumentContainsTextMatcher withLeading(final float leading) {
            return withLeading(is(leading));
        }

        public PDDocumentContainsTextMatcher withLeading(final Matcher<Float> leading) {
            return new PDDocumentContainsTextMatcher(text, position, leading, color, font);
        }

        public PDDocumentContainsTextMatcher withColor(final Color color) {
            return new PDDocumentContainsTextMatcher(text, position, leading, hasColor(color), font);
        }

        public PDDocumentContainsTextMatcher withFont(final PDFont font) {
            return new PDDocumentContainsTextMatcher(
                    text,
                    position,
                    leading,
                    color,
                    Font.builder().font(font).build().asMatcher()
            );
        }

        public PDDocumentContainsTextMatcher withFont(final PDFont font, final float size) {
            return new PDDocumentContainsTextMatcher(
                    text,
                    position,
                    leading,
                    color,
                    Font.builder().font(font).size(size).build().asMatcher()
            );
        }

        @Override
        protected boolean matchesSafely(final PDDocument doc) {
            try {
                return hasItem(asTextMatcher())
                        .matches(extractTextFrom(doc.getPages()));
            } catch (IOException ex) {
                Logger.getLogger(ContainsText.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        private Matcher<Text> asTextMatcher() {
            return new Text.Matcher(text, position, leading, color, font);
        }

        @Override
        public void describeTo(final Description desc) {
            desc.appendText("pdf document contains as text ")
                    .appendDescriptionOf(text)
                    .appendDescriptionOf(position)
                    .appendText(" and leading ")
                    .appendDescriptionOf(leading)
                    .appendText(" and color ")
                    .appendDescriptionOf(color)
                    .appendText(" and with font ")
                    .appendDescriptionOf(font);
        }

        private Collection<Text> extractTextFrom(final PDPageTree pages) throws IOException {
            final Collection<Text> result = new ArrayList<>();
            for (PDPage page : pages) {
                if (page.hasContents()) {
                    result.addAll(extractTextFrom(page));
                }
            }
            return result;
        }

        private List<Text> extractTextFrom(final PDPage page) throws IOException {
            final List<Text> result = new ArrayList<>();
            final PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            final List<Object> pageTokens = parser.getTokens();
            Text.Builder builder = Text.builder();
            final List<Float> numbers = new ArrayList<>();
            final List<COSBase> cosObjects = new ArrayList<>();
            for (Object token : pageTokens) {
                if (token instanceof Operator) {
                    final String currentOperatorName = ((Operator) token).getName();
                    switch (currentOperatorName) {
                        case OperatorName.BEGIN_TEXT:
                            builder = Text.builder();
                            break;
                        case OperatorName.MOVE_TEXT:
                            if (numbers.size() == 2) {
                                builder.atPosition(new Point(numbers.get(0), numbers.get(1)));
                            }
                            break;
                        case OperatorName.NEXT_LINE:
                            builder.appendText('\n');
                            break;
                        case OperatorName.SET_TEXT_LEADING:
                            if (numbers.size() == 1) {
                                builder.leading(numbers.get(0));
                            }
                            break;
                        case OperatorName.NON_STROKING_COLOR:
                            if (numbers.size() == 3) {
                                builder.color(numbers.get(0), numbers.get(1), numbers.get(2));
                            }
                            break;
                        case OperatorName.SET_FONT_AND_SIZE:
                            final Font.Builder fontBuilder = Font.builder();
                            if (numbers.size() == 1) {
                                fontBuilder.size(numbers.get(0));
                            }
                            if (cosObjects.size() == 1) {
                                fontBuilder.font((PDFont) cosObjects.get(0).accept(new FontExtractor()));
                            }
                            builder.font(fontBuilder);
                            break;
                        case OperatorName.END_TEXT:
                            result.add(builder.build());
                            break;
                    }
                    numbers.clear();
                    cosObjects.clear();
                } else if (token instanceof COSString) {
                    builder.appendText(((COSString) token).getString());
                } else if (token instanceof COSNumber) {
                    numbers.add(((COSNumber) token).floatValue());
                } else if (token instanceof COSName) {
                    final Object reference = page.getCOSObject().accept(new ReferenceFinder((COSName) token));
                    if (reference instanceof COSBase) {
                        cosObjects.add((COSBase) reference);
                    }
                }
            }
            return result;
        }

    }

}
