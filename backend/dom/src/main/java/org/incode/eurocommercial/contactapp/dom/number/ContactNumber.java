package org.incode.eurocommercial.contactapp.dom.number;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import org.incode.eurocommercial.contactapp.dom.contactable.ContactableEntity;
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
                        + "FROM org.incode.eurocommercial.contactapp.dom.number.ContactNumber "
                        + "WHERE owner == :owner "
                        + "   && type == :type ")
})
@Unique(name = "ContactNumber_label_UNQ", members = { "owner", "type" })
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
    private ContactableEntity owner;

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
    @ActionLayout(named = "Edit", position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "number", sequence = "1")
    public ContactNumber change(
            final String type,
            final String number) {
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
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "owner", "type");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "owner", "type");
    }
    //endregion

}
