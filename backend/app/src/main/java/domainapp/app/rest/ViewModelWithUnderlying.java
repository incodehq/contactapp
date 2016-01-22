package domainapp.app.rest;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.ViewModel;

public abstract class ViewModelWithUnderlying<T> implements ViewModel {

    protected T underlying;

    @Override
    public String viewModelMemento() {
        return mementoService.viewModelMemento(this);
    }

    @Override
    public void viewModelInit(final String memento) {
        this.underlying = (T) mementoService.viewModelInit(memento);
    }

    public String title() {
        return container.titleOf(underlying);
    }

    @Override
    public String toString() {
        return underlying != null? underlying.toString(): "(no underlying)";
    }


    @Inject
    protected DomainObjectContainer container;

    @Inject
    protected AbbreviatingMementoService mementoService;
}
