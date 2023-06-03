package at.spengergasse.company.views.library;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.components.grid.BookGrid;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.entity.Library;
import at.spengergasse.company.model.service.LibraryService;
import at.spengergasse.company.views.MainLayout;
import at.spengergasse.company.views.book.BookCreateView;
import at.spengergasse.company.views.book.BookEditView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import static org.vaadin.lineawesome.LineAwesomeIcon.BOOK_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.CARET_LEFT_SOLID;

@Route(value = "books/:id", layout = MainLayout.class)
@RoutePrefix(value= "libraries")
public class LibraryBookListView extends VerticalLayout implements BeforeEnterObserver {

    private static final Runnable NO_DATA = () -> Application.warn("No data found.");
    private final Button publishBook = UIFactory.btnPrimary("Publish", BOOK_SOLID.create(), e-> onPublish());
    private final Button back = UIFactory.btnPrimary("Back", CARET_LEFT_SOLID.create(), e -> onBack());
    private final BookGrid grid = new BookGrid(this :: onView, this :: onEdit, this :: onDelete);

    private final LibraryService service;

    private Library library;

    //CONSTRUCTOR
    public LibraryBookListView(LibraryService service) {
        this.service = service;
    }

    //READ PARAMS

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().getLong("id").ifPresentOrElse(
                this::setLibraryId,
                NO_DATA
        );
    }


    //INIT
    private void initUI(){
        addClassName("page-view");
        setSizeFull();

        Span description = new Span(new Text("Find the book you are interested in"));
        Component header = UIFactory.headerActionPanel("Books", description, back, publishBook);

        add(header);
        add(grid);
    }

    private void setLibraryId(Long libraryId) {
        service.findLibraryById(libraryId).ifPresentOrElse(l ->{
            library = l;
            initUI();
            grid.setItems(q -> service.findBooksByLibraryId(libraryId, q));
        }, NO_DATA);
    }

    //ACTIONS
    private void onEdit(Book book) {
        if (book == null){
            Application.error("Employee must not be null.");
            return;
        }
        getUI().ifPresent(ui -> ui.navigate(BookEditView.class, UIFactory.paramOf("id", book.getId())));
    }

    private void onDelete(Book book) {
        try {
            service.delete(book);
            grid.getDataProvider().refreshAll();

            Application.info("Book "+ book.getTitle() + " was deleted.");
        }catch(Exception e){
            Application.error(e.getMessage());
        }
    }

    private void onView(Book book) {
    }

    private void onPublish() {
        getUI().ifPresent(ui -> ui.navigate(BookCreateView.class));
    }

    private void onBack() {
        getUI().ifPresent(ui -> ui.navigate(LibraryListView.class, UIFactory.paramOf("id", library.getId())));
    }

    public String getPageTitle(){
        if(library == null)
            return "Library | Books";
        return library.getName() + " | Books";
    }
}
