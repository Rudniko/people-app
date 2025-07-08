package pl.kurs.peopleapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.kurs.peopleapp.datatypes.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,String>, JpaSpecificationExecutor<Person> {

    Optional<Person> findByPesel(String pesel);


}
