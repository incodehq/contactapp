package domainapp.dom.number;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import domainapp.dom.contacts.Contact;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByContactAndType", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.number.ContactNumber "
                        + "WHERE contact == :contact "
                        + "   && type == :type ")
})
@Unique(name = "ContactNumber_label_UNQ", members = { "contact", "type" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class ContactNumber implements Comparable<ContactNumber> {

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Contact contact;

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private String type;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String number;

    //region > compareTo, toString
    @Override
    public int compareTo(final ContactNumber other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "contact", "type");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "contact", "type");
    }
    //endregion

}
