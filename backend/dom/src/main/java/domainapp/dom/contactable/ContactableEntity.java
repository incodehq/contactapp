package domainapp.dom.contactable;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Function;
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
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import domainapp.dom.number.ContactNumber;
import domainapp.dom.number.ContactNumberRepository;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Discriminator(
        strategy = DiscriminatorStrategy.CLASS_NAME,
        column = "discriminator")
@Queries({
})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@MemberGroupLayout(
        columnSpans={6,0,0,6}
)

@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactableEntity  {

    public String title() {
        return getName();
    }

    @MemberOrder(sequence = "1.2")
    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String name;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String email;

    @Column(allowsNull = "true")
    @Property
    @PropertyLayout(multiLine = 6, hidden = Where.ALL_TABLES)
    @Getter @Setter
    private String notes;

    @Persistent(mappedBy = "owner", dependentElement = "true")
    @Collection()
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<ContactNumber> contactNumbers = new TreeSet<ContactNumber>();

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactNumbers", sequence = "1")
    public ContactableEntity addContactNumber(String type, String number) {
        contactNumberRepository.findOrCreate(this, type, number);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Remove")
    @MemberOrder(name = "contactNumbers", sequence = "2")
    public ContactableEntity removeContactNumber(String type) {
        final Optional<ContactNumber> contactNumberIfAny = Iterables
                .tryFind(getContactNumbers(), cn -> Objects.equal(cn.getType(), type));

        if(contactNumberIfAny.isPresent()) {
            getContactNumbers().remove(contactNumberIfAny.get());
        }
        return this;
    }

    public String default0RemoveContactNumber() {
        return getContactNumbers().size() == 1? getContactNumbers().iterator().next().getType(): null;
    }
    public List<String> choices0RemoveContactNumber() {
        return Lists.transform(Lists.newArrayList(getContactNumbers()), ContactNumber::getType);
    }

    public String disableRemoveContactNumber() {
        return getContactNumbers().isEmpty()? "No contact numbers to remove": null;
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "name");
    }

    @Inject
    ContactNumberRepository contactNumberRepository;

    public static <T extends ContactableEntity> Function<T, String> nameOf() {
        return new Function<T, String>() {
            @Nullable @Override
            public String apply(final T contactGroup) {
                return contactGroup.getName();
            }
        };
    }
}
