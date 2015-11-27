package domainapp.dom.country;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Country.class
)
public class CountryRepository {

    @Programmatic
    public java.util.List<Country> listAll() {
        return container.allInstances(Country.class);
    }

    @Programmatic
    public Country findByName(
            final String name
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Country.class,
                        "findByName",
                        "name", name));
    }

    @Programmatic
    public java.util.List<Country> findByNameContains(
            final String name
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Country.class,
                        "findByNameContains",
                        "name", name));
    }

    @Programmatic
    public Country create(final String name) {
        final Country country = container.newTransientInstance(Country.class);
        country.setName(name);
        container.persistIfNotAlready(country);
        return country;
    }

    @Programmatic
    public Country findOrCreate(
            final String name
    ) {
        Country country = findByName(name);
        if (country == null) {
            country = create(name);
        }
        return country;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
