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
package org.incode.eurocommercial.contactapp.integtests.tests.number;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.contactapp.dom.number.ContactNumber;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumberRepository;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumberType;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactNumberIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactNumberRepository contactNumberRepository;

    ContactNumber contactNumber;

    DemoFixture fs;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        contactNumber = fs.getContacts().get(0).getContactNumbers().first();
        nextTransaction();

        assertThat(contactNumber).isNotNull();
    }

    public static class Create extends ContactNumberIntegTest {

        @Test
        public void add_number_with_existing_type() throws Exception {
            // given
            final String contactNumberType = ContactNumberType.OFFICE.title();
            final String newOfficePhoneNumber = randomPhoneNumber();
            assertThat(contactNumberRepository.listAll())
                    .extracting(ContactNumber::getNumber)
                    .doesNotContain(newOfficePhoneNumber);

            // when
            wrap(this.contactNumber).create(newOfficePhoneNumber, contactNumberType, null);
            nextTransaction();

            // then
            assertThat(contactNumberRepository.listAll())
                    .extracting(ContactNumber::getNumber)
                    .contains(newOfficePhoneNumber);
        }

        @Test
        public void add_number_with_new_type() throws Exception {
            // given
            final String contactNumberType = "New type";
            final String newOfficePhoneNumber = randomPhoneNumber();
            assertThat(contactNumberRepository.listAll())
                    .extracting(ContactNumber::getNumber)
                    .doesNotContain(newOfficePhoneNumber);

            // when
            wrap(this.contactNumber).create(newOfficePhoneNumber, null, contactNumberType);
            nextTransaction();

            // then
            assertThat(contactNumberRepository.listAll())
                    .extracting(ContactNumber::getNumber)
                    .contains(newOfficePhoneNumber);
        }

        @Ignore
        @Test
        public void add_number_when_already_have_number_of_any_type() throws Exception {
            // given
            final String existingNumber = this.contactNumber.getNumber();
            System.out.println(existingNumber);

//            // then
//            thrown.expect(ArrayIndexOutOfBoundsException.class);
//            // TODO: Insert invalidation message
//            thrown.expectMessage("");
//
//            // when
//            wrap(this.contactNumber).create(existingNumber, ContactNumberType.OFFICE.title(), null);
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

    public static class Edit extends ContactNumberIntegTest {

        @Ignore("TODO")
        @Test
        public void change_number() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void change_type_to_existing() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void change_type_to_new_type() throws Exception {

        }

        @Ignore("TODO")
        @Test
        public void when_change_number_to_already_existing() throws Exception {

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

    public static class Delete extends ContactNumberIntegTest {

        @Ignore("TODO")
        @Test
        public void happy_case() throws Exception {

        }

    }

}