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
package org.incode.eurocommercial.contactapp.integtests.tests.group;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

public class ContactGroupIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactRepository contactRepository;

    @Inject
    ContactGroupRepository contactGroupRepository;

    @Inject
    FakeDataService fakeDataService;

    DemoFixture fs;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

    }



    public static class Create extends ContactGroupIntegTest {

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

    public static class Edit extends ContactGroupIntegTest {

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

    public static class Delete extends ContactGroupIntegTest {

        @Ignore("TODO")
        @Test
        public void happy_case() throws Exception {

        }

        @Test
        public void cannot_delete_when_has_contact_roles() throws Exception {

        }

    }

    public static class AddNumber extends ContactGroupIntegTest {

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

    public static class RemoveNumber extends ContactGroupIntegTest {

        @Ignore("TODO")
        @Test
        public void remove_number() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void remove_number_when_none_exists() throws Exception {

        }
    }

    public static class AddRole extends ContactGroupIntegTest {

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
        public void when_no_contact_specified() throws Exception {

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
        public void possible_contacts_should_not_include_any_for_which_contact_already_has_a_role() throws Exception {

        }
    }

    public static class RemoveRole extends ContactGroupIntegTest {

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