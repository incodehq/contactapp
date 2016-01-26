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

import domainapp.dom.group.ContactGroup;


@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(
        named = "Contacts",
        menuOrder = "1"
)
public class ContactMenu {

    @Action(semantics = SemanticsOf.SAFE, restrictTo = RestrictTo.PROTOTYPING)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public java.util.List<Contact> listAll() {
        return contactRepository.listAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public java.util.List<Contact> find(
            final String query
    ) {
        String queryRegex = toCaseInsensitiveRegex(query);
        return contactRepository.find(queryRegex);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "3")
    public java.util.List<Contact> findByGroup(
            final ContactGroup group
    ) {
        return contactRepository.findByContactGroup(group);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "4")
    public java.util.List<Contact> findByRole(
            @Parameter(optionality = Optionality.OPTIONAL) String roleName
    ) {
        String roleNameRegex = toCaseInsensitiveRegex(roleName);
        return contactRepository.findByContactRoleName(roleNameRegex);
    }

    @Action()
    @MemberOrder(sequence = "6")
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
        return contactRepository.create(name, company, email, null, officeNumber, mobileNumber, homeNumber);
    }

    public static String toCaseInsensitiveRegex(final String pattern) {
        if(pattern == null) {
            return null;
        }

        if(pattern.contains("*") || pattern.contains("?")) {
            String regex = pattern;
            regex = pattern.replace("*", ".*").replace("?", ".");
            return "(?i)" + regex;
        } else {
            return "(?i).*" + pattern + ".*";
        }
    }

    @javax.inject.Inject
    ContactRepository contactRepository;
}
