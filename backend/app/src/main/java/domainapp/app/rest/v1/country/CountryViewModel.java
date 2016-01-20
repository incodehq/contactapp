package domainapp.app.rest.v1.country;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.google.common.base.Function;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService;

import domainapp.app.rest.ViewModelWithUnderlying;
import domainapp.dom.country.Country;

public class CountryViewModel extends ViewModelWithUnderlying<Country> {

    public static Function<Country, CountryViewModel> create(final DomainObjectContainer container) {
        return new Function<Country, CountryViewModel>() {
            @Nullable @Override public CountryViewModel apply(@Nullable final Country input) {
                return input != null? container.injectServicesInto(new CountryViewModel(input)): null;
            }
        };
    }

    public CountryViewModel() {
    }

    public CountryViewModel(Country underlying) {
        this.underlying = underlying;
    }

    public String getName() {
        return underlying.getName();
    }


}
