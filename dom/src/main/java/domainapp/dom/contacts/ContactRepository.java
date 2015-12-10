package domainapp.dom.contacts;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import javax.inject.Inject;
import java.util.ArrayList;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Contact.class
)
public class ContactRepository {

    @Programmatic
    public java.util.List<Contact> listAll() {
        return container.allInstances(Contact.class);
    }

    @Programmatic
    public Contact findByName(
            final String name
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Contact.class,
                        "findByName",
                        "name", name));
    }

    @Programmatic
    public java.util.List<Contact> findByNameContains(
            final String name
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Contact.class,
                        "findByNameContains",
                        "name", name));
    }

    @Programmatic
    public java.util.List<Contact> findByContactGroup(
            ContactGroup contactGroup
    ) {
        java.util.List<Contact> resContacts = new ArrayList<Contact>();

        for(ContactRole contactRole : contactRoleRepository.findByGroup(contactGroup)) {
            resContacts.add(contactRole.getContact());
        }
        return resContacts;
    }

    @Programmatic
    public java.util.List<Contact> findByContactRoleNameContains(
            String roleName
    ) {
        if(roleName == null) roleName = "";
        java.util.List<Contact> resContacts = new ArrayList<Contact>();

        for(ContactRole contactRole : contactRoleRepository.findByNameContains(roleName)) {
            resContacts.add(contactRole.getContact());
        }

        return resContacts;
    }

    @Programmatic
    public Contact create(
            final String name,
            final String company,
            final String email,
            final String notes,
            final String officeNumber,
            final String mobileNumber,
            final String homeNumber) {
        final Contact contact = container.newTransientInstance(Contact.class);
        contact.setName(name);
        contact.setCompany(company);
        contact.setEmail(email);
        contact.setNotes(notes);

        if(officeNumber != null) {
            contact.addContactNumber("Office", officeNumber);
        }
        if(mobileNumber != null) {
            contact.addContactNumber("Mobile", mobileNumber);
        }
        if(homeNumber != null) {
            contact.addContactNumber("Home", homeNumber);
        }

        container.persistIfNotAlready(contact);
        return contact;
    }

    @Programmatic
    public Contact findOrCreate(
            final String name,
            final String company,
            final String email,
            final String notes,
            final String officeNumber,
            final String mobileNumber,
            final String homeNumber) {
        Contact contact = findByName(name);
        if (contact == null) {
            contact = create(name, company, email, notes, officeNumber, mobileNumber, homeNumber);
        }
        return contact;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;

    @Inject
    ContactRoleRepository contactRoleRepository;
}
