package domainapp.dom.contacts;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContactMenuTest {

    ContactMenu contactMenu;

    public static final String query = "a";
    public static final String queryInCaseInsensitiveRegex = "(?i).*a.*";

    public static final String regexInQuery = "*a*";
    public static final String regexInQueryInCaseInsensitiveRegex = "(?i).*a.*";

    public static final String regexInQuery2 = "?a?";
    public static final String regexInQueryInCaseInsensitiveRegex2 = "(?i).a.";

    @Before
    public void setup() {
        contactMenu = new ContactMenu();
    }

    @Test
    public void toCaseInsensitiveRegexTests() throws Exception {
        assertEquals(contactMenu.toCaseInsensitiveRegex(query), queryInCaseInsensitiveRegex);
        assertEquals(contactMenu.toCaseInsensitiveRegex(regexInQuery), regexInQueryInCaseInsensitiveRegex);
        assertEquals(contactMenu.toCaseInsensitiveRegex(regexInQuery2), regexInQueryInCaseInsensitiveRegex2);
    }
}
