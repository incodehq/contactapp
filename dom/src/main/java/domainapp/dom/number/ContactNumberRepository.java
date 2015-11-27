package domainapp.dom.number;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import domainapp.dom.contacts.Contact;

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
            final Contact contact,
            final String type) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactNumber.class,
                        "findByContactAndType",
                        "contact", contact,
                        "type", type));
    }

    @Programmatic
    public ContactNumber create(final Contact contact, final String type, final String number) {
        final ContactNumber contactNumber = container.newTransientInstance(ContactNumber.class);
        contactNumber.setContact(contact);
        contactNumber.setType(type);
        contactNumber.setNumber(number);
        container.persistIfNotAlready(contactNumber);
        return contactNumber;
    }

    @Programmatic
    public ContactNumber findOrCreate(
            final Contact contact,
            final String type,
            final String number) {
        ContactNumber contactNumber = findByContactAndType(contact, type);
        if (contactNumber == null) {
            contactNumber = create(contact, type, number);
        }
        return contactNumber;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
