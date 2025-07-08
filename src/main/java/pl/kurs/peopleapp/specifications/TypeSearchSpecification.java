package pl.kurs.peopleapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.peopleapp.datatypes.Person;

import java.util.Map;
import java.util.Set;

public interface TypeSearchSpecification {

    boolean supports(String type);

    Specification<Person> toSpecification(Map<String, String> filters);

    Class<? extends Person> getTargetClass();

    Set<String> handledSortProperties();

    Set<String> handledKeys();

    Set<String> CORE_KEYS = Set.of(
            "type", "pesel", "firstName", "lastName", "email",
            "heightFrom", "heightTo", "weightFrom", "weightTo",
            "gender", "ageFrom", "ageTo",
            "page", "size", "sort"
    );
}
