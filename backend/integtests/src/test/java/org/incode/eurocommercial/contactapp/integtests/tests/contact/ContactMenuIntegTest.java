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

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactMenu;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactMenuIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    ContactMenu contactMenu;

    public static class Find extends ContactMenuIntegTest {

        @Ignore("TODO")
        @Test
        public void match_on_name() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void match_on_company() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void match_on_email() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void no_match_on_any() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void no_query_string_provided() throws Exception {

        }
    }

    public static class FindByGroup extends ContactMenuIntegTest {

        @Ignore("TODO")
        @Test
        public void matches() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void no_match() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void no_group_specified() throws Exception {

        }

    }

    public static class FindByRole extends ContactMenuIntegTest {

        @Ignore("TODO")
        @Test
        public void matches() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void no_match() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void no_role_specified() throws Exception {

        }

    }


    public static class ListAll extends ContactMenuIntegTest {

        @Test
        public void happy_case() throws Exception {

            // given
            DemoFixture fs = new DemoFixture();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            final List<Contact> returned = wrap(contactMenu).listAll();

            // then
            final List<Contact> contacts = fs.getContacts();
            assertThat(returned).hasSize(contacts.size());

            assertThat(returned).containsAll(fs.getContacts());
            assertThat(fs.getContacts()).containsAll(returned);
        }

    }

    public static class Create extends ContactMenuIntegTest {

        @Ignore("TODO")
        @Test
        public void happy_case_with_minimal_info_provided() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void happy_case_with_all_info_provided() throws Exception {

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
        public void no_name_specified() throws Exception {

        }

    }

}