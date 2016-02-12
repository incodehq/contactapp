package org.incode.eurocommercial.contactapp.dom.group.ordering;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;

@Mixin
public class ContactGroup_fixDisplayOrder {

    private final ContactGroup contactGroup;

    public ContactGroup_fixDisplayOrder(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(
            named = "Fix",
            cssClassFa = "sort-alpha-asc"
    )
    @MemberOrder(name = "displayOrder", sequence = "1")
    public ContactGroupOrderingViewModel $$() {
        return container.injectServicesInto(new ContactGroupOrderingViewModel(contactGroup));
    }

    @Inject
    DomainObjectContainer container;

}
