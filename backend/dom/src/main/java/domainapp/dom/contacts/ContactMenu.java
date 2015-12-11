package domainapp.dom.contacts;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Contacts",
        menuOrder = "1"
)
public class ContactMenu {

    @Action(
            semantics = SemanticsOf.SAFE,
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public java.util.List<Contact> listAll() {
        return contactrepository.listAll();
    }

    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public java.util.List<Contact> findByName(
            final String name
    ) {
        return contactrepository.findByNameContains(name);
    }

    @Action(
    )
    @MemberOrder(sequence = "3")
    public Contact create(
            final String name,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String company,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String officeNumber,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String mobileNumber,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String homeNumber,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String email) {
        return contactrepository.create(name, company, email, null, officeNumber, mobileNumber, homeNumber);
    }

    @javax.inject.Inject
    ContactRepository contactrepository;
}
