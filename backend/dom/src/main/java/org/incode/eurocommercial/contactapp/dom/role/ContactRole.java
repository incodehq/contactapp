package org.incode.eurocommercial.contactapp.dom.role;

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
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
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
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.role.ContactRole "
                        + "WHERE roleName.matches(:regex) "),
        @Query(
                name = "findByContactAndContactGroup", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.role.ContactRole "
                        + "WHERE contact == :contact && contactGroup == :contactGroup "),
        @Query(
                name = "findByContact", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.role.ContactRole "
                        + "WHERE contact == :contact "),
        @Query(
                name = "findByContactGroup", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.role.ContactRole "
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
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactRole implements Comparable<ContactRole> {

    public String title() {
        return getRoleName() != null? getRoleName() : getContactGroup().getName() + "/" + getContact().getName();
    }

    @Column(allowsNull = "false")
    @Property()
    @PropertyLayout(
            hidden = Where.REFERENCES_PARENT
    )
    @Getter @Setter
    private Contact contact;

    @Column(allowsNull = "false")
    @Property()
    @PropertyLayout(
            hidden = Where.REFERENCES_PARENT
    )
    @Getter @Setter
    private ContactGroup contactGroup;

    @Column(allowsNull = "true")
    @Property()
    @Getter @Setter
    private String roleName;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Edit")
    @MemberOrder(name = "roleName", sequence = "1")
    public ContactRole changeRole(@ParameterLayout(named = "Role Name") String roleName) {
        setRoleName(roleName);
        return this;
    }

    public String default0ChangeRole() {
        return getRoleName();
    }


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
