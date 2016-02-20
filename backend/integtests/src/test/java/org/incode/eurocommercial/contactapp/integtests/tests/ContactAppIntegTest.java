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

import java.util.List;

import com.google.common.base.Throwables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;

import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;

import org.incode.eurocommercial.contactapp.integtests.bootstrap.ContactAppSystemInitializer;

public abstract class ContactAppIntegTest extends IntegrationTestAbstract {

    @BeforeClass
    public static void initClass() {
        org.apache.log4j.PropertyConfigurator.configure("logging.properties");
        ContactAppSystemInitializer.initIsft();

        // instantiating will install onto ThreadLocal
        new ScenarioExecutionForIntegration();
    }

    public static Matcher<? extends Throwable> causalChainContains(final Class<?> cls) {
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

}
