package org.incode.eurocommercial.contactapp.fixture.dom.contact;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class ContactTearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"Contact\"");
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
