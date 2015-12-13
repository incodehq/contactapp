package domainapp.dom.role;

import domainapp.dom.contacts.Contact;
import domainapp.dom.group.ContactGroup;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;

import javax.jdo.annotations.*;

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
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.role.ContactRole "
                        + "WHERE roleName.matches(:regex) "),
        @Query(
                name = "findByContactAndContactGroup", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.role.ContactRole "
                        + "WHERE contact == :contact && contactGroup == :contactGroup "),
        @Query(
                name = "findByContact", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.role.ContactRole "
                        + "WHERE contact == :contact "),
        @Query(
                name = "findByContactGroup", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.role.ContactRole "
                        + "WHERE contactGroup == :contactGroup ")
})
@Unique(name = "ContactRole_roleName_UNQ", members = { "contact", "contactGroup" })
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@MemberGroupLayout(
        columnSpans={6,0,0,6}
)
public class ContactRole implements Comparable<ContactRole> {

    public String title() {
        return getRoleName() != null? getRoleName() : getContactGroup().getName() + "/" + getContact().getName();
    }

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Contact contact;

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private ContactGroup contactGroup;

    @Column(allowsNull = "true")
    @Property()
    @Getter @Setter
    private String roleName;


    //region > compareTo, toString
    @Override
    public int compareTo(final ContactRole other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "contact", "contactGroup");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "contact", "contactGroup");
    }
    //endregion

}