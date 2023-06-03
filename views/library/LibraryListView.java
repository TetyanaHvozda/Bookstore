package at.spengergasse.company.views.library;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.BookDialog;
import at.spengergasse.company.components.NavTitle;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.entity.Library;
import at.spengergasse.company.model.service.LibraryService;
import at.spengergasse.company.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;

import java.util.Set;

import static org.vaadin.lineawesome.LineAwesomeIcon.BOOK_OPEN_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.BOOK_SOLID;


@NavTitle("Departments")
@PageTitle("Departments")
@Route(value=":id?", layout= MainLayout.class)
@RoutePrefix(value= "departments")
public class LibraryListView extends VerticalLayout implements BeforeEnterObserver {

    private final ComboBox<Library> librarySelection = new ComboBox<>("Bookstore Department");
    private final TextField address = new TextField("Address");
    private final TextField name = new TextField("Name");

    private final Section libraryFormContainer = new Section();

    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel());
    private final Button save = UIFactory.btn("Save", e -> onSave());

    private final Button publishBookDialog = UIFactory.btnPrimary("Add a book", BOOK_SOLID.create(), e-> onPublish());
    private final Button showBooks = UIFactory.btnPrimary("All Books", BOOK_OPEN_SOLID.create(), e -> onAllBooks());

    private final Binder<Library> binder = new BeanValidationBinder<>(Library.class);

    private Library library;

    private final LibraryService service;

    private final BookDialog bookDialog;


    //CONSTRUCTOR
    public LibraryListView(LibraryService service, BookDialog bookDialog) {
        this.service = service;
        this.bookDialog = bookDialog;
        initUI();
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().getLong("id")
                .flatMap(service :: findLibraryById)
                .ifPresent(this :: setLibrary);
    }

    private void initUI() {
        addClassName("page-view");
        setSizeFull();

        //COMBO
        librarySelection.setItems(service::findAllDepartments);
        librarySelection.setItemLabelGenerator((Library :: getName));
        librarySelection.setHelperText("Choose a department");
        librarySelection.setWidthFull();
        librarySelection.addValueChangeListener(e -> onLibraryChanged(e.getValue()));

        //DEPARTMENTS SECTION, EDIT DEPARTMENTS
        FormLayout formLayout = new FormLayout();
        formLayout.add(address, 2);
        formLayout.add(name, 2);

        libraryFormContainer.add(UIFactory.headerActionPanel("Edit the department",
                "Edit the address of the department, show books available in the department or add new books that are available in the current department.",
                showBooks, publishBookDialog));
        libraryFormContainer.add(formLayout);
        libraryFormContainer.add(UIFactory.buttonLayout(save, cancel));
        libraryFormContainer.setVisible(false);
        libraryFormContainer.setSizeFull();

        add(UIFactory.headerActionPanel("Departments", "Update the departments here."));
        add(librarySelection);
        add(new Hr());
        add(libraryFormContainer);

        binder.bindInstanceFields(this);
        bookDialog.initUI("Choose books that are available at this department", e -> onDialogAdd());
    }

    //GETTER SETTER
    private void setLibrary(Library library){
        this.library = library;
        binder.setBean(library);

        boolean libraryAvailable = this.library != null;
        libraryFormContainer.setVisible(libraryAvailable);

        if (libraryAvailable && !library.equals(librarySelection.getValue())){
            librarySelection.setValue(library);
        }
    }

    private void onLibraryChanged(Library value) {
        setLibrary(value);
    }


    private void onCancel() {
        setLibrary(library);
    }
    private void onSave() {
        binder.validate();
        if(binder.isValid()){
            Library library = binder.getBean();
            service.update(library);
            Application.success(library.getName() + " updated");
        }else{
            Application.warn("An error occured.");
        }
    }
    private void onPublish() {
        if (library == null){
            Application.warn("Please choose a department");
            return;
        }

        bookDialog.setItems(service::findAvailableBooks);
        bookDialog.addKeywordChangeListener(e -> search(e.getValue()));
        bookDialog.onShow();
    }

    private void search(String keyword){
        bookDialog.setItems(q -> service.findAvailableBooks(keyword, q));
    }

    private void onDialogAdd() {
        if (library == null) {
            Application.warn("No department available!");
            return;
        }
        Set<Book> selection = bookDialog.getSelectedItems();
        int size = selection.size();
        int added = service.add( library.getId(), selection );
        Application.success(size + "/" + added + (added == 1 ? " Books added" : " Books added"));
        getUI().ifPresent(ui -> ui.navigate(
                LibraryBookListView.class,
                UIFactory.paramOf("id", library.getId()))
        );
    }
    private void onAllBooks() {
        getUI().ifPresent(ui -> ui.navigate(LibraryBookListView.class,
                UIFactory.paramOf("id", library.getId())));
    }
}
