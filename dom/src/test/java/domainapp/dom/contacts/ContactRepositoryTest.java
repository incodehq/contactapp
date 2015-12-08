///**
// *  Licensed to the Apache Software Foundation (ASF) under one or more
// *  contributor license agreements.  See the NOTICE file distributed with
// *  this work for additional information regarding copyright ownership.
// *  The ASF licenses this file to You under the Apache License, Version 2.0
// *  (the "License"); you may not use this file except in compliance with
// *  the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package domainapp.dom.contacts;
//
//import org.jmock.Expectations;
//import org.jmock.Sequence;
//import org.jmock.auto.Mock;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import org.apache.isis.applib.DomainObjectContainer;
//import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
//import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
//
//import domainapp.dom.number.ContactNumberRepository;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.CoreMatchers.equalTo;
//
//public class ContactRepositoryTest {
//
//    @Rule
//    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);
//
//    @Mock
//    DomainObjectContainer mockContainer;
//
//    @Mock
//    ContactNumberRepository mockContactNumberRepository;
//
//    ContactRepository contactRepository;
//
//    @Before
//    public void setUp() throws Exception {
//        contactRepository = new ContactRepository();
//        contactRepository.container = mockContainer;
//
//    }
//
//    public static class Create extends ContactRepositoryTest {
//
//        @Test
//        public void happyCase() throws Exception {
//
//            // given
//            final Contact contact = new Contact();
//            contact.contactNumberRepository = mockContactNumberRepository;
//
//            final Sequence seq = context.sequence("create");
//            context.checking(new Expectations() {
//                {
//                    oneOf(mockContainer).newTransientInstance(Contact.class);
//                    inSequence(seq);
//                    will(returnValue(contact));
//
//                    oneOf(mockContainer).persistIfNotAlready(contact);
//                    inSequence(seq);
//
//                    exactly(3).of(mockContactNumberRepository).findOrCreate(
//                            with(equalTo(contact)), with(any(String.class)), with(any(String.class)));
//                }
//            });
//
//            // when
//            Contact obj = new Contact();
//            obj = contactRepository.create("a name", "a company", "an email", "some notes", "an office number", "a mobile number", "a home number");
//
//            // then
//            assertThat(obj).isEqualTo(contact);
//            assertThat(obj.getName()).isEqualTo("a name");
//        }
//
//    }
//
//}
