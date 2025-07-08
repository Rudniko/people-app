package pl.kurs.peopleapp.specifications;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.exceptions.InvalidSearchFieldException;

import java.util.*;
import java.util.stream.Collectors;

public final class MapBasedPersonSpecificationBuilder {

    public static Specification<Person> build(Map<String, String> filters, List<TypeSearchSpecification> specs, Sort sort) {

        TypeSearchSpecification chosenSubtypeSpec = validateSpecs(filters, specs);
        List<Specification<Person>> allSpecs = new ArrayList<>();

        String explicitType = filters.get("type");
        if (chosenSubtypeSpec != null) {

            String subtypeName = chosenSubtypeSpec.getTargetClass().getSimpleName().toLowerCase();
            allSpecs.add(PersonSpecificationUtils.isType(subtypeName));
            allSpecs.add(chosenSubtypeSpec.toSpecification(filters));

        } else {

            allSpecs.add(PersonSpecificationUtils.isType(explicitType));
            specs.stream()
                    .filter(s -> s.supports(explicitType))
                    .findFirst()
                    .map(s -> s.toSpecification(filters))
                    .ifPresent(allSpecs::add);
        }

        allSpecs.add(PersonSpecificationUtils.isLike("pesel",      filters.get("pesel")));
        allSpecs.add(PersonSpecificationUtils.isLike("firstName",  filters.get("firstName")));
        allSpecs.add(PersonSpecificationUtils.isLike("lastName",   filters.get("lastName")));
        allSpecs.add(PersonSpecificationUtils.isLike("email",      filters.get("email")));

        allSpecs.add(PersonSpecificationUtils.isRangeBetween("height",
                PersonSpecificationUtils.toBigDecimal(filters.get("heightFrom")),
                PersonSpecificationUtils.toBigDecimal(filters.get("heightTo"))
        ));
        allSpecs.add(PersonSpecificationUtils.isRangeBetween("weight",
                PersonSpecificationUtils.toBigDecimal(filters.get("weightFrom")),
                PersonSpecificationUtils.toBigDecimal(filters.get("weightTo"))
        ));

        allSpecs.add(PersonSpecificationUtils.isGender(filters.get("gender")));
        allSpecs.add(PersonSpecificationUtils.isAgeBetween(
                PersonSpecificationUtils.toInteger(filters.get("ageFrom")),
                PersonSpecificationUtils.toInteger(filters.get("ageTo"))
        ));

        List<Class<? extends Person>> subTypes = specs.stream()
                .map(TypeSearchSpecification::getTargetClass)
                .collect(Collectors.toList());
        Set<String> subSortKeys = specs.stream()
                .flatMap(s -> s.handledSortProperties().stream())
                .collect(Collectors.toSet());
        allSpecs.add(PersonSpecificationUtils.orderBy(sort, subTypes, subSortKeys));

        return Specification.allOf(allSpecs);
    }



    private static TypeSearchSpecification validateSpecs(Map<String, String> filters, List<TypeSearchSpecification> specs) {
        TypeSearchSpecification chosenSubtypeSpec = null;

        for (TypeSearchSpecification s : specs) {

            Set<String> extras = new HashSet<>(s.handledKeys());
            extras.removeAll(TypeSearchSpecification.CORE_KEYS);

            for (String key : extras) {
                String val = filters.get(key);
                if (val != null && !val.isBlank()) {
                    if (chosenSubtypeSpec != null && chosenSubtypeSpec != s) {
                        throw new InvalidSearchFieldException("Conflicting subtype filters: " + chosenSubtypeSpec.getTargetClass().getSimpleName()
                                + " vs " + s.getTargetClass().getSimpleName());
                    }
                    chosenSubtypeSpec = s;
                }
            }
        }
        return chosenSubtypeSpec;
    }
}

