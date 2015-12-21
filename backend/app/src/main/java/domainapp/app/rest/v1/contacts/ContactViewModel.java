package domainapp.app.rest.v1.contacts;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.app.rest.v1.contactable.ContactableViewModel;
import domainapp.app.rest.v1.number.ContactNumberViewModel;
import domainapp.app.rest.v1.role.ContactRoleViewModel;
import domainapp.dom.contacts.Contact;

@XmlRootElement(name = "contact")
public class ContactViewModel implements ContactableViewModel {

    public static Function<Contact, ContactViewModel> create(final DomainObjectContainer container) {
        return new Function<Contact, ContactViewModel>() {
            @Nullable @Override public ContactViewModel apply(@Nullable final Contact input) {
                return input != null ? container.injectServicesInto(new ContactViewModel(input)): null;
            }
        };
    }

    @XmlElement(required = true)
    private Contact underlying;

    public ContactViewModel() {
    }

    public ContactViewModel(Contact underlying) {
        this.underlying = underlying;
    }

    @XmlTransient
    public String getCompany() {
        return underlying.getCompany();
    }

    @XmlTransient
    @Override
    public String getName() {
        return underlying.getName();
    }

    @XmlTransient
    @Override
    public String getEmail() {
        return underlying.getEmail();
    }

    @XmlTransient
    @Override
    public String getNotes() {
        return underlying.getNotes();
    }


    @XmlTransient
    @Override
    public List<ContactNumberViewModel> getContactNumbers() {
        return Lists.newArrayList(
                Iterables.transform(underlying.getContactNumbers(), ContactNumberViewModel.create(container))
        );
    }

    @XmlTransient
    public List<ContactRoleViewModel> getContactRoles() {
        return Lists.newArrayList(
                Iterables.transform(underlying.getContactRoles(), ContactRoleViewModel.create(container))
        );
    }

    public String title() {
        return underlying != null? container.titleOf(underlying): "(no underlying)";
    }
    @Override
    public String toString() {
        return underlying != null? underlying.toString(): "(no underlying)";
    }

    @Inject
    DomainObjectContainer container;

}
