package domainapp.dom.group;

import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.country.Country;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import javax.inject.Inject;
import javax.jdo.annotations.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByCountryAndName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.group.ContactGroup "
                        + "WHERE country == :country && name == :name "),
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.group.ContactGroup "
                        + "WHERE name.matches(:regex) ")
})
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactGroup extends ContactableEntity {

    private Iterable<ContactRole> contactRoles;

    public String title() {
        return getCountry().getName() + (getName() != null ? " (" + getName() + ")" : "");
    }

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Country country;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String address;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Edit Address")
    @MemberOrder(name = "address", sequence = "1")
    public ContactableEntity changeAddress(@ParameterLayout(named = "Address") String address) {
        setAddress(address);
        return this;
    }

    public String default0ChangeAddress() {
        return getAddress();
    }


    @NotPersistent
    public List<ContactRole> getContactRoles() {
        return contactRoleRepository.findByGroup(this);
    }

    @Inject
    ContactRoleRepository contactRoleRepository;
}
