package pl.kurs.peopleapp.repositories;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import pl.kurs.peopleapp.datatypes.Employment;
import pl.kurs.peopleapp.dtos.EmploymentHistoryCount;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {

    List<Employment> findByEmployeePesel(String employeePesel);

    @Query("SELECT new pl.kurs.peopleapp.dtos.EmploymentHistoryCount(e.employee.pesel, COUNT(e)) "
            + "FROM Employment e "
            + "WHERE e.employee.pesel IN :pesels "
            + "GROUP BY e.employee.pesel")
    @QueryHints({@QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_FETCH_SIZE, value = "5000")})
    Stream<EmploymentHistoryCount> streamCountByEmployeePesels(@Param("pesels") Set<String> pesels);
}
