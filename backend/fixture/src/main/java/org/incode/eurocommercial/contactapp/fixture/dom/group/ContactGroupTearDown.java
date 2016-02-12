package org.incode.eurocommercial.contactapp.fixture.dom.group;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class ContactGroupTearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"ContactGroup\"");
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
