package com.github.sebastian.toepfer.pdf.test.hamcrest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author sebastian
 */
class AlwaysMatchTest {

    @Test
    void should_match_null() {
        assertThat(AlwaysMatch.alwaysMatch().matches(null), is(true));
    }

    @Test
    void should_match_true() {
        assertThat(AlwaysMatch.alwaysMatch().matches(true), is(true));
    }

    @Test
    void should_match_object() {
        assertThat(AlwaysMatch.alwaysMatch().matches(new Object()), is(true));
    }

}
