package org.incode.eurocommercial.contactapp.webapp.custom;

import org.isisaddons.module.togglz.glue.spi.TogglzModuleFeatureManagerProviderAbstract;

import org.incode.eurocommercial.contactapp.dom.ContactAppFeature;

/**
 * Registered in META-INF/services, as per http://www.togglz.org/documentation/advanced-config.html
 */
public class ContactAppTogglzModuleFeatureManagerProvider extends TogglzModuleFeatureManagerProviderAbstract {

    public ContactAppTogglzModuleFeatureManagerProvider() {
        super(ContactAppFeature.class);
    }

}