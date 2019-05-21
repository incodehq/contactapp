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
package org.incode.eurocommercial.contactapp.module.user.seed;

import java.util.Arrays;

import org.isisaddons.module.security.dom.user.AccountType;
import org.isisaddons.module.security.seed.scripts.AbstractUserAndRolesFixtureScript;
import org.isisaddons.module.security.seed.scripts.GlobalTenancy;
import org.isisaddons.module.security.seed.scripts.IsisModuleSecurityAdminRoleAndPermissions;

import org.incode.eurocommercial.contactapp.module.role.seed.ApacheIsisRoleAndPermissions;
import org.incode.eurocommercial.contactapp.module.role.seed.ContactAppSuperadminRoleAndPermissions;

public class SuperadminUser extends AbstractUserAndRolesFixtureScript {

    public static final String USER_NAME = "superadmin";
    private static final String PASSWORD = "pass";

    public SuperadminUser() {
        super(USER_NAME, PASSWORD, null,
                GlobalTenancy.TENANCY_PATH, AccountType.LOCAL,
                Arrays.asList(
                        IsisModuleSecurityAdminRoleAndPermissions.ROLE_NAME,
                        ContactAppSuperadminRoleAndPermissions.ROLE_NAME,
                        ApacheIsisRoleAndPermissions.ROLE_NAME

                        // configured by not required by any user:
                        //,SettingsModuleRoleAndPermissions.ROLE_NAME
                        //,ContactAppFixtureServiceRoleAndPermissions.ROLE_NAME

                        // not configured:
                        //,TogglzModuleAdminRole.ROLE_NAME
                        //,AuditModuleRoleAndPermissions.ROLE_NAME
                        //,CommandModuleRoleAndPermissions.ROLE_NAME
                        //,SessionLoggerModuleRoleAndPermissions.ROLE_NAME
                        //,PublishingModuleRoleAndPermissions.ROLE_NAME
                        //,TranslationServicePoMenuRoleAndPermissions.ROLE_NAME
                ));
    }


    @Override
    protected void execute(ExecutionContext executionContext) {
        super.execute(executionContext);
    }

}