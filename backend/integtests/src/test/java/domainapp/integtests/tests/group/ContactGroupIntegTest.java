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
package domainapp.integtests.tests.group;

import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactGroupIntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactGroupRepository contactGroupRepository;

    DemoFixture fs;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);
    }
    public static class Change extends ContactGroupIntegTest {

        @Test
        public void happyCase() throws Exception {
            // given
            ContactGroup contactGroup = contactGroupRepository.listAll().get(0);
            String name = "New name";
            String email = "New email";
            String address = "New address";
            String notes = "New notes";

            // when
            contactGroup.change(name, email, address, notes);
            // then
            assertThat(contactGroup.getName()).isEqualTo(name);
            assertThat(contactGroup.getEmail()).isEqualTo(email);
            assertThat(contactGroup.getAddress()).isEqualTo(address);
            assertThat(contactGroup.getNotes()).isEqualTo(notes);
        }

    }
}