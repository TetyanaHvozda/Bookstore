package at.spengergasse.company.components.grid;

import at.spengergasse.company.components.UIFactory;
import at.spengergasse.company.model.entity.Book;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.function.Consumer;

import static com.vaadin.flow.component.icon.VaadinIcon.SELECT;
import static com.vaadin.flow.component.icon.VaadinIcon.TRASH;
import static com.vaadin.flow.theme.lumo.LumoIcon.EDIT;


public class BookGrid extends Grid<Book> {
    public BookGrid(Consumer<Book> onView, Consumer<Book> onEdit, Consumer<Book> onDelete){
        super(Book.class, false);
        initUI(onView, onEdit, onDelete);
    }

    private void initUI(Consumer<Book> onView, Consumer<Book> onEdit, Consumer<Book> onDelete) {
        this.setPageSize(100);
        UIFactory.tableCoverColumn(this);
        this.addColumns(Book.GRID_COLS);
        UIFactory.tableAuthorColumn(this);
        this.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        this.getColumns().forEach(column -> column.setAutoWidth(true)); // Enable auto-sizing for all columns

        UIFactory.tableActionColumn(this, book -> {
            Button view = UIFactory.tableBtn(SELECT.create(), e -> onView.accept(book));
            Button edit = UIFactory.tableBtn(EDIT.create(), e -> onEdit.accept(book));
            Button delete = UIFactory.tableBtn(TRASH.create(), e -> onDelete.accept(book));
            return UIFactory.tableColumnAction(view, edit, delete);
        });
    }
}
