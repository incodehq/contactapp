package domainapp.dom.country;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.country.Country "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
@Unique(name = "Country_name_UNQ", members = { "name" })
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@MemberGroupLayout(
        columnSpans={4,0,0,12}
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class Country implements Comparable<Country> {

    public String title() {
        return getName();
    }

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private String name;

    @Persistent(mappedBy = "country", dependentElement = "true")
    @Collection()
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<ContactGroup> contactGroups = new TreeSet<ContactGroup>();

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactGroups", sequence = "1")
    public Country addContactGroup(String name) {
        contactGroupRepository.findOrCreate(this, name);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Remove")
    @MemberOrder(name = "contactGroups", sequence = "2")
    public Country removeContactGroup(String name) {
        final Optional<ContactGroup> contactGroupIfAny = Iterables
                .tryFind(getContactGroups(), cg -> Objects.equal(cg.getName(), name));

        if(contactGroupIfAny.isPresent()) {
            getContactGroups().remove(contactGroupIfAny.get());
        }
        return this;
    }

    public String default0RemoveContactGroup() {
        return getContactGroups().size() == 1? getContactGroups().iterator().next().getName(): null;
    }
    public List<String> choices0RemoveContactGroup() {
        return Lists.transform(Lists.newArrayList(getContactGroups()), ContactGroup::getName);
    }
    public String disableRemoveContactGroup() {
        return getContactGroups().isEmpty()? "No contact groups to remove": null;
    }



    //region > compareTo, toString
    @Override
    public int compareTo(final Country other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "name");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "name");
    }
    //endregion


    @Inject
    ContactGroupRepository contactGroupRepository;

}
