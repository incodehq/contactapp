package org.incode.eurocommercial.contactapp.dom.number;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.eurocommercial.contactapp.dom.contactable.ContactableEntity;

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
    public ContactNumber findByOwnerAndType(
            final ContactableEntity owner,
            final String type) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactNumber.class,
                        "findByContactAndType",
                        "owner", owner,
                        "type", type));
    }

    @Programmatic
    public ContactNumber create(final ContactableEntity owner, final String type, final String number) {
        final ContactNumber contactNumber = container.newTransientInstance(ContactNumber.class);
        contactNumber.setOwner(owner);
        contactNumber.setType(type);
        contactNumber.setNumber(number);
        container.persistIfNotAlready(contactNumber);
        return contactNumber;
    }

    @Programmatic
    public ContactNumber findOrCreate(
            final ContactableEntity owner,
            final String type,
            final String number) {
        ContactNumber contactNumber = findByOwnerAndType(owner, type);
        if (contactNumber == null) {
            contactNumber = create(owner, type, number);
        }
        return contactNumber;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
