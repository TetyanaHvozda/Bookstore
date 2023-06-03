package at.spengergasse.company.views.library;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.NavTitle;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.components.grid.BookGrid;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.entity.Library;
import at.spengergasse.company.model.service.LibraryService;
import at.spengergasse.company.views.MainLayout;
import at.spengergasse.company.views.book.BookCreateView;

import at.spengergasse.company.views.book.BookDetailsView;
import at.spengergasse.company.views.book.BookEditView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;


import static org.vaadin.lineawesome.LineAwesomeIcon.*;

@PermitAll
@NavTitle("All books")
@PageTitle("All books")
@Route(value = "all-books", layout = MainLayout.class)
@RoutePrefix(value= "books")
public class OnlineBookListView extends VerticalLayout {

    private final Button publishBook = UIFactory.btnPrimary("Publish", BOOK_SOLID.create(), e-> onPublish());
    private final BookGrid grid = new BookGrid(this :: onView, this :: onEdit, this :: onDelete);

    private final TextField searchField = new TextField();

    private final LibraryService service;
    private Library library;


    //CONSTRUCTOR
    public OnlineBookListView(LibraryService service) {
        //this.booksAndAuthors = booksAndAuthors;
        this.service = service;
        initUI();
    }



    //INIT
    private void initUI(){
        addClassName("page-view");
        setSizeFull();


        Span description = new Span(new Text("Find the book you are interested in"));
        Component header = UIFactory.headerActionPanel("Books", description, publishBook);

        searchField.setWidth("100%");
        searchField.setPlaceholder("Search by title or description");
        searchField.setPrefixComponent(SEARCH_SOLID.create());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> reload(e.getValue()));

        grid.setItems(service :: findPublishedBookById);


        add(header);
        add(searchField);
        add(grid);
    }

    private void reload(String keyword) {
        grid.setItems(e -> service.searchBookById(keyword, e));
    }

    /*    private void setLibraryId(Long libraryId) {
            service.findLibraryById(libraryId).ifPresentOrElse(l ->{
                library = l;
                initUI();
                //grid.setItems(g -> service.findBooksByLibraryId(libraryId, g));
            }, NO_DATA);
        }*/
    private void onView(Book book) {
        if (book == null){
            Application.error("Book must not be null.");
            return;
        }
        final Long bookId = book.getId();
        getUI().ifPresent(ui -> ui.navigate(BookDetailsView.class, UIFactory.paramOf("id", bookId)));
    }

    private void onEdit(Book book) {
        if (book == null){
            Application.error("Book must not be null.");
            return;
        }
        final Long bookId = book.getId();
        getUI().ifPresent(ui -> ui.navigate(BookEditView.class, UIFactory.paramOf("id", bookId)));
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

    private void onPublish() {
        getUI().ifPresent(ui -> ui.navigate(BookCreateView.class));
        return;
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
