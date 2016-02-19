package org.incode.eurocommercial.contactapp.dom.number;

import java.util.regex.Pattern;

import org.apache.isis.applib.spec.Specification;

public class ContactNumberSpec implements Specification {

    static final String ERROR_MESSAGE = "Phone number should be in form: +44 1234 5678";
    private static Pattern pattern = Pattern.compile("\\+[\\d]{2} [\\d ]+\\d");

    @Override
    public String satisfies(final Object obj) {
        if(obj == null || !(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        if(pattern.matcher(str).matches()) {
            return null;
        }
        return ERROR_MESSAGE;
    }
}
