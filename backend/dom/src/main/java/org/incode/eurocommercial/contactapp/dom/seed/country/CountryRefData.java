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
package org.incode.eurocommercial.contactapp.dom.seed.country;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.contactapp.dom.country.CountryRepository;

public class CountryRefData extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        countryRepository.findOrCreate("Global");
        countryRepository.findOrCreate("France");
        countryRepository.findOrCreate("Italy");
        countryRepository.findOrCreate("Sweden");
        countryRepository.findOrCreate("Belgium");
    }

    @Inject
    CountryRepository countryRepository;

}
