package domainapp.app.rest.v1.country;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Function;

import org.apache.isis.applib.DomainObjectContainer;

import domainapp.dom.country.Country;

@XmlRootElement(name = "country")
public class CountryViewModel {

    public static Function<Country, CountryViewModel> create(final DomainObjectContainer container) {
        return new Function<Country, CountryViewModel>() {
            @Nullable @Override public CountryViewModel apply(@Nullable final Country input) {
                return input != null? container.injectServicesInto(new CountryViewModel(input)): null;
            }
        };
    }

    @XmlElement(required = true)
    private Country underlying;

    public CountryViewModel() {
    }

    public CountryViewModel(Country underlying) {
        this.underlying = underlying;
    }

    @XmlTransient
    public String getName() {
        return underlying.getName();
    }

    public String title() {
        return underlying != null? container.titleOf(underlying): "(no underlying)";
    }
    @Override
    public String toString() {
        return underlying != null? underlying.toString(): "(no underlying)";
    }

    @Inject
    DomainObjectContainer container;

}
