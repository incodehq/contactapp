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
package org.incode.eurocommercial.contactapp.dom;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

import org.apache.isis.applib.Module;
import org.apache.isis.applib.ModuleAbstract;

import org.isisaddons.module.excel.ExcelModule;

import org.incode.eurocommercial.contactapp.module.base.ContactAppBaseModule;
import org.incode.eurocommercial.contactapp.module.contactable.ContactAppContactableModule;
import org.incode.eurocommercial.contactapp.module.contacts.ContactAppContactModule;
import org.incode.eurocommercial.contactapp.module.country.ContactAppCountryModule;
import org.incode.eurocommercial.contactapp.module.group.ContactAppGroupModule;
import org.incode.eurocommercial.contactapp.module.number.ContactAppNumberModule;
import org.incode.eurocommercial.contactapp.module.role.ContactAppRoleModule;
import org.incode.eurocommercial.contactapp.module.user.ContactAppUserModule;
import org.incode.module.settings.SettingsModule;

@XmlRootElement(name = "module")
public class ContactAppDomainModule extends ModuleAbstract {

    @Override
    public Set<Module> getDependencies() {
        return Sets.newHashSet(
                new ContactAppBaseModule(),
                new ContactAppContactableModule(),
                new ContactAppContactModule(),
                new ContactAppCountryModule(),
                new ContactAppGroupModule(),
                new ContactAppNumberModule(),
                new ContactAppRoleModule(),
                new ContactAppUserModule(),
                new ExcelModule(),
                new SettingsModule()
        );
    }

    public static class MaxLength {
        private MaxLength(){}
        public static final int NOTES = 2048;
    }

}
