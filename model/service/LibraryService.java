package at.spengergasse.company.model.service;

import at.spengergasse.company.model.entity.Author;
import at.spengergasse.company.model.entity.Book;
import at.spengergasse.company.model.entity.Library;
import at.spengergasse.company.model.repository.AuthorRepository;
import at.spengergasse.company.model.repository.BookRepository;
import at.spengergasse.company.model.repository.LibraryRepository;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class LibraryService {
    private final BookRepository bookRepo;
    private final LibraryRepository libraryRepo;

    private final AuthorRepository authorRepo;


    public LibraryService(BookRepository bookRepo, LibraryRepository libraryRepo, AuthorRepository authorRepo) {
        this.bookRepo = bookRepo;
        this.libraryRepo = libraryRepo;
        this.authorRepo = authorRepo;

    }

/*    public void authenticate(String username, String password) throws LibraryException {
        User user = userRepo.getById(username);
        if (user != null && user.checkPassword(password) ) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            //createRoutes(user.getRole());
        } else {
            throw new LibraryException("");
        }
    }*/

    //BOOK
    public void update(Book book){

        bookRepo.save(book);
    }

    public void delete(Book book){
        book.setLibrary(null);
        bookRepo.delete(book);
    }

    public Stream<Book> findBooksByLibraryId(Long libraryId, Query<Book, Void> query){
        return bookRepo.findByLibraryId(
                libraryId,
                PageRequest.of(
                        query.getPage(),
                        query.getPageSize(),
                        VaadinSpringDataHelpers.toSpringDataSort(query)
                )
        ).stream();
    }

    public Stream<Book> searchBookById(String keyword, Query<Book, Void> query) {

        return bookRepo.searchById("%" + keyword.toLowerCase() + "%",
                toPageable(query)).stream();
    }

    public Stream<Book> findAvailableBooks(Query<Book, Void> query) {

        return bookRepo.findAvailable(toPageable(query)).stream();
    }

    public Stream<Book> findAvailableBooks(String keyword, Query<Book, Void> query) {

        return bookRepo.searchAvailable("%" + keyword.toLowerCase() + "%",
                toPageable(query)).stream();
    }
    public Stream<Book> findPublishedBookById(Query<Book, Void> query) {

        return bookRepo.findById(toPageable(query)).stream();
    }
    public Optional<Book> findBookById(Long bid) {

    return bookRepo.findById(bid);
}



    private <T,F> Pageable toPageable(Query<T, F> query) {
        return PageRequest.of(
                query.getPage(),
                query.getPageSize(),
                VaadinSpringDataHelpers.toSpringDataSort(query)
        );
    }

    //LIBRARY
    public Optional<Library> findLibraryById(Long libraryId){
        if (libraryId == null)
            return Optional.empty();
        return libraryRepo.findById(libraryId);
    }

    public Stream<Library> findAllDepartments(Query<Library, String> query){
        return libraryRepo.findAll(toPageable(query)).stream();
    }

    public void update(Library library){

        libraryRepo.save(library);
    }

    public int add(Long libraryId, Collection<Book> books) {
        int counter = 0;

        Library library = libraryRepo.getReferenceById( libraryId );

        for (Book book : books) {
            try {
                book.setLibrary( library );
                bookRepo.save(book);
                counter++;
            } catch (Exception e) {
                log.error("Error add {}, {}", book, e.getMessage(), e);
            }
        }
        return counter;
    }


    //AUTHOR
    public void updateAuthor(Author author){
        authorRepo.save(author);
    }
    public Stream<Author> findAuthors(Query<Author, Void> query){
        return authorRepo.findAll(toPageable(query)).stream();
    }

    public void addAuthor(Long bookId, Set<Author> authors) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        for (Author author : authors) {
            author.addBook(book);  // Associate the book with the author
            authorRepo.save(author);  // Save the author with the updated book association
        }
        bookRepo.save(book);  // Save the updated book with the associated authors
    }



}
