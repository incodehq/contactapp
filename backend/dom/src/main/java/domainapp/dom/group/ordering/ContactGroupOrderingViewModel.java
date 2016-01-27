package domainapp.dom.group.ordering;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import lombok.Getter;
import lombok.Setter;

@javax.xml.bind.annotation.XmlRootElement(name = "contactGroupOrderingViewModel")
@javax.xml.bind.annotation.XmlType(
        propOrder = {                                               // <2>
                "contactGroup"
        }
)
@javax.xml.bind.annotation.XmlAccessorType(XmlAccessType.FIELD)
@DomainObjectLayout(
        titleUiEvent = org.apache.isis.applib.services.eventbus.TitleUiEvent.Default.class
)
public class ContactGroupOrderingViewModel implements org.apache.isis.applib.services.dto.Dto {

    private static final Ordering<ContactGroup> displayNumberThenNatural =
            Ordering.natural()
                    .nullsLast()
                    .onResultOf(ContactGroup.Functions.displayNumberOf())
                    .compound(Ordering.natural());

    public ContactGroupOrderingViewModel() {
    }

    public ContactGroupOrderingViewModel(final ContactGroup contactGroup) {
        setContactGroup(contactGroup);
    }

    @Title
    @XmlElement
    @Getter @Setter
    private ContactGroup contactGroup;


    @XmlTransient
    private Integer displayOrder;

    public Integer getDisplayOrder() {
        return contactGroup.getDisplayOrder();
    }



    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(
            cssClassFa = "arrow-up"
    )
    @MemberOrder(name = "displayOrder", sequence = "1")
    public ContactGroupOrderingViewModel moveUp() {
        reorder(orderedContactGroups());
        spreadOut(orderedContactGroups(), 10);
        updateCurrent(-15, Integer.MAX_VALUE);
        reorder(orderedContactGroups());
        return this;
    }


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(
            cssClassFa = "arrow-down"
    )
    @MemberOrder(name = "displayOrder", sequence = "2")
    public ContactGroupOrderingViewModel moveDown() {
        reorder(orderedContactGroups());
        spreadOut(orderedContactGroups(), 10);
        updateCurrent(+15, Integer.MIN_VALUE);
        reorder(orderedContactGroups());
        return this;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(
            cssClassFa = "ban"
    )
    @MemberOrder(name = "displayOrder", sequence = "3")
    public ContactGroupOrderingViewModel clear() {
        contactGroup.setDisplayOrder(null);
        reorder(orderedContactGroups());
        return this;
    }


    private void spreadOut(final List<ContactGroup> contactGroupsBefore, final int factor) {
        for (ContactGroup contactGroup : contactGroupsBefore) {
            final Integer displayOrder = contactGroup.getDisplayOrder();
            if(displayOrder != null) {
                contactGroup.setDisplayOrder(displayOrder * factor);
            }
        }
    }
    private void updateCurrent(final int adjust, final int fallback) {
        final Integer currDisplayOrder = contactGroup.getDisplayOrder();
        contactGroup.setDisplayOrder(
                currDisplayOrder != null
                        ? currDisplayOrder + adjust
                        : fallback);
    }

    private void reorder(final List<ContactGroup> contactGroupsAfter) {
        int num = 0;
        for (ContactGroup contactGroup : contactGroupsAfter) {
            if(contactGroup.getDisplayOrder() != null) {
                contactGroup.setDisplayOrder(++num);
            }
        }
    }

    @Collection(
            notPersisted = true // so that Apache Isis ignores when remapping
    )
    @CollectionLayout(
            defaultView = "table",
            paged = 100
    )
    @XmlTransient // so that JAXB ignores when recreating
    public List<ContactGroupOrderingViewModel> getContactGroups() {
        final List<ContactGroup> contactGroups = orderedContactGroups();
        return Lists.newArrayList(
                    FluentIterable.from(contactGroups)
                                  .transform(toViewModel())
                 );
    }

    private List<ContactGroup> orderedContactGroups() {
        final List<ContactGroup> contactGroups = repository.listAll();
        Collections.sort(contactGroups, displayNumberThenNatural);
        return contactGroups;
    }

    private Function<ContactGroup, ContactGroupOrderingViewModel> toViewModel() {
        return new Function<ContactGroup, ContactGroupOrderingViewModel>() {
            @Nullable @Override public ContactGroupOrderingViewModel apply(@Nullable final ContactGroup contactGroup) {
                final ContactGroupOrderingViewModel vm = ContactGroupOrderingViewModel.this;
                return contactGroup == vm.contactGroup
                        ? vm // can't have two view models both representing the same contact group at same time
                        : new ContactGroupOrderingViewModel(contactGroup);
            }
        };
    }


    @XmlTransient
    @Inject
    ContactGroupRepository repository;

    static class Functions {
        private Functions(){}

    }
}
