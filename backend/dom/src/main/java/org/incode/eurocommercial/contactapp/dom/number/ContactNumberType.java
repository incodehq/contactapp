package org.incode.eurocommercial.contactapp.dom.number;

public enum ContactNumberType  {

    OFFICE,
    MOBILE,
    HOME,
    WORK;

    public String title() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
