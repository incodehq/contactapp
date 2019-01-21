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
package org.incode.eurocommercial.contactapp.app;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

import org.apache.isis.applib.Module;
import org.apache.isis.applib.ModuleAbstract;

import org.isisaddons.module.docx.DocxModule;
import org.isisaddons.module.security.SecurityModule;

import org.incode.eurocommercial.contactapp.dom.ContactAppDomainModule;

@XmlRootElement(name = "module")
public class ContactAppAppModule extends ModuleAbstract {

    @Override
    public Set<Module> getDependencies() {
        return Sets.newHashSet(
                new ContactAppDomainModule(),
                new DocxModule(),
                new SecurityModule()
        );
    }

    @Override public Set<Class<?>> getAdditionalServices() {
        return Sets.newHashSet(
                org.isisaddons.module.security.dom.password.PasswordEncryptionServiceUsingJBcrypt.class
        );
    }
}
