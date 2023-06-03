package at.spengergasse.company.components.grid;
import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.model.entity.Author;
import at.spengergasse.company.model.entity.Book;
import com.vaadin.flow.component.grid.Grid;

import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthorCardList extends Grid<Author> {
    @PostConstruct
    private void initUI() {
        setSizeFull();
        setHeight("100%");
        setSizeFull();

        this.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        this.addComponentColumn(this::createCard);
        this.setSelectionMode(Grid.SelectionMode.MULTI);
        this.addItemClickListener(this::onItemClick);
        this.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private HorizontalLayout createCard(Author author) {
        String firstName = author.getFirstName();
        String lastName = author.getLastName();

        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.addClassName("card");
        cardLayout.setAlignItems( FlexComponent.Alignment.CENTER );

        Span fnContainer = new Span( firstName );
        fnContainer.getStyle().set("font-size", "var(--lumo-font-size-s)");
        fnContainer.getStyle().set("color", "var(--lumo-secondary-text-color)");
        Span lnContainer = new Span( lastName );
        lnContainer.getStyle().set("font-weight", "bold");

        cardLayout.add( fnContainer, lnContainer);
        cardLayout.setPadding(true);
        cardLayout.setSpacing(true);
        cardLayout.setMargin(false);

        return cardLayout;
    }


    //  ACTIONS

    private void onItemClick(ItemClickEvent<Author> event) {
        this.select( event.getItem() );
    }
}
