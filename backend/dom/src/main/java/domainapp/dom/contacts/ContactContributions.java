package domainapp.dom.contacts;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.role.ContactRole;
import org.apache.isis.applib.annotation.*;

import javax.inject.Inject;
import java.util.List;



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
        return contactRepository.findByContactRoleName(contactRole.getRoleName());
    }

    @Inject
    ContactRepository contactRepository;
}
