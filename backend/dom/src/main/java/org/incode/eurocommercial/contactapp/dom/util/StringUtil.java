package org.incode.eurocommercial.contactapp.dom.util;

import com.google.common.base.Strings;

public class StringUtil {
    private StringUtil() {
    }

    public static String firstNonEmpty(final String... str) {
        for (String s : str) {
            if (!Strings.isNullOrEmpty(s)) {
                return s;
            }
        }
        return null;
    }

    public static String eitherOr(final String role, final String newRole, final String thing) {
        if((Strings.isNullOrEmpty(role) && Strings.isNullOrEmpty(newRole)) || (!Strings.isNullOrEmpty(role) && !Strings.isNullOrEmpty(newRole))) {
            return "Must specify either an (existing) "
                    + thing
                    + " or a new "
                    + thing;
        }
        return null;
    }
}
