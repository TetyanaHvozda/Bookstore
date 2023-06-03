package at.spengergasse.company.views.book;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.AuthorDialog;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.components.form.BookForm;
import at.spengergasse.company.model.entity.Author;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.service.LibraryService;
import at.spengergasse.company.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.hibernate.Hibernate;

import java.util.Set;

@Route(value = "publish", layout = MainLayout.class)
@RoutePrefix("book")
public class BookCreateView extends VerticalLayout {

    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel());
    private final Button save = UIFactory.btn("Save", e -> onSave());
    private final Button publish = UIFactory.btn("Publish", e -> onPublish());
    private final Button addAuthorDialog = UIFactory.btn("Add Author", e -> onDialogShow());


    private final BookForm bookForm;
    private final LibraryService service;
    private final AuthorDialog authorDialog;

    public BookCreateView(LibraryService service, BookForm bookForm, AuthorDialog authorDialog) {
        this.service = service;
        this.bookForm = bookForm;
        this.authorDialog = authorDialog;
        initUI();
    }

    private void initUI() {
        addClassName("page-view");
        Component header = UIFactory.headerActionPanel("Online Bookstore", "Publish your book here.");
        bookForm.setBook(new Book());

        add(header);
        add(bookForm);
        add(addAuthorDialog);
        authorDialog.initUI("Choose author(s)", e -> onDialogAdd());
        add(UIFactory.buttonLayout(save, cancel, publish));

    }

    //ACTIONS
    private void onCancel() {
        bookForm.refresh();
        getUI().ifPresent( ui -> ui.getPage().getHistory().back());
    }
    private void onSave() {
        //die Idee hier ist ein Buch als Entwurf zu speichern damit nur der User kann sehen
        //To Do: User Profile implementieren, "Meine Buecher" Seite implementieren
    }
    private void onPublish() {
        if(bookForm.isValid() ) {
            Book book = bookForm.getBook();
            Set<Author> selection = authorDialog.getSelectedItems();

            service.update(book);
            // Explicitly initialize the authors collection
            Hibernate.initialize(book.getAuthors());

            service.addAuthor(book.getId(), selection);
            for (Author author : selection) {
                service.updateAuthor(author);
            }
            Application.info(book.getTitle() + " was successfully published.");
            bookForm.refresh();
        }else {
            Application.warn("Please check the data you have provided.");
        }
    }

    private void onDialogShow(){
        authorDialog.setItems(service::findAuthors);
        authorDialog.onShow();
    }

    private void onDialogAdd(){
        Set<Author> selection = authorDialog.getSelectedItems();
/*        int size = selection.size();
        int added = service.addAuthor(bookForm.getBook().getId(), selection);*/
        Grid<Author> grid = new Grid<>(Author.class, false);
        grid.addColumn(Author::getFirstName).setHeader("Author's First name");
        grid.addColumn(Author::getLastName).setHeader("Author's Last name");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(selection);
        add(grid);
    }
}
