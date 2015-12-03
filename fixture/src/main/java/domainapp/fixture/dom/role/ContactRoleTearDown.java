package domainapp.fixture.dom.role;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class ContactRoleTearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"ContactRole\"");
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
