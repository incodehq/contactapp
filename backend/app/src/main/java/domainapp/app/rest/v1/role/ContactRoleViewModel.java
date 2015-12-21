package domainapp.app.rest.v1.role;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.app.rest.v1.group.ContactGroupViewModel;
import domainapp.dom.role.ContactRole;

@XmlRootElement(name = "contact-role")
public class ContactRoleViewModel {

    public static Function<ContactRole, ContactRoleViewModel> create(final DomainObjectContainer container) {
        return new Function<ContactRole, ContactRoleViewModel>() {
            @Nullable @Override public ContactRoleViewModel apply(@Nullable final ContactRole input) {
                return input != null? container.injectServicesInto(new ContactRoleViewModel(input)): null;
            }
        };
    }

    @XmlElement(required = true)
    private ContactRole underlying;

    public ContactRoleViewModel() {
    }

    public ContactRoleViewModel(final ContactRole underlying) {
        this.underlying = underlying;
    }

    @XmlTransient
    public ContactGroupViewModel getContactGroup() {
        return ContactGroupViewModel.create(container).apply(underlying.getContactGroup());
    }

    @XmlTransient
    public String getRoleName() {
        return underlying.getRoleName();
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
