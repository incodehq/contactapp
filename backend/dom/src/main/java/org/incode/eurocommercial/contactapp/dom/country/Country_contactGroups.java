package org.incode.eurocommercial.contactapp.dom.country;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Mixin;

import org.incode.eurocommercial.contactapp.dom.group.ContactGroup;
import org.incode.eurocommercial.contactapp.dom.group.ContactGroupRepository;

@Mixin
public class Country_contactGroups {

    private final Country country;

    public Country_contactGroups(Country country) {
        this.country = country;
    }

    @Action()
    public List<ContactGroup> $$() {
        return contactGroupRepository.findByCountry(this.country);
    }

    @Inject
    ContactGroupRepository contactGroupRepository;

}
