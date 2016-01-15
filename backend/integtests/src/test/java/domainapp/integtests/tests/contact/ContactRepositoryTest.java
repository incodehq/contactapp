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
        public void sadCase() throws Exception {
            // given
            final String contactName = "Not a name";

            // when
            final List<Contact> contacts = contactRepository.findByName(contactName);

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

            int i = 0;
            while(i < list.size()) {
                if(list.get(i).getRoleName() != null) {
                    regex = list.get(i).getRoleName();
                    break;
                }
                i++;
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

    public static class FindByNameUsingRexEx extends ContactRepositoryTest {

        @Test
        public void happyCase() throws Exception {
            // given
            String contactName = contactRepository.listAll().get(0).getName();
            contactName = contactName.split(" ")[0];

            // when
            final String regex1 = contactName;
            final String regex2 = contactName + "*";

            final List<Contact> contacts1 = contactRepository.findByName(regex1);
            final List<Contact> contacts2 = contactRepository.findByName(regex2);

            // then
            assertThat(contacts1.size()).isEqualTo(0);
            assertThat(contacts2.size()).isGreaterThan(0);
        }

        @Test
        public void sadCase() throws Exception {
            // given
            final String contactName = "*Not a name*";

            // when
            final List<Contact> contacts = contactRepository.findByName(contactName);

            // then
            assertThat(contacts.size()).isEqualTo(0);
        }
    }

}