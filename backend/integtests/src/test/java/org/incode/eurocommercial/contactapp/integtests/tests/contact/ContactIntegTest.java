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
package org.incode.eurocommercial.contactapp.integtests.tests.contact;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import com.google.common.collect.FluentIterable;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactMenu;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumberType;
import org.incode.eurocommercial.contactapp.dom.role.ContactRole;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactRepository contactRepository;

    @Inject
    ContactMenu contactMenu;

    @Inject
    FakeDataService fakeDataService;

    DemoFixture fs;
    Contact contact;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        contact = fs.getContacts().get(0);
        nextTransaction();

        assertThat(contact).isNotNull();
    }

    public static class Name extends ContactIntegTest {

        @Test
        public void accessible() throws Exception {
            // when
            final String name = wrap(contact).getName();
            // then
            assertThat(name).isNotNull();
        }

    }

    public static class Create extends ContactIntegTest {

        @Test
        public void happy_case() throws Exception {

            // when
            final String name = fakeDataService.name().fullName();
            final String company = fakeDataService.strings().upper(Contact.MaxLength.COMPANY);
            final String officePhoneNumber = randomPhoneNumber();
            final String mobilePhoneNumber = randomPhoneNumber();
            final String homePhoneNumber = randomPhoneNumber();
            final String email = fakeDataService.javaFaker().internet().emailAddress();

            final Contact newContact = wrap(this.contact)
                    .create(name, company, officePhoneNumber, mobilePhoneNumber, homePhoneNumber, email);
            nextTransaction();

            // then
            assertThat(contact).isNotSameAs(newContact);

            assertThat(newContact.getName()).isEqualTo(name);
            assertThat(newContact.getCompany()).isEqualTo(company);
            assertThat(newContact.getEmail()).isEqualTo(email);

            assertThat(newContact.getContactNumbers()).hasSize(3);

            assertContains(newContact.getContactNumbers(), ContactNumberType.OFFICE, officePhoneNumber);
            assertContains(newContact.getContactNumbers(), ContactNumberType.MOBILE, mobilePhoneNumber);
            assertContains(newContact.getContactNumbers(), ContactNumberType.HOME, homePhoneNumber);

            assertThat(newContact.getContactRoles()).isEmpty();

            assertThat(newContact.getNotes()).isNull();
        }

        @Ignore("TODO")
        @Test
        public void name_already_in_use_by_contact() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void name_already_in_use_by_contact_group() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_name_not_provided() throws Exception {

        }

    }

    public static class Edit extends ContactIntegTest {

        @Test
        public void happy_case() throws Exception {

            // when
            final String name = fakeDataService.name().fullName();
            final String company = fakeDataService.strings().upper(Contact.MaxLength.COMPANY);
            final String email = fakeDataService.javaFaker().internet().emailAddress();
            final String notes = fakeDataService.lorem().sentence(3);

            final Contact contact = wrap(this.contact).edit(name, company, email, notes);
            nextTransaction();

            // then
            assertThat(contact).isSameAs(this.contact);

            assertThat(contact.getName()).isEqualTo(name);
            assertThat(contact.getCompany()).isEqualTo(company);
            assertThat(contact.getEmail()).isEqualTo(email);
            assertThat(contact.getNotes()).isEqualTo(notes);
        }

        @Ignore("TODO")
        @Test
        public void name_already_in_use_by_contact() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void name_already_in_use_by_contact_group() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_name_not_provided() throws Exception {

        }

    }

    public static class Delete extends ContactIntegTest {

        @Test
        public void happy_case() throws Exception {

            // given
            final List<Contact> contacts = contactRepository.listAll();

            final int sizeBefore = contacts.size();
            assertThat(sizeBefore).isGreaterThan(0);

            final Contact someContact = fakeDataService.collections().anyOf(contacts);
            final String someContactName = someContact.getName();
            nextTransaction();


            // when
            someContact.delete();
            nextTransaction();


            // then
            final List<Contact> contactsAfter = contactRepository.listAll();

            final int sizeAfter = contactsAfter.size();
            assertThat(sizeAfter).isEqualTo(sizeBefore-1);

            assertThat(FluentIterable.from(contactsAfter).filter(
                    contact -> {
                        return Objects.equals(contact.getName(), someContactName);
                    }
            )).isEmpty();

        }

    }

    public static class AddNumber extends ContactIntegTest {


        String officePhoneNumber;

        @Before
        public void setUp() {
            // deliberately does not call super.setUp()

            // given
            final String name = fakeDataService.name().fullName();

            this.officePhoneNumber = randomPhoneNumber();

            this.contact = wrap(contactMenu).create(name, null, officePhoneNumber, null, null, null);
            nextTransaction();

        }

        @Test
        public void add_number_with_existing_type() throws Exception {

            // given
            assertContains(contact.getContactNumbers(), ContactNumberType.OFFICE, officePhoneNumber);
            assertThat(contact.getContactNumbers()).hasSize(1);

            // when
            String newOfficePhoneNumber = randomPhoneNumber();
            wrap(contact).addContactNumber(newOfficePhoneNumber, ContactNumberType.OFFICE.title(), null);
            nextTransaction();

            // then
            assertThat(contact.getContactNumbers()).hasSize(2);
            assertContains(contact.getContactNumbers(), ContactNumberType.OFFICE, newOfficePhoneNumber);
            assertContains(contact.getContactNumbers(), ContactNumberType.OFFICE, this.officePhoneNumber);

        }

        @Ignore("TODO")
        @Test
        public void add_number_with_new_type() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void add_number_when_already_have_number_of_any_type() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_no_type_specified() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_both_existing_type_and_new_type_specified() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_no_number_provided() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void invalid_number_format() throws Exception {

        }

    }

    public static class RemoveNumber extends ContactIntegTest {

        String officePhoneNumber;
        String homePhoneNumber;

        @Before
        public void setUp() {
            // deliberately does not call super.setUp()

            // given
            final String name = fakeDataService.name().fullName();

            this.officePhoneNumber = randomPhoneNumber();
            this.homePhoneNumber = randomPhoneNumber();

            this.contact = wrap(contactMenu).create(name, null, officePhoneNumber, null, homePhoneNumber, null);
            nextTransaction();
        }

        @Test
        public void remove_number() throws Exception {

            // given
            assertThat(contact.getContactNumbers()).hasSize(2);
            final String existingNumber = fakeDataService.collections().anyOf(contact.choices0RemoveContactNumber());
            nextTransaction();

            // when
            wrap(contact).removeContactNumber(existingNumber);
            nextTransaction();

            // then
            assertNotContains(contact.getContactNumbers(), existingNumber);
        }

        @Ignore("TODO")
        @Test
        public void remove_number_when_none_exists() throws Exception {

        }
    }

    public static class AddRole extends ContactIntegTest {

        @Test
        public void happy_case_using_existing_role_name() throws Exception {

            // given
            final int numRolesBefore = contact.getContactRoles().size();

            // when
            final ContactGroup contactGroup = fakeDataService.collections().anyOf(this.contact.choices0AddContactRole());
            final String existingRole = fakeDataService.collections().anyOf(this.contact.choices1AddContactRole());

            final Contact contact = wrap(this.contact).addContactRole(contactGroup, existingRole, null);
            nextTransaction();

            // then
            assertThat(contact.getContactRoles()).hasSize(numRolesBefore+1);
            assertThat(contact.getContactRoles())
                    .extracting(
                            ContactRole::getContactGroup,
                            ContactRole::getRoleName,
                            ContactRole::getContact)
                    .contains(
                        Tuple.tuple(
                                contactGroup,
                                existingRole,
                                this.contact));
        }

        @Ignore("TODO")
        @Test
        public void happy_case_using_new_role_name() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void happy_case_using_new_role_name_which_also_in_list() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_no_group_specified() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_no_role_specified() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_both_existing_role_and_new_role_specified() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void possible_groups_should_not_include_any_for_which_contact_already_has_a_role() throws Exception {

        }
    }

    public static class RemoveRole extends ContactIntegTest {

        @Test
        public void remove_role() throws Exception {

            // given
            final int contactRolesBefore = contact.getContactRoles().size();
            assertThat(contactRolesBefore).isGreaterThan(0);

            // when
            final ContactGroup contactGroup = fakeDataService.collections().anyOf(this.contact.choices0RemoveContactRole());
            wrap(this.contact).removeContactRole(contactGroup);
            nextTransaction();

            // then
            assertThat(contact.getContactRoles()).hasSize(contactRolesBefore-1);

        }

        @Ignore("TODO")
        @Test
        public void remove_role_when_none_exists() throws Exception {

        }
    }

}