package domainapp.app.rest.v1.contacts;

import java.util.List;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.dom.contacts.ContactRepository;
import domainapp.dom.group.ContactGroupRepository;

@DomainService(
        nature = NatureOfService.VIEW_REST_ONLY
)
public class ContactableViewModelRepository {

    @Action(
            semantics = SemanticsOf.SAFE,
            typeOf = ContactableViewModel.class
    )
    public java.util.List<ContactableViewModel> listAll() {
        final List<ContactableViewModel> contactable = Lists.newArrayList();
        contactable.addAll(
                FluentIterable
                        .from(contactGroupRepository.listAll())
                        .transform(ContactableViewModel.createForGroup(container))
                        .toList());
        contactable.addAll(
                FluentIterable
                        .from(contactRepository.listAll())
                        .transform(ContactableViewModel.createForContact(container))
                        .toList());
        return contactable;
    }

    @javax.inject.Inject
    ContactGroupRepository contactGroupRepository;

    @javax.inject.Inject
    ContactRepository contactRepository;

    @javax.inject.Inject
    DomainObjectContainer container;
}
