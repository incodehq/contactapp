package domainapp.app.rest.v1.contacts;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.app.rest.ViewModelWithUnderlying;
import domainapp.app.rest.v1.number.ContactNumberViewModel;
import domainapp.app.rest.v1.role.ContactRoleViewModel;
import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.contacts.Contact;
import domainapp.dom.country.Country;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.role.ContactRole;

public class ContactableViewModel extends ViewModelWithUnderlying<ContactableEntity> {

    public enum Type {
        CONTACT,
        CONTACT_GROUP
    }

    public static class Functions {
        private Functions(){}
    }
    public static Function<Contact, ContactableViewModel> createForContact(final DomainObjectContainer container) {
        return new Function<Contact, ContactableViewModel>() {
            @Nullable @Override public ContactableViewModel apply(@Nullable final Contact input) {
                return input != null ? container.injectServicesInto(new ContactableViewModel(input)): null;
            }
        };
    }

    public static Function<ContactGroup, ContactableViewModel> createForGroup(final DomainObjectContainer container) {
        return new Function<ContactGroup, ContactableViewModel>() {
            @Nullable @Override public ContactableViewModel apply(@Nullable final ContactGroup input) {
                return input != null? container.injectServicesInto(new ContactableViewModel(input)): null;
            }
        };
    }

    private Contact contact() {
        return getType() == Type.CONTACT? (Contact) underlying : null;
    }

    private ContactGroup contactGroup() {
        return getType() == Type.CONTACT_GROUP? (ContactGroup) underlying : null;
    }

    public ContactableViewModel() {
    }

    public ContactableViewModel(Contact contact) {
        this.underlying = contact;
    }

    public ContactableViewModel(ContactGroup contactGroup) {
        this.underlying = contactGroup;
    }

    public Type getType() {
        return this.underlying instanceof Contact? Type.CONTACT: Type.CONTACT_GROUP;
    }

    public String getName() {
        final String name = underlying.getName();
        // remove white space and &nbsp;
        return name != null
                ?name.replace(String.valueOf((char) 160), " ").trim()
                :null;
    }

    public String getFirstName() {
        return getType() == Type.CONTACT? firstNameFrom(getName()): "";
    }

    public String getLastName() {
        final String lastName = getType() == Type.CONTACT ? lastNameFrom(getName()) : getName();
        return lastName;
    }

    public String getEmail() {
        return underlying.getEmail();
    }

    public String getNotes() {
        return underlying.getNotes();
    }

    public List<ContactNumberViewModel> getContactNumbers() {
        return Lists.newArrayList(
                Iterables.transform(underlying.getContactNumbers(), ContactNumberViewModel.create(container))
        );
    }

    /**
     * For searching by the filter bar.
     * @return
     */
    @XmlTransient
    public String getContactRoleNames() {
        return Joiner.on(";").join(
                FluentIterable.from(getContactRoles())
                .transform(ContactRoleViewModel.nameOf())
                .filter(Predicates.notNull()));
    }

    /**
     * For {@link Type#CONTACT contacts}, returns the roles they have (in various groups)
     * For {@link Type#CONTACT_GROUP contact groups}, returns the roles that different contacts play within that group.
     *
     * @return
     */
    @XmlTransient
    public List<ContactRoleViewModel> getContactRoles() {
        final Collection<ContactRole> contactRoles =
                getType() == Type.CONTACT_GROUP
                    ? contactGroup().getContactRoles()
                    : contact().getContactRoles();
        return Lists.newArrayList(
                Iterables.transform(
                    contactRoles,
                    ContactRoleViewModel.create(container)));
    }

    /**
     * Only populated for {@link #getType()} of {@link Type#CONTACT}.
     */
    public String getCompany() {
        if (getType() == Type.CONTACT_GROUP) {
            return null;
        }
        return contact().getCompany();
    }

    /**
     * Only populated for {@link #getType()} of {@link Type#CONTACT_GROUP}.
     */
    public String getCountry() {
        if(getType() == Type.CONTACT) return null;
        final Country country = contactGroup().getCountry();
        return country != null? container.titleOf(country): null;
    }

    static String firstNameFrom(final String name) {
        final int i = name.lastIndexOf(" ");
        return i != -1? name.substring(0, i): "";
    }

    static String lastNameFrom(final String name) {
        final int i = name.lastIndexOf(" ");
        return i != -1? name.substring(i+1): name;
    }



}
