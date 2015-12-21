package domainapp.app.rest.v1.contacts;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.dom.contacts.ContactRepository;

@DomainService(
        nature = NatureOfService.VIEW_REST_ONLY
)
public class ContactViewModelRepository {

    @Action(
            semantics = SemanticsOf.SAFE,
            typeOf = ContactViewModel.class
    )
    public java.util.List<ContactViewModel> listAll() {
        return Lists.newArrayList(
                Iterables.transform(contactRepository.listAll(),
                        ContactViewModel.create(container))
                );
    }

    @javax.inject.Inject
    ContactRepository contactRepository;

    @javax.inject.Inject
    DomainObjectContainer container;
}
