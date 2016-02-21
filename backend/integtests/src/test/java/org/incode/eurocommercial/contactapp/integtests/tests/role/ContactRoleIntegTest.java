/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package org.incode.eurocommercial.contactapp.integtests.tests.role;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;
import org.incode.eurocommercial.contactapp.dom.role.ContactRoleRepository;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

public class ContactRoleIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactGroupRepository contactGroupRepository;

    @Inject
    ContactRepository contactRepository;

    @Inject
    ContactRoleRepository contactRoleRepository;

    DemoFixture fs;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);
        nextTransaction();
    }


    public static class AlsoInGroup extends ContactRoleIntegTest {

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
        public void possible_groups_should_not_include_any_for_which_contact_already_has_a_role() throws Exception {

        }

    }

    public static class AlsoWithContact extends ContactRoleIntegTest {


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
        public void possible_contacts_should_not_include_any_which_already_have_a_role_in_group() throws Exception {

        }

    }

    public static class Edit extends ContactRoleIntegTest {


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
        public void when_no_role_specified() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_both_existing_role_and_new_role_specified() throws Exception {

        }

    }

    public static class Delete extends ContactRoleIntegTest {

        @Ignore("TODO")
        @Test
        public void happy_case() throws Exception {

        }

    }


}