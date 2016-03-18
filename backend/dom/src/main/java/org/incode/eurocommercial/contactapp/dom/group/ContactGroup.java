/*
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.eurocommercial.contactapp.dom.group;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import org.incode.eurocommercial.contactapp.dom.contactable.ContactableEntity;
import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.country.Country;
import org.incode.eurocommercial.contactapp.dom.role.ContactRole;
import org.incode.eurocommercial.contactapp.dom.role.ContactRoleRepository;
import org.incode.eurocommercial.contactapp.dom.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByCountry", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.group.ContactGroup "
                        + "WHERE country == :country "),
        @Query(
                name = "findByCountryAndName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.group.ContactGroup "
                        + "WHERE country == :country && name == :name "),
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.contactapp.dom.group.ContactGroup "
                        + "WHERE name.matches(:regex) "),
})
@Unique(name = "ContactGroup_displayNumber_UNQ", members = { "displayOrder" })
@DomainObject(
        editing = Editing.DISABLED
)
@MemberGroupLayout(
        columnSpans = { 6, 0, 0, 6 },
        left = { "General", "Other" }
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactGroup extends ContactableEntity implements Comparable<ContactGroup> {

    public static class MaxLength {
        private MaxLength() {
        }

        public static final int ADDRESS = 255;
    }

    public String title() {
        return getName() + " (" + getCountry().getName() + ")";
    }

    @MemberOrder(name = "Other", sequence = "1")
    @Column(allowsNull = "true")
    @Property()
    @PropertyLayout(hidden = Where.ALL_TABLES)
    @Getter @Setter
    private Integer displayOrder;

    @MemberOrder(sequence = "1.5") // after #name, before #address
    @javax.jdo.annotations.Persistent(defaultFetchGroup = "true") // eager load
    @Column(allowsNull = "false")
    @Property()
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private Country country;

    @Column(allowsNull = "true", length = MaxLength.ADDRESS)
    @Property
    @PropertyLayout(multiLine = 3)
    @Getter @Setter
    private String address;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Create", position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "Notes", sequence = "1")
    public ContactGroup create(
            final Country country,
            @Parameter(maxLength = ContactableEntity.MaxLength.NAME)
            final String name) {
        return contactGroupRepository.findOrCreate(country, name);
    }

    public Country default0Create() {
        return getCountry();
    }

    public String validateCreate(
            final Country country,
            final String name) {
        if (!contactGroupRepository.findByName(name).isEmpty()) {
            return "This name is already in use by another contact group";
        } else {
            return contactRepository.findByName(name).isEmpty() ? null : "This name is already in use by a contact";
        }
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "Notes", sequence = "2")
    public ContactableEntity edit(
            @Parameter(maxLength = ContactableEntity.MaxLength.NAME)
            final String name,
            @Parameter(maxLength = MaxLength.ADDRESS, optionality = Optionality.OPTIONAL)
            @ParameterLayout(multiLine = 3)
            final String address,
            @Parameter(maxLength = ContactableEntity.MaxLength.EMAIL, optionality = Optionality.OPTIONAL)
            final String email,
            @Parameter(maxLength = ContactableEntity.MaxLength.NOTES, optionality = Optionality.OPTIONAL)
            @ParameterLayout(multiLine = 6)
            final String notes) {
        setName(name);
        setAddress(address);
        setEmail(email);
        setNotes(notes);
        return this;
    }

    public String default0Edit() {
        return getName();
    }

    public String default1Edit() {
        return getAddress();
    }

    public String default2Edit() {
        return getEmail();
    }

    public String default3Edit() {
        return getNotes();
    }

    public String validateEdit(
            final String name,
            final String address,
            final String email,
            final String notes) {
        if (!contactGroupRepository.findByName(name).isEmpty()) {
            return "This name is already in use by another contact group";
        } else {
            return contactRepository.findByName(name).isEmpty() ? null : "This name is already in use by a contact";
        }
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(named = "Delete", position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "Notes", sequence = "3")
    public void delete() {
        contactGroupRepository.delete(this);
    }

    public String disableDelete() {
        return getContactRoles().isEmpty() ? null : "This group has contacts";
    }

    @NotPersistent
    @Collection
    @CollectionLayout(named = "Role of Contacts in Group", defaultView = "table")
    public List<ContactRole> getContactRoles() {
        List<ContactRole> contactRoles =  contactRoleRepository.findByGroup(this);
        Collections.sort(contactRoles);
        return contactRoles;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactRoles", sequence = "1")
    public ContactGroup addContactRole(
            @Parameter(optionality = Optionality.MANDATORY)
            Contact contact,
            @Parameter(maxLength = ContactRole.MaxLength.NAME, optionality = Optionality.OPTIONAL)
            String role,
            @Parameter(maxLength = ContactRole.MaxLength.NAME, optionality = Optionality.OPTIONAL)
            String newRole) {
        final String roleName = StringUtil.firstNonEmpty(newRole, role);
        contactRoleRepository.findOrCreate(contact, this, roleName);
        return this;
    }

    public List<Contact> choices0AddContactRole() {
        final List<Contact> contacts = contactRepository.listAll();
        final List<Contact> currentContacts =
                FluentIterable
                        .from(getContactRoles())
                        .transform(ContactRole::getContact)
                        .toList();
        contacts.removeAll(currentContacts);
        return contacts;
    }

    public SortedSet<String> choices1AddContactRole() {
        return contactRoleRepository.roleNames();
    }

    public String validateAddContactRole(final Contact contact, final String role, final String newRole) {
        return StringUtil.eitherOr(role, newRole, "role");
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Remove")
    @MemberOrder(name = "contactRoles", sequence = "2")
    public ContactGroup removeContactRole(final Contact contact) {
        final Optional<ContactRole> contactRoleIfAny = Iterables
                .tryFind(getContactRoles(), cn -> Objects.equal(cn.getContact(), contact));

        if (contactRoleIfAny.isPresent()) {
            getContactRoles().remove(contactRoleIfAny.get());
        }
        return this;
    }

    public Contact default0RemoveContactRole() {
        return getContactRoles().size() == 1 ? getContactRoles().iterator().next().getContact() : null;
    }

    public List<Contact> choices0RemoveContactRole() {
        return Lists.transform(Lists.newArrayList(getContactRoles()), ContactRole::getContact);
    }

    public String disableRemoveContactRole() {
        return getContactRoles().isEmpty() ? "No contacts to remove" : null;
    }

    private static final Ordering<ContactGroup> byDisplayNumberThenName =
            Ordering
                    .natural()
                    .nullsLast()
                    .onResultOf(displayNumberOf())
                    .compound(Ordering
                            .natural()
                            .onResultOf(nameOf())
                    );

    private static Function<ContactGroup, Integer> displayNumberOf() {
        return new Function<ContactGroup, Integer>() {
            @Nullable @Override
            public Integer apply(@Nullable final ContactGroup contactGroup) {
                return contactGroup.getDisplayOrder();
            }
        };
    }

    @Override
    public int compareTo(final ContactGroup other) {
        return byDisplayNumberThenName.compare(this, other);
    }

    @Inject
    ContactRoleRepository contactRoleRepository;
    @Inject
    ContactRepository contactRepository;
    @javax.inject.Inject
    ContactGroupRepository contactGroupRepository;

}
