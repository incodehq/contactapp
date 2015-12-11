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

package domainapp.fixture.scenarios.demo;

import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;

import domainapp.dom.contacts.Contact;
import domainapp.fixture.dom.contact.ContactTearDown;
import domainapp.fixture.dom.country.CountryTearDown;
import domainapp.fixture.dom.group.ContactGroupTearDown;
import domainapp.fixture.dom.number.ContactNumberTearDown;
import domainapp.fixture.dom.role.ContactRoleTearDown;
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

        getContacts().addAll((List) objectsByClass.get(ContactRowHandler.class));
    }

    protected String getSpreadsheetBasename() {
        return getClass().getSimpleName();
    }

    private Class[] getHandlers() {
        return new Class[]{
                ContactRowHandler.class,
        };
    }
}
