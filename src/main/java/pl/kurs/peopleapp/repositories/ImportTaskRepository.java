package pl.kurs.peopleapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.peopleapp.imports.datatypes.ImportTaskStatus;
import pl.kurs.peopleapp.imports.datatypes.ImportTask;


public interface ImportTaskRepository extends JpaRepository<ImportTask, Long> {
    boolean existsByStatus(ImportTaskStatus status);

}
