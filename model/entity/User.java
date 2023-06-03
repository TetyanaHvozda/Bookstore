package at.spengergasse.company.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    @Column(name = "first_name")
    private String firstName;
    @NotBlank
    @NotNull
    @Column(name = "last_name")
    private String lastName;
    @NotBlank
    @NotNull
    @Column(name = "username")
    private String username;
    @NotBlank
    @NotNull
    @Column(name = "password")
    private String password;
    @Column(name = "imageURL")
    private String imageURL;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<Book> booksPublished = new HashSet<>();
/*    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<Book> booksSaved = new HashSet<>();*/

/*    public User(String firstName, String lastName, String username, String password, String imageURL) {
        setFirstName(firstName);
        setLastName(lastName);
        setUsername(username);
        setPassword(password);
        setImageURL(imageURL);
    }*/

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Set<Book> getBooksPublished() {
        return new HashSet<>(booksPublished);
    }

    public void setBooksPublished(Set<Book> booksPublished) {
        this.booksPublished = booksPublished;
    }
}
