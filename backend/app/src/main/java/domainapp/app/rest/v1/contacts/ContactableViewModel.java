package domainapp.app.rest.v1.contacts;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.app.rest.v1.country.CountryViewModel;
import domainapp.app.rest.v1.number.ContactNumberViewModel;
import domainapp.app.rest.v1.role.ContactRoleViewModel;
import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.contacts.Contact;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.role.ContactRole;

@XmlRootElement(name = "contactable")
public class ContactableViewModel {

    enum Type {
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
        return type == Type.CONTACT? (Contact) contactable : null;
    }

    private ContactGroup contactGroup() {
        return type == Type.CONTACT_GROUP? (ContactGroup) contactable : null;
    }

    public ContactableViewModel() {
    }

    public ContactableViewModel(Contact contact) {
        this.contactable = contact;
        this.type = Type.CONTACT;
    }

    public ContactableViewModel(ContactGroup contactGroup) {
        this.contactable = contactGroup;
        this.type = Type.CONTACT_GROUP;
    }

    @XmlElement(required = true)
    private Type type;

    public Type getType() {
        return type;
    }

    @XmlElement(required = true)
    private ContactableEntity contactable;

    @XmlTransient
    public String getName() {
        return contactable.getName();
    }

    @XmlTransient
    public String getEmail() {
        return contactable.getEmail();
    }

    @XmlTransient
    public String getNotes() {
        return contactable.getNotes();
    }

    @XmlTransient
    public List<ContactNumberViewModel> getContactNumbers() {
        return Lists.newArrayList(
                Iterables.transform(contactable.getContactNumbers(), ContactNumberViewModel.create(container))
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
                type == Type.CONTACT_GROUP
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
    @XmlTransient
    public String getCompany() {
        if (type == Type.CONTACT_GROUP) {
            return null;
        }
        return contact().getCompany();
    }

    /**
     * Only populated for {@link #getType()} of {@link Type#CONTACT_GROUP}.
     */
    @XmlTransient
    public CountryViewModel getCountry() {
        if(type == Type.CONTACT) return null;
        return CountryViewModel.create(container).apply(contactGroup().getCountry());
    }

    public String title() {
        return container.titleOf(contactable);
    }

    @Override
    public String toString() {
        return contactable != null? contactable.toString(): "(no underlying)";
    }

    @Inject
    DomainObjectContainer container;



}
