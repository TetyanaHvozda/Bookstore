package at.spengergasse.company.components.form;

import at.spengergasse.company.Application;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.model.entity.Author;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.entity.Category;
import com.vaadin.flow.component.ClickEvent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BookForm extends Div {
    private TextField title = new TextField("Title");
    private TextField description = new TextField("Description");
    private List<HorizontalLayout> authors = new ArrayList<>();
    private TextField isbn = new TextField("isbn");
    private ComboBox<Category> category = new ComboBox<>("Category");
    private TextField cover = new TextField("Cover");
    private final Image image = new Image();
    private final DatePicker dateOfPublication = new DatePicker("Published on");

    private final Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private Upload upload;
    private Button add = new Button("Add Author", this::onAdd);


    @PostConstruct
    private void init(){

        category.setItems(Category.values());

        //UPLOAD
        FileBuffer fileBuffer = new FileBuffer(fileName -> new File( Application.UPLOAD_PATH, fileName).getCanonicalFile());
        upload = new Upload(fileBuffer);
        upload.addSucceededListener(this::onUploadSuccess);
        upload.setWidthFull();

        image.setMaxHeight("100px");
        image.getStyle().set("margin-right", "10px");

        HorizontalLayout coverLayout = new HorizontalLayout(image, upload);
        coverLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        coverLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        cover.setEnabled(false);

/*        HorizontalLayout authorLayout = new HorizontalLayout(firstName, lastName, add);
        authorLayout.setAlignItems(FlexComponent.Alignment.BASELINE);*/

        //FORM
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                title, description,
                category, isbn,
                dateOfPublication
        );

        add(coverLayout);
        add(new Hr());
        add(formLayout);
        //add(authorLayout);
        //add(add);

        //BINDER
        binder.bindInstanceFields(this);
    }


    //GETTER/SETTER
    public void setBook(Book book){
        if (book == null){
            Application.error("Error: Book is null");
            return;
        }
        binder.setBean(book);
        setImage(book.getTitle(), book.getCover());
        // Clear existing author fields
        authors.clear();

        for (Author author : book.getAuthors()) {
            TextField firstNameField = new TextField("Author's First Name");
            TextField lastNameField = new TextField("Author's Last Name");
            binder.forField(firstNameField)
                    .bind(
                            (ValueProvider<Book, String>) item -> author.getFirstName(),
                            (Setter<Book, String>) (item, value) -> author.setFirstName(value)
                    );
            binder.forField(lastNameField)
                    .bind(
                            (ValueProvider<Book, String>) item -> author.getLastName(),
                            (Setter<Book, String>) (item, value) -> author.setLastName(value)
                    );

            // Add the author fields to the layout
            HorizontalLayout authorLayout = new HorizontalLayout(firstNameField, lastNameField);
            authorLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
            authors.add(authorLayout);
            add(authorLayout);
        }
    }


    private void setImage(String name, String imageSrc){
        UIFactory.loadImageSrcBook(name, imageSrc, image);
        cover.setValue(imageSrc);
    }


    public Book getBook(){

        return binder.getBean();

    }


    public boolean isValid(){
        binder.validate();
        return binder.isValid();
    }


    //ACTIONS
    public void refresh(){
        binder.readBean(new Book());

    }
    private void onUploadSuccess(SucceededEvent succeededEvent) {
        String fileName = succeededEvent.getFileName();
        String imageSrc = Application.UPLOAD_PATH + fileName;
        String name = title.getValue();
        setImage(name, imageSrc);

    }

    private HorizontalLayout addAuthorField() {
        TextField firstName = new TextField("Author's First Name");
        TextField lastName = new TextField("Author's Last Name");


        HorizontalLayout authorLayout = new HorizontalLayout(firstName, lastName, add);
        authorLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

        return authorLayout;

    }


    private void onAdd(ClickEvent<Button> event) {

        HorizontalLayout authorLayout = addAuthorField();
        add(authorLayout);

    }

    public void setReadOnly(boolean readOnly) {
        title.setReadOnly(readOnly);
        description.setReadOnly(readOnly);
        category.setReadOnly(readOnly);
        dateOfPublication.setReadOnly(readOnly);
        isbn.setReadOnly(readOnly);
        upload.setVisible(false);

        authors.forEach(authorLayout -> {
            authorLayout.getChildren().forEach(component -> {
                if (component instanceof TextField) {
                    ((TextField) component).setReadOnly(readOnly);
                }
            });
        });
    }

}
