package domainapp.fixture.scenarios.demo;

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

    @Override
    public java.util.List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final org.isisaddons.module.excel.dom.ExcelFixture excelFixture,
            final Object previousRow) {

        final ContactImport previousContactRow = (ContactImport) previousRow;
        if(previousContactRow != null) {
            if(country == null) country = previousContactRow.getCountry();
            if(group == null) group = previousContactRow.getGroup();
        }
        if(company != null && company.equals("(none)")) company = null;

        Country country = countryRepository.findOrCreate(this.country);
        ContactGroup contactGroup = contactGroupRepository.findOrCreate(country, group);

        if(address != null) {
            contactGroup.setAddress(address);
        }

        if(name == null) {
            name = this.country + "/" + this.role;
        }

        Contact contact = contactRepository.findOrCreate(name, company, email, note, office, mobile, home);

        contact.addContactRole(contactGroup, role, null);

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
