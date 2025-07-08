package pl.kurs.peopleapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.peopleapp.datatypes.Pensioner;

import java.util.Optional;

public interface PensionerRepository extends JpaRepository<Pensioner, String> {

    Optional<Pensioner> findByPesel(String pesel);
}
