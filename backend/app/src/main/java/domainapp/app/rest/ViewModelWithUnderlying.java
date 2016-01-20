package domainapp.app.rest;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.ViewModel;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService;
import org.apache.isis.applib.services.urlencoding.UrlEncodingService;

public abstract class ViewModelWithUnderlying<T> implements ViewModel {

    protected T underlying;


    @Override
    public String viewModelMemento() {
        final Bookmark bookmark = bookmarkService.bookmarkFor(underlying);
        final String str = bookmark.toString();
        return urlEncodingService.encode(str);
    }

    @Override
    public void viewModelInit(final String memento) {
        final String decoded = urlEncodingService.decode(memento);
        this.underlying = (T) bookmarkService.lookup(new Bookmark(decoded));
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
    protected BookmarkService bookmarkService;

    @Inject
    protected UrlEncodingService urlEncodingService;
}
