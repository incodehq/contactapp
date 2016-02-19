package org.incode.eurocommercial.contactapp.dom.number;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.FluentIterable;

/**
 * Default set of {@link ContactNumber#getType() contact number type}s
 */
public enum ContactNumberType {

    OFFICE,
    MOBILE,
    HOME,
    WORK;

    public String title() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static Set<String> titles() {
        return FluentIterable
                .from(Arrays.asList(values()))
                .transform(ContactNumberType::title)
                .toSet();
    }

}
