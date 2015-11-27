package domainapp.dom.contacts;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

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
}
