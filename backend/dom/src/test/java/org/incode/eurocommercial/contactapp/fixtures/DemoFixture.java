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
package org.incode.eurocommercial.contactapp.fixtures;

import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;

import org.incode.eurocommercial.contactapp.module.contact.fixture.ContactTearDown;
import org.incode.eurocommercial.contactapp.module.contacts.dom.Contact;
import org.incode.eurocommercial.contactapp.module.country.fixture.CountryTearDown;
import org.incode.eurocommercial.contactapp.module.group.fixture.ContactGroupTearDown;
import org.incode.eurocommercial.contactapp.module.number.fixture.ContactNumberTearDown;
import org.incode.eurocommercial.contactapp.module.role.fixture.ContactRoleTearDown;

import lombok.Getter;

public class DemoFixture extends FixtureScript {

    public DemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Getter
    private final List<Contact> contacts = Lists.newArrayList();

    @Override
    protected void execute(final ExecutionContext ec) {

        // zap everything
        ec.executeChild(this, new ContactRoleTearDown());
        ec.executeChild(this, new ContactNumberTearDown());
        ec.executeChild(this, new ContactTearDown());
        ec.executeChild(this, new ContactGroupTearDown());
        ec.executeChild(this, new CountryTearDown());

        // load data from spreadsheet
        final URL spreadsheet = Resources.getResource(DemoFixture.class, getSpreadsheetBasename() + ".xlsx");
        final ExcelFixture fs = new ExcelFixture(spreadsheet, getHandlers());
        ec.executeChild(this, fs);

        // make objects created by ExcelFixture available to our caller.
        final Map<Class, List<Object>> objectsByClass = fs.getObjectsByClass();

        getContacts().addAll(
                FluentIterable
                        .from((List) objectsByClass.get(ContactImport.class))
                        .filter(Predicates.notNull())
                        .toList());

    }

    protected String getSpreadsheetBasename() {
        return getClass().getSimpleName();
    }

    private Class[] getHandlers() {
        return new Class[]{
                ContactImport.class,
        };
    }
}
