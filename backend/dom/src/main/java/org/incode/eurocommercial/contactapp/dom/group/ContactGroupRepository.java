package org.incode.eurocommercial.contactapp.dom.group;

import java.util.Collections;
import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.eurocommercial.contactapp.dom.country.Country;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ContactGroup.class
)
public class ContactGroupRepository {

    @Programmatic
    public java.util.List<ContactGroup> listAll() {
        final List<ContactGroup> contactGroups = container.allInstances(ContactGroup.class);
        Collections.sort(contactGroups);
        return contactGroups;
    }

    @Programmatic
    public ContactGroup findByCountryAndName(
            final Country country,
            final String name
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactGroup.class,
                        "findByCountryAndName",
                        "country", country,
                        "name", name));
    }

    @Programmatic
    public java.util.List<ContactGroup> findByName(
            String regex
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactGroup.class,
                        "findByName",
                        "regex", regex));
    }

    @Programmatic
    public ContactGroup create(final Country country, final String name) {
        final ContactGroup contactGroup = container.newTransientInstance(ContactGroup.class);
        contactGroup.setCountry(country);
        contactGroup.setName(name);
        container.persistIfNotAlready(contactGroup);
        return contactGroup;
    }

    @Programmatic
    public ContactGroup findOrCreate(
            final Country country,
            final String name
    ) {
        ContactGroup contactGroup = findByCountryAndName(country, name);
        if (contactGroup == null) {
            contactGroup = create(country, name);
        }
        return contactGroup;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
