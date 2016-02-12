package org.incode.eurocommercial.contactapp.dom.role;

import java.util.SortedSet;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ContactRole.class
)
public class ContactRoleRepository {

    @Programmatic
    public java.util.List<ContactRole> listAll() {
        return container.allInstances(ContactRole.class);
    }

    @Programmatic
    public SortedSet<String> roleNames() {
        final ImmutableList<String> roleNames =
                FluentIterable
                        .from(listAll())
                        .transform(ContactRole::getRoleName)
                        .filter(Predicates.notNull()).toList();
        return Sets.newTreeSet(roleNames);
    }

    @Programmatic
    public ContactRole findByContactAndContactGroup(
            final Contact contact,
            final ContactGroup contactGroup
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactRole.class,
                        "findByContactAndContactGroup",
                        "contact", contact,
                        "contactGroup", contactGroup));
    }

    @Programmatic
    public java.util.List<ContactRole> findByName(
            final String regex
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactRole.class,
                        "findByName",
                        "regex", regex));
    }

    @Programmatic
    public java.util.List<ContactRole> findByContact(
            final Contact contact
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactRole.class,
                        "findByContact",
                        "contact", contact));
    }

    @Programmatic
    public java.util.List<ContactRole> findByGroup(
            final ContactGroup contactGroup
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactRole.class,
                        "findByContactGroup",
                        "contactGroup", contactGroup));
    }

    @Programmatic
    public ContactRole create(final Contact contact, final ContactGroup contactGroup, final String roleName) {
        final ContactRole contactRole = container.newTransientInstance(ContactRole.class);
        contactRole.setContact(contact);
        contactRole.setContactGroup(contactGroup);
        contactRole.setRoleName(roleName);
        container.persistIfNotAlready(contactRole);
        return contactRole;
    }

    @Programmatic
    public ContactRole findOrCreate(
            final Contact contact,
            final ContactGroup contactGroup,
            final String roleName
    ) {
        ContactRole contactRole = findByContactAndContactGroup(contact, contactGroup);
        if (contactRole == null) {
            contactRole = create(contact, contactGroup, roleName);
        }
        return contactRole;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;

}
