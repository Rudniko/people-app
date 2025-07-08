package pl.kurs.peopleapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Person;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EmployeeSpecification implements TypeSearchSpecification {

    @Override
    public boolean supports(String type) {
        return type == null || "employee".equalsIgnoreCase(type);
    }

    @Override
    public Class<? extends Person> getTargetClass() {
        return Employee.class;
    }

    @Override
    public Set<String> handledSortProperties() {
        return Set.of("currentProfession", "currentSalary", "startOfEmployment", "numberOfProfessions");
    }

    @Override
    public Set<String> handledKeys() {
        return Stream.concat(TypeSearchSpecification.CORE_KEYS.stream(),
                Stream.of(
                        "currentProfession",
                        "currentSalaryFrom", "currentSalaryTo",
                        "startOfEmploymentFrom", "startOfEmploymentTo",
                        "numberOfProfessionsFrom", "numberOfProfessionsTo")
        ).collect(Collectors.toSet());
    }

    @Override
    public Specification<Person> toSpecification(Map<String, String> filters) {
        return Specification.allOf(
                PersonSpecificationUtils.isLike("currentProfession", filters.get("currentProfession")),
                PersonSpecificationUtils.isRangeBetween("currentSalary",
                        PersonSpecificationUtils.toBigDecimal(filters.get("currentSalaryFrom")),
                        PersonSpecificationUtils.toBigDecimal(filters.get("currentSalaryTo"))
                ),
                PersonSpecificationUtils.isRangeBetween("startOfEmployment",
                        PersonSpecificationUtils.toLocalDate(filters.get("startOfEmploymentFrom")),
                        PersonSpecificationUtils.toLocalDate(filters.get("startOfEmploymentTo"))
                ),
                PersonSpecificationUtils.hasProfessionsBetween(
                        PersonSpecificationUtils.toInteger(filters.get("numberOfProfessionsFrom")),
                        PersonSpecificationUtils.toInteger(filters.get("numberOfProfessionsTo"))
                )
        );
    }
}
