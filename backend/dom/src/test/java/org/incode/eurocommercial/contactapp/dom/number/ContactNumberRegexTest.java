package org.incode.eurocommercial.contactapp.dom.number;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactNumberRegexTest {

    private ContactNumberRegex spec;

    @Before
    public void setUp() throws Exception {
        spec = new ContactNumberRegex();
    }

    @Test
    public void happy_case() throws Exception {
        assertThat(spec.satisfies("+44 1234 5678")).isNull();
    }

    @Test
    public void happy_case_no_spaces() throws Exception {
        assertThat(spec.satisfies("+44 12345678")).isNull();
    }

    @Test
    public void happy_case_with_brackets() throws Exception {
        assertThat(spec.satisfies("+44 (0)1234 5678")).isNull();
    }

    @Test
    public void happy_case_with_brackets2() throws Exception {
        assertThat(spec.satisfies("+44 (01)1234 5678")).isNull();
    }

    @Test
    public void sad_case_missing_first_plus() throws Exception {
        assertThat(spec.satisfies("44 1234 5678")).isEqualTo(ContactNumberRegex.ERROR_MESSAGE);
    }

    @Test
    public void sad_case_no_space() throws Exception {
        assertThat(spec.satisfies("+441234 5678")).isEqualTo(ContactNumberRegex.ERROR_MESSAGE);
    }

    @Test
    public void when_not_a_string() throws Exception {
        assertThat(spec.satisfies(new Object())).isNull();
    }

    @Test
    public void when_null() throws Exception {
        assertThat(spec.satisfies(new Object())).isNull();
    }
}