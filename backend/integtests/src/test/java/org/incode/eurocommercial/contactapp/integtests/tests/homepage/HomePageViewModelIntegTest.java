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
package org.incode.eurocommercial.contactapp.integtests.tests.homepage;

import java.util.List;
import java.util.function.Predicate;

import javax.inject.Inject;

import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.wrapper.DisabledException;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.contactapp.app.services.homepage.HomePageService;
import org.incode.eurocommercial.contactapp.app.services.homepage.HomePageViewModel;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.country.Country;
import org.incode.eurocommercial.contactapp.dom.country.CountryRepository;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.role.ContactRole;
import org.incode.eurocommercial.contactapp.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.contactapp.integtests.tests.ContactAppIntegTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class HomePageViewModelIntegTest extends ContactAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactRepository contactRepository;
    @Inject
    CountryRepository countryRepository;

    @Inject
    FakeDataService fakeDataService;

    @Inject
    HomePageService homePageService;

    DemoFixture fs;

    HomePageViewModel homePageViewModel;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        homePageViewModel = homePageService.homePage();
    }

    public static class ContactGroups extends HomePageViewModelIntegTest {

        @Test
        public void accessible() throws Exception {
            // when
            final List<ContactGroup> groups = homePageViewModel.getGroups();

            // then
            assertThat(groups.size()).isEqualTo(4);
        }

    }

    public static class CreateGroup extends HomePageViewModelIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<ContactGroup> groups = homePageViewModel.getGroups();
            final int sizeBefore = groups.size();

            // when
            final List<Country> list = countryRepository.listAll();
            final Country someCountry = fakeDataService.collections().anyOf(list);
            final String groupName = fakeDataService.strings().fixed(8);

            wrap(homePageViewModel).createContactGroup(someCountry, groupName);

            // then
            final List<ContactGroup> groupsAfter = homePageViewModel.getGroups();
            assertThat(groupsAfter.size()).isEqualTo(sizeBefore+1);
            assertThat(groupsAfter)
                    .filteredOn(contactGroupOf2(someCountry, groupName))
                    .hasSize(1);
        }

    }

    public static class DeleteGroup extends HomePageViewModelIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Country> list = countryRepository.listAll();
            final Country someCountry = fakeDataService.collections().anyOf(list);
            final String groupName = fakeDataService.strings().fixed(8);

            wrap(homePageViewModel).createContactGroup(someCountry, groupName);

            final List<ContactGroup> groups = homePageViewModel.getGroups();

            final ContactGroup addedContactGroup = FluentIterable.from(groups)
                    .filter(contactGroupOf(someCountry, groupName))
                    .toList().get(0);

            final int sizeBefore = groups.size();

            final List<ContactRole> contactRoles = addedContactGroup.getContactRoles();
            assertThat(contactRoles).isEmpty();

            // when
            final List<ContactGroup> contactGroupChoices = homePageViewModel.choices0DeleteContactGroup();

            // then
            assertThat(contactGroupChoices).contains(addedContactGroup);

            // and when
            wrap(homePageViewModel).deleteContactGroup(addedContactGroup);

            // then
            final List<ContactGroup> groupsAfter = homePageViewModel.getGroups();
            assertThat(groupsAfter).hasSize(sizeBefore-1);

            assertThat(groupsAfter)
                    .filteredOn(contactGroupOf2(someCountry, groupName))
                    .hasSize(0);
        }

        @Test
        public void cannot_delete_group_that_has_contact_roles() throws Exception {


            // given
            final List<ContactGroup> groups = homePageViewModel.getGroups();

            final ContactGroup someGroup = fakeDataService.collections().anyOf(groups);

            final List<ContactRole> contactRoles = someGroup.getContactRoles();
            assertThat(contactRoles).isNotEmpty();

            // when
            final List<ContactGroup> contactGroupChoices = homePageViewModel.choices0DeleteContactGroup();

            // then
            assertThat(contactGroupChoices).doesNotContain(someGroup);

            // then
            expectedExceptions.expect(DisabledException.class);

            // when
            wrap(homePageViewModel).deleteContactGroup(someGroup);
        }

    }

    private static Predicate<ContactGroup> contactGroupOf2(
            final Country someCountry,
            final String groupName) {
        return contactGroup -> matches(someCountry, groupName, contactGroup);
    }

    private static com.google.common.base.Predicate<ContactGroup> contactGroupOf(
            final Country someCountry,
            final String groupName) {
        return contactGroup -> matches(someCountry, groupName, contactGroup);
    }

    private static boolean matches(final Country someCountry, final String groupName, final ContactGroup contactGroup) {
        return Objects.equal(contactGroup.getName(), groupName) &&
        Objects.equal(contactGroup.getCountry().getName(), someCountry.getName());
    }

}