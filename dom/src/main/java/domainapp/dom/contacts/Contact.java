package domainapp.dom.contacts;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import domainapp.dom.contactable.ContactableEntity;
import org.apache.isis.applib.annotation.*;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.number.ContactNumber;
import domainapp.dom.number.ContactNumberRepository;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
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
@DomainObject(
        editing = Editing.DISABLED
)
public class Contact extends ContactableEntity {

    public String title() {
        return getName();
    }

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String company;

    @Persistent(mappedBy = "contact", dependentElement = "false")
    @Collection()
    @CollectionLayout(render = RenderType.EAGERLY)
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

    @Inject
    ContactRoleRepository contactRoleRepository;
    @Inject
    ContactNumberRepository contactNumberRepository;
}
