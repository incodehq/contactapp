package domainapp.fixture.dom.contact;

import java.util.Collections;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.dom.contacts.Contact;
import domainapp.dom.contacts.ContactRepository;
import domainapp.dom.country.Country;
import domainapp.dom.country.CountryRepository;
import domainapp.dom.group.ContactGroup;
import domainapp.dom.group.ContactGroupRepository;
import lombok.Getter;
import lombok.Setter;

public class ContactRowHandler implements org.isisaddons.module.excel.dom.ExcelFixtureRowHandler {

    @Getter @Setter
    private String country;
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

    @Override
    public java.util.List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final org.isisaddons.module.excel.dom.ExcelFixture excelFixture,
            final Object previousRow) {

        final ContactRowHandler previousContactRow = (ContactRowHandler) previousRow;
        if(previousContactRow != null) {
            if(country == null) country = previousContactRow.getCountry();
            if(group == null) group = previousContactRow.getGroup();
            if(name == null) name = previousContactRow.getName();
            if(office == null) office = previousContactRow.getOffice();
            if(mobile == null) mobile = previousContactRow.getMobile();
            if(home == null) home = previousContactRow.getHome();
            if(email == null) email = previousContactRow.getEmail();
            if(role == null) role = previousContactRow.getRole();
            if(note == null) note = previousContactRow.getNote();
        }
        if(company != null && company.equals("(none)")) company = null;

        Country country = countryRepository.findOrCreate(this.country);
        ContactGroup contactGroup = contactGroupRepository.findOrCreate(country, name);

        if(name == null) {
            name = this.country + "/" + this.role;
        }

        Contact contact = contactRepository.findOrCreate(name, company, email, note, office, mobile, home);

        contact.addContactRole(contactGroup, role);

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
