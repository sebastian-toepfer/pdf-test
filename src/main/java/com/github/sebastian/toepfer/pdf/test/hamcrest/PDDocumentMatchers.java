package com.github.sebastian.toepfer.pdf.test.hamcrest;

import com.github.sebastian.toepfer.pdf.test.hamcrest.ContainsText.PDDocumentContainsTextMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.containsString;

/**
 *
 * @author sebastian
 *
 * @since 0.1.0
 */
public final class PDDocumentMatchers {

    private PDDocumentMatchers() {
    }

    public static PDDocumentContainsTextMatcher containsText(final String text) {
        return containsText(containsString(text));
    }

    public static PDDocumentContainsTextMatcher containsText(final Matcher<String> text) {
        return ContainsText.contains(text);
    }

}
