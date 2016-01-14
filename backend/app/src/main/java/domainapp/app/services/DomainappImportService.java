package domainapp.app.services;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.value.Blob;
import org.apache.isis.core.runtime.system.context.IsisContext;

import org.isisaddons.module.excel.dom.ExcelFixture;

import domainapp.fixture.scenarios.demo.ContactImport;

@DomainService(
        //nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        menuBar = DomainServiceLayout.MenuBar.SECONDARY,
        named = "Administration"
)
public class DomainappImportService {

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(cssClass = "btn-warning")
    public List<FixtureResult> importSpreadsheet(final Blob file) {

        FixtureScript script = new ExcelFixture(
                file,
                ContactImport.class
        );
        return fixtureScripts.runFixtureScript(script, "");
    }

    @Inject
    private FixtureScripts fixtureScripts;

    @Inject
    private IsisContext isisContext;

    @Inject
    private DomainObjectContainer container;

}