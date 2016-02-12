package org.incode.eurocommercial.contactapp.dom;

import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum ContactAppFeature implements org.togglz.core.Feature {

    @Label("Dummy feature")
    @EnabledByDefault
    dummy;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}
