package org.incode.eurocommercial.contactapp.dom.contacts;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumberType;
import org.incode.eurocommercial.contactapp.dom.role.ContactRole;
import org.incode.eurocommercial.contactapp.dom.role.ContactRoleRepository;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Contact.class
)
public class ContactRepository {

    @Programmatic
    public java.util.List<Contact> listAll() {
        return asSortedList(container.allInstances(Contact.class));
    }

    @Programmatic
    public java.util.List<Contact> find(
            final String regex
    ) {
        java.util.SortedSet<Contact> contacts = Sets.newTreeSet();
        contacts.addAll(findByName(regex));
        contacts.addAll(findByCompany(regex));
        contacts.addAll(findByContactRoleName(regex));
        for(ContactGroup contactGroup : contactGroupRepository.findByName(regex)) {
            contacts.addAll(findByContactGroup(contactGroup));
        }
        return asSortedList(contacts);
    }

    @Programmatic
    public java.util.List<Contact> findByName(
            final String regex
    ) {
        final List<Contact> contacts = container.allMatches(
                new QueryDefault<>(
                        Contact.class,
                        "findByName",
                        "regex", regex));
        return asSortedList(contacts);
    }

    @Programmatic
    public java.util.List<Contact> findByCompany(
            final String regex
    ) {
        final List<Contact> contacts = container.allMatches(
                new QueryDefault<>(
                        Contact.class,
                        "findByCompany",
                        "regex", regex));
        return asSortedList(contacts);
    }

    @Programmatic
    public java.util.List<Contact> findByContactGroup(
            ContactGroup contactGroup
    ) {
        java.util.SortedSet<Contact> contacts = Sets.newTreeSet();
        for(ContactRole contactRole : contactRoleRepository.findByGroup(contactGroup)) {
            contacts.add(contactRole.getContact());
        }
        return asSortedList(contacts);
    }

    @Programmatic
    public java.util.List<Contact> findByContactRoleName(
            String regex
    ) {
        java.util.SortedSet<Contact> contacts = Sets.newTreeSet();

        for(ContactRole contactRole : contactRoleRepository.findByName(regex)) {
            contacts.add(contactRole.getContact());
        }

        return asSortedList(contacts);
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
            contact.addContactNumber(ContactNumberType.OFFICE, officeNumber);
        }
        if(mobileNumber != null) {
            contact.addContactNumber(ContactNumberType.MOBILE, mobileNumber);
        }
        if(homeNumber != null) {
            contact.addContactNumber(ContactNumberType.HOME, homeNumber);
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

    @Programmatic
    public void delete(final Contact contact) {
        final SortedSet<ContactRole> contactRoles = contact.getContactRoles();
        for (ContactRole contactRole : contactRoles) {
            container.removeIfNotAlready(contactRole);
        }
        container.removeIfNotAlready(contact);
    }

    private static List<Contact> asSortedList(final List<Contact> contacts) {
        Collections.sort(contacts);
        return contacts;
    }

    private static  List<Contact> asSortedList(final SortedSet<Contact> contactsSet) {
        final List<Contact> contacts = Lists.newArrayList();
        // no need to sort, just copy over
        contacts.addAll(contactsSet);
        return contacts;
    }


    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;

    @Inject
    private ContactRoleRepository contactRoleRepository;

    @Inject
    private ContactGroupRepository contactGroupRepository;

}
