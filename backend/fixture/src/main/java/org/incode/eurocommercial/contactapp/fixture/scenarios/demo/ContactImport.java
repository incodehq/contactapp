package org.incode.eurocommercial.contactapp.fixture.scenarios.demo;

import java.util.Collections;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.contactapp.dom.contacts.Contact;
import org.incode.eurocommercial.contactapp.dom.contacts.ContactRepository;
import org.incode.eurocommercial.contactapp.dom.country.Country;
import org.incode.eurocommercial.contactapp.dom.country.CountryRepository;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;
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

        if(country  != null && country .equals("")) country  = null;
        if(address  != null && address .equals("")) address  = null;
        if(group    != null && group   .equals("")) group    = null;
        if(company  != null && company .equals("")) company  = null;
        if(name     != null && name    .equals("")) name     = null;
        if(office   != null && office  .equals("")) office   = null;
        if(mobile   != null && mobile  .equals("")) mobile   = null;
        if(home     != null && home    .equals("")) home     = null;
        if(email    != null && email   .equals("")) email    = null;
        if(role     != null && role    .equals("")) role     = null;
        if(note     != null && note    .equals("")) note     = null;
        if(disorder != null && disorder.equals("")) disorder = null;

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
            if(office != null) contactGroup.addContactNumber("Office", office);
            if(mobile != null) contactGroup.addContactNumber("Mobile", mobile);
            if(home   != null) contactGroup.addContactNumber("Home", home);
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
