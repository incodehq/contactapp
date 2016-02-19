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

    private ContactNumber findByOwnerAndNumber(
            final ContactableEntity owner,
            final String number) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ContactNumber.class,
                        "findByOwnerAndNumber",
                        "owner", owner,
                        "number", number));
    }

    private ContactNumber create(
            final ContactableEntity owner,
            final String number,
            final String type) {
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
            final String number,
            final String type) {
        ContactNumber contactNumber = findByOwnerAndNumber(owner, number);
        if (contactNumber == null) {
            contactNumber = create(owner, number, type);
        } else {
            contactNumber.setType(type);
        }
        return contactNumber;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
