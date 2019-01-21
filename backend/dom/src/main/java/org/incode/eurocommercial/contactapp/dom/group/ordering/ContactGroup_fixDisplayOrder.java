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
package org.incode.eurocommercial.contactapp.dom.group.ordering;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.eurocommercial.contactapp.module.group.dom.ContactGroup;

@Mixin
public class ContactGroup_fixDisplayOrder {

    private final ContactGroup contactGroup;

    public ContactGroup_fixDisplayOrder(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(
            named = "Fix",
            cssClassFa = "sort-alpha-asc"
    )
    public ContactGroupOrderingViewModel $$() {
        return container.injectServicesInto(new ContactGroupOrderingViewModel(contactGroup));
    }

    @Inject
    DomainObjectContainer container;

}
