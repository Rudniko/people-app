package pl.kurs.peopleapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.peopleapp.datatypes.Pensioner;
import pl.kurs.peopleapp.datatypes.Person;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PensionerSpecification implements TypeSearchSpecification {
    @Override
    public boolean supports(String type) {
        return type == null || "pensioner".equalsIgnoreCase(type);
    }

    @Override
    public Class<? extends Person> getTargetClass() {
        return Pensioner.class;
    }

    @Override
    public Set<String> handledSortProperties() {
        return Set.of("pension", "yearsWorked");
    }

    @Override
    public Set<String> handledKeys() {
        return Stream.concat(TypeSearchSpecification.CORE_KEYS.stream(),
                Stream.of(
                        "pensionFrom", "pensionTo",
                        "yearsWorkedFrom", "yearsWorkedTo")
        ).collect(Collectors.toSet());
    }

    @Override
    public Specification<Person> toSpecification(Map<String, String> filters) {
        return Specification.allOf(
                PersonSpecificationUtils.isRangeBetween("pension",
                        PersonSpecificationUtils.toBigDecimal(filters.get("pensionFrom")),
                        PersonSpecificationUtils.toBigDecimal(filters.get("pensionTo"))
                ),
                PersonSpecificationUtils.isRangeBetween("yearsWorked",
                        PersonSpecificationUtils.toInteger(filters.get("yearsWorkedFrom")),
                        PersonSpecificationUtils.toInteger(filters.get("yearsWorkedTo"))
                )
        );
    }
}
