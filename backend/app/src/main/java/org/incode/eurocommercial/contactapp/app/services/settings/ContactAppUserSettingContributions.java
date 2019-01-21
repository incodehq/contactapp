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
package org.incode.eurocommercial.contactapp.app.services.settings;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.user.ApplicationUser;

import org.incode.module.settings.dom.jdo.UserSettingJdo;

@DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class ContactAppUserSettingContributions {

    @Action(
            semantics = SemanticsOf.SAFE,
            hidden = Where.EVERYWHERE   // CURRENTLY NO APP SETTINGS, SO JUST HIDE FOR NOW...
    )
    @ActionLayout(
            named = "User Settings",
            contributed = Contributed.AS_ASSOCIATION
    )
    @CollectionLayout(
            named = "Settings",
            render = RenderType.EAGERLY
    )
    public List<UserSettingJdo> userSettingsFor(final ApplicationUser applicationUser) {
        return settingsService.listAllSettings(applicationUser.getUsername());
    }


    @javax.inject.Inject
    private ContactAppSettingsService settingsService;

}
