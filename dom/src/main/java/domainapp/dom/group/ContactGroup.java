package domainapp.dom.group;

import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.country.Country;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import javax.jdo.annotations.*;

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
public class ContactGroup extends ContactableEntity {

    public String title() {
        return getCountry().getName() + (getName() != null ? " (" + getName() + ")" : "");
    }

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Country country;

}
