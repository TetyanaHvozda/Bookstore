package at.spengergasse.company.components.grid;

import at.spengergasse.company.components.UIFactory;
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
public class BookCardList extends Grid<Book> {
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
        private HorizontalLayout createCard(Book book) {
            String cover = book.getCover();
            String title = book.getTitle();
            String description = book.getDescription();
            String category = book.getCategory().name();

            HorizontalLayout cardLayout = new HorizontalLayout();
            cardLayout.setAlignItems( FlexComponent.Alignment.CENTER );

            Image coverImg = UIFactory.loadImage( title, cover );

            Span descContainer = new Span( description );
            descContainer.getStyle().set("font-size", "var(--lumo-font-size-s)");
            descContainer.getStyle().set("color", "var(--lumo-secondary-text-color)");
            Span titleContainer = new Span( title );
            titleContainer.getStyle().set("font-weight", "bold");

            Span catContainer = new Span( category );
            catContainer.setHeightFull();
            catContainer.getElement().getThemeList().add("badge success");
            cardLayout.setAlignSelf( FlexComponent.Alignment.CENTER, catContainer );

            VerticalLayout dataLayout = new VerticalLayout( titleContainer);
            dataLayout.setSpacing(false);
            dataLayout.setPadding(false);
            dataLayout.setMargin(true);

            cardLayout.add( coverImg, dataLayout, catContainer);
            cardLayout.setPadding(false);
            cardLayout.setSpacing(false);
            cardLayout.setMargin(false);

            return cardLayout;
        }


    //  ACTIONS

        private void onItemClick(ItemClickEvent<Book> event) {
            this.select( event.getItem() );
        }

}
