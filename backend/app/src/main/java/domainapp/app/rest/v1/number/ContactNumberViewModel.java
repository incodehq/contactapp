package domainapp.app.rest.v1.number;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Nature;

import domainapp.dom.number.ContactNumber;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        editing = Editing.DISABLED
)
public class ContactNumberViewModel {

    public static Function<ContactNumber, ContactNumberViewModel> create(final DomainObjectContainer container) {
        return new Function<ContactNumber, ContactNumberViewModel>() {
            @Nullable @Override public ContactNumberViewModel apply(@Nullable final ContactNumber input) {
                return input != null? container.injectServicesInto(new ContactNumberViewModel(input)): null;
            }
        };
    }

    @XmlElement(required = true)
    ContactNumber underlying;

    public ContactNumberViewModel() {
    }

    public ContactNumberViewModel(final ContactNumber underlying) {
        this.underlying = underlying;
    }

    @XmlTransient
    public String getType() {
        return underlying.getType();
    };

    @XmlTransient
    public String getNumber() {
        return underlying.getNumber();
    }

    public String title() {
        return underlying != null? container.titleOf(underlying): "(no underlying)";
    }
    @Override
    public String toString() {
        return underlying != null? underlying.toString(): "(no underlying)";
    }

    @Inject
    DomainObjectContainer container;

}
