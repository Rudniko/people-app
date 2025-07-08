package pl.kurs.peopleapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.peopleapp.datatypes.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByPesel(String pesel);
}
