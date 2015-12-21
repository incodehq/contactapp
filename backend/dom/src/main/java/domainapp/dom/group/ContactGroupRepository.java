package domainapp.dom.group;

import org.apache.isis.applib.annotation.*;

import domainapp.dom.country.Country;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ContactGroup.class
)
public class  ContactGroupRepository {

    @Programmatic
    public java.util.List<ContactGroup> listAll() {
        return container.allInstances(ContactGroup.class);
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
