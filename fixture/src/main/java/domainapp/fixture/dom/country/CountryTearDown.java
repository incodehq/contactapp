package domainapp.fixture.dom.country;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class CountryTearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"Country\"");
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
