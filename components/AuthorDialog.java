package at.spengergasse.company.components;

import at.spengergasse.company.components.grid.AuthorCardList;
import at.spengergasse.company.components.grid.BookCardList;
import at.spengergasse.company.model.entity.Author;
import at.spengergasse.company.model.entity.Book;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.grid.Grid;
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
public class AuthorDialog  extends Dialog {

    private final Button select = UIFactory.btnPrimary("Save");
    private final AuthorCardList cardList;

    //CONSTRUCTOR
    public AuthorDialog(AuthorCardList cardList){
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



        //LISTENER
        cardList.addSelectionListener(this::onSelect);
        cardList.setHeightFull();

        select.addClickListener(listener);

        add(cardList);
        getFooter().add(UIFactory.footerPanel(select));
    }

    //GETTER SETTER
    public void setItems(CallbackDataProvider.FetchCallback<Author, Void> fetchCallback){
        cardList.setItems(fetchCallback);
    }

    public Set<Author> getSelectedItems(){
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

    private void onSelect(SelectionEvent<Grid<Author>, Author> event) {
        int size = event.getAllSelectedItems().size();
    }
}
