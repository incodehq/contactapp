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

import domainapp.dom.contacts.Contact;
import domainapp.dom.contacts.ContactRepository;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import domainapp.dom.role.ContactRoleRepository;
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

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
            assertThat(contacts.size()).isEqualTo(13);
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
            String regex = contactRoleRepository.listAll().get(0).getRoleName();
            if(regex == null) regex = "";
            regex = "(?i).*" + regex + ".*";
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