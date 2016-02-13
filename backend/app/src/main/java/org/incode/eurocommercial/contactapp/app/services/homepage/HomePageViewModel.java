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
package org.incode.eurocommercial.contactapp.app.services.homepage;

import java.util.List;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.ViewModel;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.country.Country;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;

@ViewModel
public class HomePageViewModel {

    public String title() {
        return "Contact Groups";
    }


    @CollectionLayout(paged=1000)
    @org.apache.isis.applib.annotation.HomePage
    public List<ContactGroup> getGroups() {
        return contactGroupRepository.listAll();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Create")
    @MemberOrder(name = "groups", sequence = "1")
    public HomePageViewModel createContactGroup(final Country country, String name) {
        contactGroupRepository.findOrCreate(country, name);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Delete")
    @MemberOrder(name = "groups", sequence = "2")
    public HomePageViewModel deleteContactGroup(final ContactGroup contactGroup) {
        contactGroupRepository.delete(contactGroup);
        return this;
    }

    public List<ContactGroup> choices0DeleteContactGroup() {
        final List<ContactGroup> contactGroups = contactGroupRepository.listAll();
        final List<Contact> contacts = contactRepository.listAll();
        final ImmutableList<ContactGroup> usedContactGroups = FluentIterable.from(contacts)
                .transformAndConcat(contact -> contact.getContactRoles())
                .transform(contactRole -> contactRole.getContactGroup())
                .toList();
        contactGroups.removeAll(usedContactGroups);
        return contactGroups;
    }
    public String disableDeleteContactGroup() {
        return choices0DeleteContactGroup().isEmpty()? "No contact groups without contacts": null;
    }

    @javax.inject.Inject
    ContactGroupRepository contactGroupRepository;
    @javax.inject.Inject
    ContactRepository contactRepository;


}
