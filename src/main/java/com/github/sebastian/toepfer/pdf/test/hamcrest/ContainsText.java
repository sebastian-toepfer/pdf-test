package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.github.sebastian.toepfer.pdf.test.hamcrest.AllwaysMatch.allwaysMatch;
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

        PDDocumentContainsTextMatcher(final Matcher<String> text) {
            this(text, allwaysMatch());
        }

        PDDocumentContainsTextMatcher(final Matcher<String> text, final Matcher<Point> position) {
            this(text, position, allwaysMatch());
        }

        PDDocumentContainsTextMatcher(final Matcher<String> text, final Matcher<Point> position, final Matcher<Float> leading) {
            this.text = Objects.requireNonNull(text);
            this.position = Objects.requireNonNull(position);
            this.leading = Objects.requireNonNull(leading);
        }

        public PDDocumentContainsTextMatcher atPosition(final float x, final float y) {
            return atPosition(is(x), is(y));
        }

        public PDDocumentContainsTextMatcher atPosition(final Matcher<Float> x, final Matcher<Float> y) {
            return new PDDocumentContainsTextMatcher(text, Point.Matcher.atPosition(x, y), leading);
        }

        public PDDocumentContainsTextMatcher withLeading(final float leading) {
            return withLeading(is(leading));
        }

        public PDDocumentContainsTextMatcher withLeading(final Matcher<Float> leading) {
            return new PDDocumentContainsTextMatcher(text, position, leading);
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
            return new Text.Matcher(text, position, leading);
        }

        @Override
        public void describeTo(final Description desc) {
            desc.appendText("pdf document contains as text ")
                    .appendDescriptionOf(text)
                    .appendDescriptionOf(position)
                    .appendText(" and leading ")
                    .appendDescriptionOf(leading);
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
                        case OperatorName.END_TEXT:
                            result.add(builder.build());
                            break;
                    }
                    numbers.clear();
                } else if (token instanceof COSString) {
                    builder.appendText(((COSString) token).getString());
                } else if (token instanceof COSNumber) {
                    numbers.add(((COSNumber) token).floatValue());
                }
            }
            return result;
        }
    }

}
