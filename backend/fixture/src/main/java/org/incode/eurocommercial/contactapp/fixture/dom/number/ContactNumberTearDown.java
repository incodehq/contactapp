package org.incode.eurocommercial.contactapp.fixture.dom.number;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class ContactNumberTearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"ContactNumber\"");
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
