package domainapp.app.rest.v1.group;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.dom.group.ContactGroupRepository;

@DomainService(
        nature = NatureOfService.VIEW_REST_ONLY
)
public class ContactGroupViewModelRepository {

    @Action(
            semantics = SemanticsOf.SAFE,
            typeOf = ContactGroupViewModel.class
    )
    public java.util.List<ContactGroupViewModel> listAll() {
        return Lists.newArrayList(
                Iterables.transform(contactGroupRepository.listAll(),
                        ContactGroupViewModel.create(container))
                );
    }

    @javax.inject.Inject
    ContactGroupRepository contactGroupRepository;

    @javax.inject.Inject
    DomainObjectContainer container;
}
