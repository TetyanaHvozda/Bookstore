package at.spengergasse.company.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "LIBRARY")
public class Library extends AbstractEntity {
    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    //GETTER/SETTER

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //ACTIONS Books
    public void addBook(Book book){
        books.add(book);
        book.setLibrary(this);
    }

    public void removeBook(Book book){
        books.remove(book);
        book.setLibrary(null);
    }

    public Set<Book> getBooks() {
        return new HashSet<>(books);
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    //COMPARE TO

    @Override
    public int compareTo(AbstractEntity o) {
        if (o == null)
            return 1;
        if(!(o instanceof Library library))
            return 1;
        return name.compareTo(library.getName());
    }

    //TO STRING


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        return sb.toString();
    }
}
