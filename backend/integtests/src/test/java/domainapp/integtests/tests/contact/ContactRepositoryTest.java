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
package domainapp.integtests.tests.contact;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import domainapp.dom.contacts.Contact;
import domainapp.dom.contacts.ContactRepository;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import static org.assertj.core.api.Assertions.assertThat;

public class ContactRepositoryTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactRepository contactRepository;

    @Before
    public void setUp() throws Exception {
        // given
        FixtureScript fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);
    }

    public static class ListAll extends ContactRepositoryTest {

        @Test
        public void happyCase() throws Exception {
            // given, when
            final List<Contact> contacts = contactRepository.listAll();

            // then
            assertThat(contacts.size()).isEqualTo(14);
        }
    }

    public static class Find extends ContactRepositoryTest {

        @Test
        public void multipleFindersSingleQuery() throws Exception {
            // given
            String query = "(?i).*a.*";
            // when
            final List<Contact> contacts = contactRepository.find(query);

            // then
            assertThat(contacts.size()).isEqualTo(14); // 9 contact names, 4 contact groups, 1 both group and role name
        }
    }

    public static class FindByName extends ContactRepositoryTest {

        @Test
        public void happyCase() throws Exception {
            // given
            final String contactName = contactRepository.listAll().get(0).getName();

            // when
            final List<Contact> contacts = contactRepository.findByName(contactName);

            // then
            assertThat(contacts.size()).isGreaterThan(0);
        }

        @Test
        public void partialName() throws Exception {
            // given
            final String contactName = contactRepository.listAll().get(0).getName();
            final String firstName = contactName.split(" ")[0];
            final String firstLetter = "b";

            // when
            final List<Contact> contact = contactRepository.findByName("(?i).*" + firstName + ".*");
            final List<Contact> contacts = contactRepository.findByName("(?i).*" + firstLetter + ".*");

            // then
            assertThat(contact.size()).isEqualTo(1);
            assertThat(contacts.size()).isEqualTo(6);
        }

        @Test
        public void sadCase() throws Exception {
            // given
            final String contactName = "Not a name";

            // when
            final List<Contact> contacts = contactRepository.findByName("(?i).*" + contactName + ".*");

            // then
            assertThat(contacts.size()).isEqualTo(0);
        }
    }

    public static class FindByContactGroup extends ContactRepositoryTest {

        @Inject
        ContactGroupRepository contactGroupRepository;

        @Test
        public void happyCase() throws Exception {
            // given
            final ContactGroup contactGroup = contactGroupRepository.listAll().get(0);

            // when
            final List<Contact> contacts = contactRepository.findByContactGroup(contactGroup);

            // then
            assertThat(contacts.size()).isGreaterThan(0);
        }

        @Test
        public void sadCase() throws Exception {
            // given
            final ContactGroup contactGroup = new ContactGroup();

            // when
            final List<Contact> contacts = contactRepository.findByContactGroup(contactGroup);

            // then
            assertThat(contacts.size()).isEqualTo(0);
        }
    }

    public static class FindByContactRoleName extends ContactRepositoryTest {

        @Inject
        ContactRoleRepository contactRoleRepository;

        @Test
        public void happyCase() throws Exception {
            // given
            List<ContactRole> list = contactRoleRepository.listAll();
            String regex = "No ContactRoleName in fixtures";

            for(ContactRole contactRole : list) {
                if (contactRole.getRoleName() != null) {
                    regex = contactRole.getRoleName();
                }
            }

            // when
            final List<Contact> contacts = contactRepository.findByContactRoleName(regex);

            // then
            assertThat(contacts.size()).isGreaterThan(0);
        }

        @Test
        public void sadCase() throws Exception {
            // given
            final String roleName = "Not a role";

            // when
            final List<Contact> contacts = contactRepository.findByContactRoleName(roleName);

            // then
            assertThat(contacts.size()).isEqualTo(0);
        }
    }

}