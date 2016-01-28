package domainapp.dom.contacts;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import domainapp.dom.number.ContactNumberRepository;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.contacts.Contact "
                        + "WHERE name.matches(:regex) ")
})
@DomainObject(
        editing = Editing.DISABLED
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class Contact extends ContactableEntity implements Comparable<Contact> {

    public String title() {
        return getName();
    }

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String company;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(
            named = "Edit",
            position = ActionLayout.Position.PANEL
    )
    @MemberOrder(name = "Notes", sequence = "1")
    public ContactableEntity change(
            final String name,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String company,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String email,
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(multiLine = 6)
            final String notes) {
        setName(name);
        setCompany(company);
        setEmail(email);
        setNotes(notes);
        return this;
    }

    public String default0Change() {
        return getName();
    }
    public String default1Change() {
        return getCompany();
    }
    public String default2Change() {
        return getEmail();
    }
    public String default3Change() {
        return getNotes();
    }

    @Persistent(mappedBy = "contact", dependentElement = "false")
    @Collection()
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<ContactRole> contactRoles = new TreeSet<ContactRole>();

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactRoles", sequence = "1")
    public Contact addContactRole(
            @Parameter(optionality = Optionality.MANDATORY)
            ContactGroup contactGroup,
            @Parameter(optionality = Optionality.OPTIONAL)
            String existingRole,
            @Parameter(optionality = Optionality.OPTIONAL)
            String newRole) {
        final String roleName = existingRole != null? existingRole: newRole;
        contactRoleRepository.findOrCreate(this, contactGroup, roleName);
        return this;
    }

    public List<ContactGroup> choices0AddContactRole() {
        return contactGroupRepository.listAll();
    }
    public SortedSet<String> choices1AddContactRole() {
        final ImmutableList<String> roleNames = FluentIterable.from(contactRoleRepository.listAll())
                .transform(new Function<ContactRole, String>() {
                    @Nullable @Override public String apply(final ContactRole contactRole) {
                        return contactRole.getRoleName();
                    }
                }).filter(Predicates.notNull()).toList();
        return Sets.newTreeSet(roleNames);
    }

    public String validate2AddContactRole(final String newRole) {
        if(newRole == null) {
            return null;
        }
        if(choices1AddContactRole().contains(newRole)) {
            return "This role already exists - select from the list";
        }
        return null;
    }

    public String validateAddContactRole(final ContactGroup contactGroup, final String existingRole, final String newRole) {
        if((existingRole != null && newRole != null) || (existingRole == null && newRole == null)) {
            return "Specify either an existing role or a new role";
        }
        return null;
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

    @Override
    public int compareTo(final Contact o) {
        return byName.compare(this, o);
    }

    private static final Ordering<Contact> byName =
                    Ordering.natural()
                            .onResultOf(nameOf());

    @Inject
    ContactRoleRepository contactRoleRepository;
    @Inject
    ContactGroupRepository contactGroupRepository;
    @Inject
    ContactNumberRepository contactNumberRepository;



}
