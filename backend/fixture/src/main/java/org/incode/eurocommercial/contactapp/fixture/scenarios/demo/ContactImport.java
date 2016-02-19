package org.incode.eurocommercial.contactapp.fixture.scenarios.demo;

import java.util.Collections;

import com.google.common.base.Strings;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.country.Country;
import org.incode.eurocommercial.contactapp.dom.country.CountryRepository;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;
import org.incode.eurocommercial.contactapp.dom.number.ContactNumberType;

import lombok.Getter;
import lombok.Setter;

public class ContactImport implements org.isisaddons.module.excel.dom.ExcelFixtureRowHandler {

    @Getter @Setter
    private String country;
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String group;
    @Getter @Setter
    private String company;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String office;
    @Getter @Setter
    private String mobile;
    @Getter @Setter
    private String home;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String role;
    @Getter @Setter
    private String note;
    @Getter @Setter
    private Integer disorder;

    @Override
    public java.util.List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final org.isisaddons.module.excel.dom.ExcelFixture excelFixture,
            final Object previousRow) {

        country = Strings.emptyToNull(country);
        address = Strings.emptyToNull(address);
        group = Strings.emptyToNull(group);
        company = Strings.emptyToNull(company);
        name = Strings.emptyToNull(name);
        office = Strings.emptyToNull(office);
        mobile = Strings.emptyToNull(mobile);
        home = Strings.emptyToNull(home);
        email = Strings.emptyToNull(email);
        role = Strings.emptyToNull(role);
        note = Strings.emptyToNull(note);

        final ContactImport previousContactRow = (ContactImport) previousRow;
        if(previousContactRow != null) {
            if(country == null) country = previousContactRow.getCountry();
            if(group   == null) group   = previousContactRow.getGroup();
        }

        Country country = countryRepository.findOrCreate(this.country);
        ContactGroup contactGroup = contactGroupRepository.findOrCreate(country, group);

        if(address  != null) contactGroup.setAddress(address);
        if(disorder != null) {
            int displayOrder = disorder;
            contactGroup.setDisplayOrder(displayOrder);
        }
        Contact contact = null;
        if(name == null) {
            if(office != null) contactGroup.addContactNumber(office, ContactNumberType.OFFICE.title(), null);
            if(mobile != null) contactGroup.addContactNumber(mobile, ContactNumberType.MOBILE.title(), null);
            if(home   != null) contactGroup.addContactNumber(home, ContactNumberType.HOME.title(), null);
            if(email  != null) contactGroup.setEmail(email);
        }
        else {
            contact = contactRepository.findOrCreate(name, company, email, note, office, mobile, home);
            contact.addContactRole(contactGroup, role, null);
        }
        executionContext.addResult(excelFixture, contact);

        return Collections.singletonList(contact);
    }

    @javax.inject.Inject
    CountryRepository countryRepository;
    @javax.inject.Inject
    ContactRepository contactRepository;
    @javax.inject.Inject
    ContactGroupRepository contactGroupRepository;

}
