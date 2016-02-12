package org.incode.eurocommercial.contactapp.dom.group.ordering;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.ViewModel;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService;

import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;
import lombok.Getter;
import lombok.Setter;

//@javax.xml.bind.annotation.XmlRootElement(name = "contactGroupOrderingViewModel")
//@javax.xml.bind.annotation.XmlType(
//        propOrder = {
//                "contactGroup"
//        }
//)
//@javax.xml.bind.annotation.XmlAccessorType(XmlAccessType.FIELD)
@MemberGroupLayout(
        columnSpans = {6,0,0,6},
        left = {"Group", "Ordering"}
)
@DomainObjectLayout(
        cssClassFa = "sort-alpha-asc"
)
public class ContactGroupOrderingViewModel implements ViewModel {

    @Inject
    BookmarkService bookmarkService;
    @Inject
    DomainObjectContainer container;

    @Override
    public String viewModelMemento() {
        final ContactGroup contactGroup = getContactGroup();
        final Bookmark bookmark = bookmarkService.bookmarkFor(contactGroup);
        return bookmark.getIdentifier();
    }

    @Override
    public void viewModelInit(final String memento) {
        final Bookmark bookmark = bookmarkService.bookmarkFor(ContactGroup.class, memento);
        this.contactGroup = (ContactGroup) bookmarkService.lookup(bookmark);
    }


    public ContactGroupOrderingViewModel() {
    }

    public ContactGroupOrderingViewModel(final ContactGroup contactGroup) {
        setContactGroup(contactGroup);
    }

    //@XmlElement
    @Property
    @PropertyLayout(
            hidden = Where.PARENTED_TABLES
    )
    @Getter @Setter
    @MemberOrder(name="Group", sequence = "1")
    private ContactGroup contactGroup;


    // @XmlTransient
    private Integer displayOrder;

    @Property
    @PropertyLayout()
    @MemberOrder(name = "Ordering", sequence = "2")
    public Integer getDisplayOrder() {
        return contactGroup.getDisplayOrder();
    }



    @Title
    @Property
    @PropertyLayout(
            hidden = Where.OBJECT_FORMS
    )
    @MemberOrder(name="Ordering", sequence = "3")
    public String getName() {
        return container.titleOf(contactGroup);
    }




    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(
            cssClassFa = "arrow-up"
    )
    @MemberOrder(name = "displayOrder", sequence = "1")
    public ContactGroupOrderingViewModel moveUp() {
        reorder(repository.listAll());
        spreadOut(repository.listAll(), 10);
        updateCurrent(-15, Integer.MAX_VALUE);
        reorder(repository.listAll());
        return this;
    }


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(
            cssClassFa = "arrow-down"
    )
    @MemberOrder(name = "displayOrder", sequence = "2")
    public ContactGroupOrderingViewModel moveDown() {
        reorder(repository.listAll());
        spreadOut(repository.listAll(), 10);
        updateCurrent(+15, Integer.MIN_VALUE);
        reorder(repository.listAll());
        return this;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(
            cssClassFa = "ban"
    )
    @MemberOrder(name = "displayOrder", sequence = "3")
    public ContactGroupOrderingViewModel clear() {
        contactGroup.setDisplayOrder(null);
        reorder(repository.listAll());
        return this;
    }


    private void spreadOut(final List<ContactGroup> contactGroupsBefore, final int factor) {
        for (ContactGroup contactGroup : contactGroupsBefore) {
            final Integer displayOrder = contactGroup.getDisplayOrder();
            if(displayOrder != null) {
                contactGroup.setDisplayOrder(displayOrder * factor);
            } else {
                return;
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
            } else {
                return;
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
    public List<ContactGroupOrderingViewModel> getContactGroups() {
        final List<ContactGroup> contactGroups = repository.listAll();
        return Lists.newArrayList(
                    FluentIterable.from(contactGroups)
                                  .transform(toViewModel())
                 );
    }

    private Function<ContactGroup, ContactGroupOrderingViewModel> toViewModel() {
        return new Function<ContactGroup, ContactGroupOrderingViewModel>() {
            @Nullable @Override public ContactGroupOrderingViewModel apply(@Nullable final ContactGroup contactGroup) {
                final ContactGroupOrderingViewModel vm = ContactGroupOrderingViewModel.this;
                return contactGroup == vm.contactGroup
                        ? vm // can't have two view models both representing the same contact group at same time
                        : container.injectServicesInto(new ContactGroupOrderingViewModel(contactGroup));
            }
        };
    }


    // @XmlTransient
    @Inject
    ContactGroupRepository repository;

    static class Functions {
        private Functions(){}

    }
}
