package domainapp.dom.contacts;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.number.ContactNumber;
import domainapp.dom.number.ContactNumberRepository;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "contacts",
        table = "Contact"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByNameContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.contacts.Contact "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.contacts.Contact "
                        + "WHERE name == :name ")
})
@Unique(name = "Contact_name_UNQ", members = { "name" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Contact implements Comparable<Contact> {

    public String title() {
        return getName();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String name;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String company;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String email;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String notes;



    @Persistent(mappedBy = "contact", dependentElement = "true")
    @Collection()
    @CollectionLayout(
            render = RenderType.EAGERLY
    )
    @Getter @Setter
    private SortedSet<ContactNumber> contactNumbers = new TreeSet<ContactNumber>();

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactNumbers", sequence = "1")
    public Contact addContactNumber(String type, String number) {
        contactNumberRepository.findOrCreate(this, type, number);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Remove")
    @MemberOrder(name = "contactNumbers", sequence = "2")
    public Contact removeContactNumber(String type) {
        final Optional<ContactNumber> contactNumberIfAny = Iterables
                .tryFind(getContactNumbers(), cn -> Objects.equal(cn.getType(), type));

        if(contactNumberIfAny.isPresent()) {
            getContactNumbers().remove(contactNumberIfAny.get());
        }
        return this;
    }

    public String default0RemoveContactNumber() {
        return getContactNumbers().size() == 1? getContactNumbers().iterator().next().getType(): null;
    }
    public List<String> choices0RemoveContactNumber() {
        return Lists.transform(Lists.newArrayList(getContactNumbers()), ContactNumber::getType);
    }

    public String disableRemoveContactNumber() {
        return getContactNumbers().isEmpty()? "No contact numbers to remove": null;
    }



    @Persistent(mappedBy = "contact", dependentElement = "false")
    @Collection()
    @Getter @Setter
    private SortedSet<ContactRole> contactRoles = new TreeSet<ContactRole>();

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactRoles", sequence = "1")
    public Contact addContactRole(ContactGroup contactGroup, String roleName) {
        contactRoleRepository.findOrCreate(this, contactGroup, roleName);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Remove")
    @MemberOrder(name = "contactRoles", sequence = "2")
    public Contact removeContactRole(ContactGroup contactGroup) {
        final Optional<ContactRole> contactRoleIfAny = Iterables
                .tryFind(getContactRoles(), cn -> Objects.equal(cn.getContactGroup(), contactGroup));

        if(contactRoleIfAny.isPresent()) {
            getContactRoles().remove(contactRoleIfAny.get());
        }
        return this;
    }

    public ContactGroup default0RemoveContactRole() {
        return getContactRoles().size() == 1? getContactRoles().iterator().next().getContactGroup(): null;
    }
    public List<ContactGroup> choices0RemoveContactRole() {
        return Lists.transform(Lists.newArrayList(getContactRoles()), ContactRole::getContactGroup);
    }
    public String disableRemoveContactRole() {
        return getContactRoles().isEmpty()? "No contact numbers to remove": null;
    }


    //region > compareTo, toString
    @Override
    public int compareTo(final Contact other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "name");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "name");
    }
    //endregion

    @Inject
    ContactRoleRepository contactRoleRepository;
    @Inject
    ContactNumberRepository contactNumberRepository;
}
