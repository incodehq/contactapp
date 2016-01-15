package domainapp.dom.contacts;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import domainapp.dom.utils.StringUtils;

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
    public java.util.List<Contact> find(
            final String regex
    ) {
        String pattern = StringUtils.wildcardToCaseInsensitiveRegex(regex);
        java.util.Set<Contact> results = new HashSet<Contact>();

        results.addAll(findByName(pattern));
        results.addAll(findByContactRoleName(pattern));
        for(ContactGroup contactGroup : contactGroupRepository.findByName(pattern)) {
            results.addAll(findByContactGroup(contactGroup));
        }

        java.util.List<Contact> resultsList = new ArrayList<Contact>();
        resultsList.addAll(results);

        return resultsList;
    }

    @Programmatic
    public java.util.List<Contact> findByName(
            final String regex
    ) {
        String pattern = StringUtils.wildcardToCaseInsensitiveRegex(regex);
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Contact.class,
                        "findByName",
                        "regex", pattern));
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
    public java.util.List<Contact> findByContactRoleName(
            String regex
    ) {
        java.util.List<Contact> resContacts = new ArrayList<Contact>();

        for(ContactRole contactRole : contactRoleRepository.findByName(regex)) {
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
        java.util.List<Contact> contacts = findByName(name);
        Contact contact;
        if (contacts.size() == 0) {
            contact = create(name, company, email, notes, officeNumber, mobileNumber, homeNumber);
        } else {
            contact = contacts.get(0);
        }
        return contact;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;

    @Inject
    private ContactRoleRepository contactRoleRepository;

    @Inject
    private ContactGroupRepository contactGroupRepository;
}
