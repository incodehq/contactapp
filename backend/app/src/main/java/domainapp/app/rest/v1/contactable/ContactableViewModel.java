package domainapp.app.rest.v1.contactable;

import java.util.List;

import domainapp.app.rest.v1.number.ContactNumberViewModel;

public interface ContactableViewModel {

    String getName();

    String getEmail();

    String getNotes();

    List<ContactNumberViewModel> getContactNumbers();

}
