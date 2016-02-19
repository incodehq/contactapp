package org.incode.eurocommercial.contactapp.dom.number;

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
}
