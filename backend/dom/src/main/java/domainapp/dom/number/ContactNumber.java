package domainapp.dom.number;

import domainapp.dom.contactable.ContactableEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import javax.jdo.annotations.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
                        + "WHERE contactableEntity == :contactableEntity "
                        + "   && type == :type ")
})
@Unique(name = "ContactNumber_label_UNQ", members = { "contactableEntity", "type" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactNumber implements Comparable<ContactNumber> {

    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Property()
    @Getter @Setter
    private ContactableEntity contactableEntity;

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private String type;

    @Column(allowsNull = "false")
    @Property
    @Title
    @Getter @Setter
    private String number;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Edit")
    @MemberOrder(name = "number", sequence = "1")
    public ContactNumber change(@ParameterLayout(named = "Type") String type,
                                @ParameterLayout(named = "Number") String number) {
        setType(type);
        setNumber(number);
        return this;
    }

    public String default0Change() {
        return getType();
    }
    public String default1Change() {
        return getNumber();
    }

    //region > compareTo, toString
    @Override
    public int compareTo(final ContactNumber other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "contactableEntity", "type");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "contactableEntity", "type");
    }
    //endregion

}
