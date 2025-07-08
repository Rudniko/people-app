package pl.kurs.peopleapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.datatypes.Student;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class StudentSpecification implements TypeSearchSpecification {
    @Override
    public boolean supports(String type) {
        return type == null || "student".equalsIgnoreCase(type);
    }

    @Override
    public Class<? extends Person> getTargetClass() {
        return Student.class;
    }

    @Override
    public Set<String> handledSortProperties() {
        return Set.of("universityName", "studyField", "currentStudyYear", "scholarship");
    }

    @Override
    public Set<String> handledKeys() {
        return Stream.concat(TypeSearchSpecification.CORE_KEYS.stream(),
                Stream.of(
                        "universityName",
                        "studyField",
                        "currentStudyYearFrom", "currentStudyYearTo",
                        "scholarshipFrom", "scholarshipTo")
        ).collect(Collectors.toSet());
    }

    @Override
    public Specification<Person> toSpecification(Map<String, String> filters) {
        return Specification.allOf(
                PersonSpecificationUtils.isLike("universityName", filters.get("universityName")),
                PersonSpecificationUtils.isLike("studyField", filters.get("studyField")),
                PersonSpecificationUtils.isRangeBetween("currentStudyYear",
                        PersonSpecificationUtils.toInteger(filters.get("currentStudyYearFrom")),
                        PersonSpecificationUtils.toInteger(filters.get("currentStudyYearTo"))),
                PersonSpecificationUtils.isRangeBetween("scholarship",
                        PersonSpecificationUtils.toBigDecimal(filters.get("scholarshipFrom")),
                        PersonSpecificationUtils.toBigDecimal(filters.get("scholarshipTo"))
                ));
    }
}
