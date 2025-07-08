package pl.kurs.peopleapp.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.peopleapp.datatypes.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @EntityGraph(attributePaths = "employments")
    Optional<Employee> findByPesel(String pesel);


}
