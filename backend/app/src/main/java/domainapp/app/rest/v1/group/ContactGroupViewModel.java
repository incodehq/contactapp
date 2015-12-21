package domainapp.app.rest.v1.group;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.app.rest.v1.contactable.ContactableViewModel;
import domainapp.app.rest.v1.country.CountryViewModel;
import domainapp.app.rest.v1.number.ContactNumberViewModel;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.number.ContactNumber;

@XmlRootElement(name = "contact-group")
public class ContactGroupViewModel implements ContactableViewModel {

    public static Function<ContactGroup, ContactGroupViewModel> create(final DomainObjectContainer container) {
        return new Function<ContactGroup, ContactGroupViewModel>() {
            @Nullable @Override public ContactGroupViewModel apply(@Nullable final ContactGroup input) {
                return input != null? container.injectServicesInto(new ContactGroupViewModel(input)): null;
            }
        };
    }

    @XmlElement(required = true)
    private ContactGroup underlying;

    public ContactGroupViewModel() {
    }

    public ContactGroupViewModel(ContactGroup underlying) {
        this.underlying = underlying;
    }

    public CountryViewModel getCountry() {
        return CountryViewModel.create(container).apply(underlying.getCountry());
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

    @XmlTransient
    @Override
    public String getName() {
        return underlying.getName();
    }

    @XmlTransient
    @Override
    public String getEmail() {
        return underlying.getEmail();
    }

    @XmlTransient
    @Override
    public String getNotes() {
        return underlying.getNotes();
    }

    @XmlTransient
    @Override
    public List<ContactNumberViewModel> getContactNumbers() {
        return Lists.newArrayList(
                Iterables.transform(underlying.getContactNumbers(), new Function<ContactNumber, ContactNumberViewModel>() {
                    @Nullable @Override public ContactNumberViewModel apply(@Nullable final ContactNumber input) {
                        return new ContactNumberViewModel(input);
                    }
                })
        );
    }

}
