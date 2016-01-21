package domainapp.app.rest.v1.role;

import javax.annotation.Nullable;

import com.google.common.base.Function;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.app.rest.ViewModelWithUnderlying;
import domainapp.app.rest.v1.contacts.ContactableViewModel;
import domainapp.dom.contacts.Contact;
import domainapp.dom.country.Country;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.role.ContactRole;

public class ContactRoleViewModel extends ViewModelWithUnderlying<ContactRole> {

    public static Function<ContactRoleViewModel, String> nameOf() {
        return new Function<ContactRoleViewModel, String>() {
            @Nullable @Override public String apply(@Nullable final ContactRoleViewModel contactRoleViewModel) {
                return contactRoleViewModel.getRoleName();
            }
        };
    }

    public static Function<ContactRole, ContactRoleViewModel> create(final DomainObjectContainer container) {
        return new Function<ContactRole, ContactRoleViewModel>() {
            @Nullable @Override public ContactRoleViewModel apply(@Nullable final ContactRole input) {
                return input != null? container.injectServicesInto(new ContactRoleViewModel(input)): null;
            }
        };
    }

    public ContactRoleViewModel() {
    }

    public ContactRoleViewModel(final ContactRole underlying) {
        this.underlying = underlying;
    }

    public ContactableViewModel getContact() {
        return ContactableViewModel.createForContact(container).apply(underlying.getContact());
    }

    public ContactableViewModel getContactGroup() {
        return ContactableViewModel.createForGroup(container).apply(underlying.getContactGroup());
    }

    public String getRoleName() {
        return underlying.getRoleName();
    }

    /**
     * The {@link #getContact()}'s {@link Contact#getName() name}.
     */
    public String getContactName() {
        return underlying.getContact().getName();
    }

    /**
     * The {@link #getContact()}'s {@link Contact#getCompany() company}.
     */
    public String getContactCompany() {
        return underlying.getContact().getCompany();
    }

    /**
     * The {@link #getContactGroup()}'s {@link ContactGroup#getName() name}.
     */
    public String getContactGroupName() {
        return underlying.getContactGroup().getName();
    }

    /**
     * The {@link #getContactGroup()}'s {@link ContactGroup#getCountry() country's}  {@link Country#getName() name}.
     */
    public String getContactGroupCountryName() {
        final Country country = underlying.getContactGroup().getCountry();
        return country != null? country.getName(): null;
    }

}
