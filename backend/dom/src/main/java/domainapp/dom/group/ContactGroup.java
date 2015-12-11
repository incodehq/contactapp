package domainapp.dom.group;

import javax.jdo.annotations.*;

import domainapp.dom.contactable.ContactableEntity;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import domainapp.dom.country.Country;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByCountryAndName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.group.ContactGroup "
                        + "WHERE country == :country && name == :name ")
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
