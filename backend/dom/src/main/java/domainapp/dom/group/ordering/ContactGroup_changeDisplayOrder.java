package domainapp.dom.group.ordering;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.dom.group.ContactGroup;

@Mixin
public class ContactGroup_changeDisplayOrder {

    private final ContactGroup contactGroup;

    public ContactGroup_changeDisplayOrder(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(
            named = "Change"
    )
    @MemberOrder(name = "displayOrder", sequence = "1")
    public ContactGroupOrderingViewModel $$() {
        final ContactGroupOrderingViewModel viewModel = new ContactGroupOrderingViewModel();
        viewModel.setContactGroup(contactGroup);
        return viewModel;
    }

}
