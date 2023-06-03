package at.spengergasse.company.model.repository;

import at.spengergasse.company.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book>{

    List<Book> findByLibraryId(Long libraryId, Pageable pageable);

    @Query(value = "select b from Book b ")
    List<Book> findById(Pageable pageable);

    @Query(value = "select b from Book b left join Library lib on b.library.id=lib.id where b.library is null")
    List<Book> findAvailable(Pageable pageable);

    @Query(value = "select b from Book b left join Library lib on b.library.id=lib.id where b.library is null and (LOWER(CONCAT(b.title, ' ', b.description)) LIKE :keyword)")
    List<Book> searchAvailable(String keyword, Pageable pageable);

    @Query(value = "select b from Book b WHERE (LOWER(CONCAT(b.title, ' ', b.description)) LIKE :keyword)")
    List<Book> searchById(String keyword, Pageable pageable);
}
