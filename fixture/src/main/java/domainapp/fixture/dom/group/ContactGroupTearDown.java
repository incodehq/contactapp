package domainapp.fixture.dom.group;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class ContactGroupTearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"group\".\"ContactGroup\"");
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
