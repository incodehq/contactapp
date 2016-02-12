package org.incode.eurocommercial.contactapp.app.rest.v1.number;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;

import org.apache.isis.applib.DomainObjectContainer;

import org.incode.eurocommercial.contactapp.app.rest.ViewModelWithUnderlying;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumber;

public class ContactNumberViewModel extends ViewModelWithUnderlying<ContactNumber> {

    public static Function<ContactNumber, ContactNumberViewModel> create(final DomainObjectContainer container) {
        return new Function<ContactNumber, ContactNumberViewModel>() {
            @Nullable @Override public ContactNumberViewModel apply(@Nullable final ContactNumber input) {
                return input != null? container.injectServicesInto(new ContactNumberViewModel(input)): null;
            }
        };
    }

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

}
