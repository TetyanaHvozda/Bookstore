package at.spengergasse.company.views.book;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.components.form.BookForm;
import at.spengergasse.company.model.entity.Author;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.service.LibraryService;
import at.spengergasse.company.views.MainLayout;
import at.spengergasse.company.views.library.OnlineBookListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;

@PageTitle(value="Book edit")
@Route(value=":id", layout = MainLayout.class)
@RoutePrefix(value="books")
public class BookEditView extends Div implements BeforeEnterObserver {

    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel());
    private final Button save = UIFactory.btn("Save", e -> onSave());


    private final LibraryService service;
    private final BookForm bookForm;


    public BookEditView(LibraryService service, BookForm bookForm) {
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
                bookForm::setBook,
                () -> {
                    Application.error("No book found"); onCancel();
                }
        );
    }

    private void initUI() {
        addClassName("page-view");
        setSizeFull();
        Component header = UIFactory.headerActionPanel("Online Bookstore", "Publish your book here.");
        bookForm.setBook(new Book());

        add(header);
        add(bookForm);
        add(UIFactory.buttonLayout(save, cancel));

    }


    private void onSave() {
        if(bookForm.isValid()){
            Book book = bookForm.getBook();

            service.update(book);
            // Save the authors
            for (Author author : book.getAuthors()) {
                service.updateAuthor(author);
            }

            Application.info(book.getTitle() + " was successfully updated.");
            getUI().ifPresent( ui -> ui.navigate(OnlineBookListView.class));
        }else {
            Application.warn("Invalid data. Please check form data.");
        }
    }


    private void onCancel() {
        getUI().ifPresent( ui -> ui.getPage().getHistory().back());
    }
}
