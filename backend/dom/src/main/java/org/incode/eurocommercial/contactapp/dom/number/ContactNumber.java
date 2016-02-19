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
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import org.incode.eurocommercial.contactapp.dom.ContactAppDomainModule;
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
                name = "findByOwnerAndNumber", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.number.ContactNumber "
                        + "WHERE owner == :owner "
                        + "   && number == :number ")
})
@Unique(name = "ContactNumber_owner_number_UNQ", members = { "owner", "number" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactNumber implements Comparable<ContactNumber> {

    public static class MaxLength {
        private MaxLength(){}
        public static final int TYPE = 20;
        public static final int NUMBER = 30;
        public static final int NOTES = ContactAppDomainModule.MaxLength.NOTES;
    }


    @Column(allowsNull = "false")
    @Property
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private ContactableEntity owner;

    @Column(allowsNull = "false", length = MaxLength.TYPE)
    @Property
    @Getter @Setter
    private String type;

    @Title
    @Column(allowsNull = "false", length = MaxLength.NUMBER)
    @Property
    @Getter @Setter
    private String number;

    @Column(allowsNull = "true", length = MaxLength.NOTES)
    @Property
    @PropertyLayout(multiLine = 6, hidden = Where.ALL_TABLES)
    @Getter @Setter
    private String notes;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "number", sequence = "1")
    public ContactNumber edit(
            @Parameter(maxLength = MaxLength.NUMBER, mustSatisfy = ContactNumberSpec.class)
            final String number,
            @Parameter(maxLength = MaxLength.TYPE)
            final String type,
            @Parameter(maxLength = MaxLength.NOTES)
            final String notes) {
        setNumber(number);
        setType(type);
        setNotes(notes);
        return this;
    }

    public String default0Edit() {
        return getNumber();
    }
    public String default1Edit() {
        return getType();
    }
    public String default2Edit() {
        return getNotes();
    }

    //region > compareTo, toString
    @Override
    public int compareTo(final ContactNumber other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "owner", "number");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "owner", "number", "type");
    }
    //endregion

}
