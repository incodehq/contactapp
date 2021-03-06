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

import org.apache.isis.applib.AbstractSubscriber;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.module.settings.dom.UserSetting;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class HideContributionForSettings extends AbstractSubscriber {

    @Programmatic
    @com.google.common.eventbus.Subscribe
    @org.axonframework.eventhandling.annotation.EventHandler
    public void on(final org.isisaddons.module.command.dom.HasUsername_recentCommandsByUser.ActionDomainEvent ev) {
        final List<Object> arguments = ev.getArguments();
        if(arguments.size()==1 && arguments.get(0) instanceof UserSetting) {
            ev.veto("");
        }
    }

    @Programmatic
    @com.google.common.eventbus.Subscribe
    @org.axonframework.eventhandling.annotation.EventHandler
    public void on(final org.isisaddons.module.sessionlogger.dom.HasUsername_recentSessionsForUser.ActionDomainEvent ev) {
        final List<Object> arguments = ev.getArguments();
        if(arguments.size()==1 && arguments.get(0) instanceof UserSetting) {
            ev.veto("");
        }
    }

}
