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

    static PDDocumentContainsTextMatcher contains(final String text) {
        return new PDDocumentContainsTextMatcher(text);
    }

    public static class PDDocumentContainsTextMatcher extends TypeSafeMatcher<PDDocument> {

        private final String text;
        private final Matcher<Point> position;

        PDDocumentContainsTextMatcher(final String text) {
            this(text, allwaysMatch());
        }

        PDDocumentContainsTextMatcher(final String text, final Matcher<Point> position) {
            this.text = Objects.requireNonNull(text);
            this.position = Objects.requireNonNull(position);
        }

        public PDDocumentContainsTextMatcher atPosition(final float x, final float y) {
            return atPosition(is(x), is(y));
        }

        private PDDocumentContainsTextMatcher atPosition(final Matcher<Float> x, final Matcher<Float> y) {
            return new PDDocumentContainsTextMatcher(text, Point.Matcher.atPosition(x, y));
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
            return new Text.Matcher(is(text), position);
        }

        @Override
        public void describeTo(final Description desc) {
            desc.appendText("pdf document contains ").appendText(text);
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
                        case OperatorName.END_TEXT:
                            result.add(builder.build());
                            break;
                        case OperatorName.MOVE_TEXT:
                            if (numbers.size() == 2) {
                                builder.atPosition(new Point(numbers.get(0), numbers.get(1)));
                            }
                            break;
                    }
                    numbers.clear();
                } else if (token instanceof COSString) {
                    builder.withText(((COSString) token).getString());
                } else if (token instanceof COSNumber) {
                    numbers.add(((COSNumber) token).floatValue());
                }
            }
            return result;
        }
    }

}
