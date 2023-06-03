package at.spengergasse.company.model.repository;

import at.spengergasse.company.model.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LibraryRepository  extends JpaRepository<Library, Long>, JpaSpecificationExecutor<Library> {
}
