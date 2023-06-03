package at.spengergasse.company.components;

import at.spengergasse.company.components.grid.BookCardList;
import at.spengergasse.company.model.entity.Book;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.vaadin.lineawesome.LineAwesomeIcon.SEARCH_SOLID;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BookDialog  extends Dialog {
    private final TextField searchField = new TextField();
    private final H5 footerInfo = new H5("0 selected");
    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel());
    private final Button select = UIFactory.btnPrimary("Save");
    private final BookCardList cardList;

    //CONSTRUCTOR
    public BookDialog(BookCardList cardList){
        this.cardList = cardList;
    }

    //INIT
    public void initUI(String title, ComponentEventListener<ClickEvent<Button>> listener){
        setHeaderTitle(title);
        addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        setHeight("90vh");
        setMaxWidth("1024px");
        setMinWidth("70vw");

        select.addClickListener((buttonClickEvent -> {
            if(isOpened())
                close();
        }));


        searchField.setVisible(false);
        searchField.setWidth("80%");
        searchField.setPlaceholder("Search by title or description");
        searchField.setPrefixComponent(SEARCH_SOLID.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.getStyle().set("margin-left", "10%");
        searchField.getStyle().set("margin-right", "10%");
        searchField.getStyle().set("padding-bottom", "10px");

        //LISTENER
        cardList.addSelectionListener(this::onSelect);
        cardList.setHeightFull();

        select.addClickListener(listener);

        add(searchField);
        add(cardList);
        getFooter().add(UIFactory.footerPanel(footerInfo, select, cancel));
    }

    //GETTER SETTER
    public void setItems(CallbackDataProvider.FetchCallback<Book, Void> fetchCallback){
        cardList.setItems(fetchCallback);
    }

    public Set<Book> getSelectedItems(){
        return cardList.getSelectedItems();
    }

    //ACTIONS
    public void onCancel(){
        cardList.deselectAll();
        if(isOpened())
            close();
    }

    public void onShow(){
        if(!isOpened())
            open();
    }

    private void onSelect(SelectionEvent<Grid<Book>, Book> event) {
        int size = event.getAllSelectedItems().size();
        footerInfo.setText( size + " selected" );
    }


    public void addKeywordChangeListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> listener){
        searchField.setVisible(true);
        searchField.addValueChangeListener(listener);
    }

}
