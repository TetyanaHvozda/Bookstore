package at.spengergasse.company.model.repository;

import at.spengergasse.company.model.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository  extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    @Query(value = "select a from Author a ")
    Page<Author> findAll(Pageable pageable);
}
