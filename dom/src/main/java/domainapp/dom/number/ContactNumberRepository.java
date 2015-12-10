package domainapp.dom.number;

import domainapp.dom.contactable.ContactableEntity;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ContactNumber.class
)
public class ContactNumberRepository {

    @Programmatic
    public java.util.List<ContactNumber> listAll() {
        return container.allInstances(ContactNumber.class);
    }

    @Programmatic
    public ContactNumber findByContactAndType(
            final ContactableEntity contactableEntity,
            final String type) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactNumber.class,
                        "findByContactAndType",
                        "contactableEntity", contactableEntity,
                        "type", type));
    }

    @Programmatic
    public ContactNumber create(final ContactableEntity contactableEntity, final String type, final String number) {
        final ContactNumber contactNumber = container.newTransientInstance(ContactNumber.class);
        contactNumber.setContactableEntity(contactableEntity);
        contactNumber.setType(type);
        contactNumber.setNumber(number);
        container.persistIfNotAlready(contactNumber);
        return contactNumber;
    }

    @Programmatic
    public ContactNumber findOrCreate(
            final ContactableEntity contactableEntity,
            final String type,
            final String number) {
        ContactNumber contactNumber = findByContactAndType(contactableEntity, type);
        if (contactNumber == null) {
            contactNumber = create(contactableEntity, type, number);
        }
        return contactNumber;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
