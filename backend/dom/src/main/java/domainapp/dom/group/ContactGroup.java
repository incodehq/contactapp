package domainapp.dom.group;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Function;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.country.Country;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;

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
                        + "WHERE name.matches(:regex) "),
})
@Unique(name = "ContactGroup_displayNumber_UNQ", members = {"displayOrder"})
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


    @MemberOrder(sequence = "1.1")
    @Column(allowsNull = "true")
    @Property()
    @Getter @Setter
    private Integer displayOrder;

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

    public static class Functions {
        private Functions(){}

        public static Function<ContactGroup, Integer> displayNumberOf() {
            return new Function<ContactGroup, Integer>() {
                @Nullable @Override
                public Integer apply(@Nullable final ContactGroup contactGroup) {
                    return contactGroup.getDisplayOrder();
                }
            };
        }

    }

}
