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
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactIntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactRepository contactRepository;

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
}