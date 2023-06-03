package at.spengergasse.company.views.book;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.components.form.BookForm;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.service.LibraryService;
import at.spengergasse.company.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;

import static org.apache.naming.ContextAccessController.setReadOnly;


@PageTitle(value="Book Details")
@Route(value=":id", layout = MainLayout.class)
@RoutePrefix(value="book")
public class BookDetailsView extends Div implements BeforeEnterObserver {
    private final Button back = UIFactory.btn("Cancel", e -> onBack());

    private final LibraryService service;
    private final BookForm bookForm;


    public BookDetailsView(LibraryService service, BookForm bookForm) {
        this.service = service;
        this.bookForm = bookForm;
        initUI();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        event.getRouteParameters().getLong("id").ifPresent(this::setParameter);
    }

    private void setParameter(Long bookId) {
        service.findBookById(bookId).ifPresentOrElse(
                book -> {
                    bookForm.setBook(book);
                    bookForm.setReadOnly(true);
                },
                () -> {
                    Application.error("No book found"); onBack();
                }
        );
    }

    private void initUI() {
        addClassName("page-view");
        setSizeFull();
        Component header = UIFactory.headerActionPanel("Online Bookstore", "Details of the book.");
        bookForm.setBook(new Book());

        add(header);
        add(bookForm);
        add(UIFactory.buttonLayout(back));


    }

    private void onBack() {
        getUI().ifPresent( ui -> ui.getPage().getHistory().back());
    }
}
