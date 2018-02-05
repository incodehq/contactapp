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
package org.incode.eurocommercial.contactapp.dom.seed.roles;

import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionRule;
import org.isisaddons.module.security.seed.scripts.AbstractRoleAndPermissionsFixtureScript;

import org.incode.eurocommercial.contactapp.dom.ContactAppDomainModule;

public class ContactAppAdminRoleAndPermissions extends AbstractRoleAndPermissionsFixtureScript {

    public static final String ROLE_NAME = "contactapp-admin-role";

    public ContactAppAdminRoleAndPermissions() {
        super(ROLE_NAME, "Read/write access to contactapp domain objects and home page");
    }

    @Override
    protected void execute(final ExecutionContext executionContext) {
        newPackagePermissions(
                ApplicationPermissionRule.ALLOW,
                ApplicationPermissionMode.CHANGING,
                "org.incode.eurocommercial.contactapp.app",
                ContactAppDomainModule.class.getPackage().getName()
                );
        newClassPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                classFor("org.incode.eurocommercial.contactapp.app.services.ContactAppImportService")
        );
        newClassPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                classFor("org.incode.eurocommercial.contactapp.app.services.ContactAppTearDownService")
        );
        newClassPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                classFor("org.incode.eurocommercial.contactapp.app.services.ContactAppCreateCountriesService")
        );

    }
    private static Class<?> classFor(final String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}