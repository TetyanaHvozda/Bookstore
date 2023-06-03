package at.spengergasse.company.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "AUTHOR")
public class Author extends AbstractEntity{

    //public static final String[] GRID_COLS = new String[]{"firstName", "lastName", "book"};
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_book")*/

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> books;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }

    //COMPARE TO


    @Override
    public int compareTo(AbstractEntity o) {
        if(o == null)
            return 1;
        if(!(o instanceof Author author))
            return 1;

        return lastName.compareTo(author.getLastName());
    }

    //TO STRING

    @Override
    public String toString() {
        return super.toString() +"Author{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", book=" + books +
                '}';
    }
}
