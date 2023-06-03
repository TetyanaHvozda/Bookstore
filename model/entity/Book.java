package at.spengergasse.company.model.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "BOOK")
public class Book extends AbstractEntity{

    public static final String[] GRID_COLS = new String[]{"title", "category"};
    @NotNull
    @NotBlank
    @Column(name="title")
    private String title;

    @NotBlank
    @Column(name = "description")
    private String description;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "published_on")
    private LocalDate dateOfPublication;

    @NotNull
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "cover")
    private String cover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_library")
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user")
    private User users;

    @ManyToMany(fetch = FetchType.EAGER)
    //creates a join table (Schnittstelle zw. Book und Author)
    @JoinTable(name="book_author", joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name="author_id"))
    //@OneToMany(mappedBy="book", cascade=CascadeType.ALL, orphanRemoval=false, fetch=FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    //GETTER/SETTER

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(LocalDate dateOfPublication) {

        this.dateOfPublication = dateOfPublication;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public User getUser() {
        return users;
    }

    public void setUser(User user) {
        this.users = user;
    }

    public static void getAuthorFirstName(Book book) {
        book.getAuthors().stream().map(Author::getFirstName).collect(Collectors.joining(", "));
    }

    // -- ADD ATHOURS

    public void addAuthors(Author author){
        authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthors(Author author){
        authors.remove(author);
        //authors.setB
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public int compareTo(AbstractEntity o) {
        if(o == null)
            return 1;
        if(!(o instanceof Book book))
            return 1;
        return title.compareTo(book.getTitle());
    }

    // TO STRING

    @Override
    public String toString() {
        return super.toString() +
                ", " + title +
                ", " + description +
                ", " + dateOfPublication +
                //", " + isbn +
                ", " + category;
    }
}
