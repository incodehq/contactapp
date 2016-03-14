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
package org.incode.eurocommercial.contactapp.integtests.tests;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.contactapp.dom.number.ContactNumber;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumberType;
import org.incode.eurocommercial.contactapp.integtests.bootstrap.ContactAppSystemInitializer;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ContactAppIntegTest extends IntegrationTestAbstract {


    @Inject protected
    FixtureScripts fixtureScripts;
    @Inject protected
    FakeDataService fakeDataService;


    @BeforeClass
    public static void initClass() {
        org.apache.log4j.PropertyConfigurator.configure("logging.properties");
        ContactAppSystemInitializer.initIsft();

        // instantiating will install onto ThreadLocal
        new ScenarioExecutionForIntegration();
    }

    protected static Matcher<? extends Throwable> causalChainContains(final Class<?> cls) {
        return new TypeSafeMatcher<Throwable>() {
            @Override
            protected boolean matchesSafely(Throwable item) {
                final List<Throwable> causalChain = Throwables.getCausalChain(item);
                for (Throwable throwable : causalChain) {
                    if(cls.isAssignableFrom(throwable.getClass())){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("exception with causal chain containing " + cls.getSimpleName());
            }
        };
    }

    protected static void assertContains(
            final Collection<ContactNumber> contactNumbers,
            final String type,
            final String number) {
        assertThat(FluentIterable
                    .from(contactNumbers)
                    .filter(x -> Objects.equals(x.getType(), type)))
                .extracting(ContactNumber::getNumber)
                .contains(number);
    }

    protected static void assertNotContains(
            final Collection<ContactNumber> contactNumbers,
            final String number) {
        assertThat(contactNumbers)
                .extracting(ContactNumber::getNumber)
                .doesNotContain(number);
    }

    protected String randomPhoneNumber() {
        return "+" + fakeDataService.strings().digits(2) + " " + fakeDataService.strings().digits(4) + " " + fakeDataService.strings().digits(6);
    }

}
