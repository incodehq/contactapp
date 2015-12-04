package domainapp.dom.contacts;

import java.util.List;

import javax.inject.Inject;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.role.ContactRole;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;



@DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class ContactContributions {

    @CollectionLayout(render = RenderType.EAGERLY)
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public List<Contact> contacts(ContactGroup contactGroup){
        return contactRepository.findByContactGroup(contactGroup);
    }

    @CollectionLayout(render = RenderType.EAGERLY)
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public List<Contact> contactsWithTheSameRole(ContactRole contactRole){
        return contactRepository.findByContactRole(contactRole);
    }

    @Inject
    ContactRepository contactRepository;
}
