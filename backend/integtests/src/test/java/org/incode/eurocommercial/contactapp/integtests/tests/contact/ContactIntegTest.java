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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactRepository contactRepository;

    @Inject
    FakeDataService fakeDataService;

    DemoFixture fs;
    Contact contactPojo;
    Contact contactWrapped;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        contactPojo = fs.getContacts().get(0);

        assertThat(contactPojo).isNotNull();
        contactWrapped = wrap(contactPojo);
    }

    public static class Name extends ContactIntegTest {

        @Test
        public void accessible() throws Exception {
            // when
            final String name = contactWrapped.getName();
            // then
            assertThat(name).isNotNull();
        }

    }

    public static class Create extends ContactIntegTest {

        @Ignore("TODO")
        @Test
        public void happy_case() throws Exception {

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

        @Ignore("TODO")
        @Test
        public void happy_case() throws Exception {

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

            // when
            someContact.delete();

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

        @Ignore("TODO")
        @Test
        public void add_number_with_existing_type() throws Exception {

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

        @Ignore("TODO")
        @Test
        public void remove_number() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void remove_number_when_none_exists() throws Exception {

        }
    }

    public static class AddRole extends ContactIntegTest {

        @Ignore("TODO")
        @Test
        public void happy_case_using_existing_role_name() throws Exception {

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

        @Ignore("TODO")
        @Test
        public void remove_role() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void remove_role_when_none_exists() throws Exception {

        }
    }

}