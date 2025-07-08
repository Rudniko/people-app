package pl.kurs.peopleapp.specifications;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.peopleapp.datatypes.Employment;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.exceptions.InvalidFieldException;
import pl.kurs.peopleapp.exceptions.InvalidPersonTypeException;
import pl.kurs.peopleapp.exceptions.InvalidSearchFieldException;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public final class PersonSpecificationUtils {

    public static Specification<Person> isLike(String field, String value) {
        return whenPresent(value, v ->
                (root, q, cb) -> cb.like(
                        cb.lower(root.get(field)), "%" + v.toLowerCase() + "%")
        );
    }

    public static Specification<Person> isRangeBetween(String field, Comparable from, Comparable to) {
        if (from == null && to == null) {
            return alwaysTrue();
        }
        if (from == null || to == null) {
            throw new InvalidSearchFieldException("Both from and to must be provided for '" + field + "'");
        }
        return (root, q, cb) -> cb.between(root.get(field), from, to);
    }

    public static Specification<Person> isGender(String gender) {
        return whenPresent(gender, g ->
                (root, q, cb) -> cb.equal(
                        cb.function("pesel_gender", String.class, root.get("pesel")),
                        g.toUpperCase()
                )
        );
    }

    public static Specification<Person> isAgeBetween(Integer fromYears, Integer toYears) {
        if (fromYears == null && toYears == null) {
            return alwaysTrue();
        }
        if (fromYears == null || toYears == null || fromYears < 0 || toYears < 0 || fromYears > toYears) {
            throw new InvalidSearchFieldException("ageFrom ≤ ageTo and both must be specified");
        }

        LocalDate today = LocalDate.now();
        LocalDate maxBirthDate = today.minusYears(fromYears);
        LocalDate minBirthDate = today.minusYears(toYears);

        return (root, q, cb) -> cb.between(
                cb.function("pesel_birthdate", Date.class, root.get("pesel")),
                Date.valueOf(minBirthDate),
                Date.valueOf(maxBirthDate)
        );
    }

    public static Specification<Person> isType(String typeName) {
        if (typeName == null || typeName.isBlank()) {
            return alwaysTrue();
        }
        String pkg = Person.class.getPackage().getName();
        String className = capitalize(typeName);

        try {
            Class<?> cls = Class.forName(pkg + "." + className);
            if (!Person.class.isAssignableFrom(cls)) {
                throw new InvalidPersonTypeException("Unsupported type: " + typeName);
            }
            Class<? extends Person> personClass = (Class<? extends Person>) cls;
            return (root, q, cb) -> cb.equal(root.type(), personClass);

        } catch (ClassNotFoundException e) {
            throw new InvalidPersonTypeException("Unsupported type: " + typeName);
        }
    }

    public static Specification<Person> hasProfessionsBetween(Integer fromCount, Integer toCount) {
        if (fromCount == null && toCount == null) {
            return alwaysTrue();
        }
        if (fromCount == null || toCount == null) {
            throw new InvalidSearchFieldException("Both from and to must be provided for numberOfProfessions");
        }

        return (root, query, cb) -> {
            query.distinct(true);
            Subquery<Long> sq = query.subquery(Long.class);
            Root<Employment> emp = sq.from(Employment.class);
            sq.select(cb.count(emp));
            sq.where(cb.equal(emp.get("employee"), root));

            Expression<Integer> countExpr = sq.getSelection().as(Integer.class);
            return cb.between(countExpr, fromCount, toCount);
        };
    }

    public static BigDecimal toBigDecimal(String s) {
        return parseOrNull(s, BigDecimal::new);
    }

    public static Integer toInteger(String s) {
        return parseOrNull(s, Integer::parseInt);
    }

    public static LocalDate toLocalDate(String s) {
        return parseOrNull(s, LocalDate::parse);
    }

    private static <T> T parseOrNull(String s, Function<String, T> parser) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return parser.apply(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static <T> Specification<Person> whenPresent(String value, Function<String, Specification<Person>> mapper) {
        return (value == null || value.isBlank()) ? alwaysTrue() : mapper.apply(value);
    }

    private static Specification<Person> alwaysTrue() {
        return (r, q, cb) -> cb.conjunction();
    }


    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }


    public static Specification<Person> orderBy(Sort sort, List<? extends Class<? extends Person>> subTypes, Set<String> subSortKeys) {
        return (root, query, cb) -> {
            if (sort == null || !sort.isSorted()) {
                return cb.conjunction();
            }

            query.distinct(true);

            List<Order> orders = StreamSupport.stream(sort.spliterator(), false)
                    .map(o -> buildOrder(root, query, cb, o, subTypes, subSortKeys))
                    .collect(Collectors.toList());

            query.orderBy(orders);
            return cb.conjunction();
        };
    }

    private static Order buildOrder(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb, Sort.Order sortOrder,
                                    List<? extends Class<? extends Person>> subTypes, Set<String> subSortKeys) {
        String prop = sortOrder.getProperty();
        Expression<?> expr = getExpression(root, query, cb, prop, subTypes, subSortKeys);
        return sortOrder.isAscending() ? cb.asc(expr) : cb.desc(expr);
    }


    private static Expression<?> getExpression(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb, String prop,
                                               List<? extends Class<? extends Person>> subTypes, Set<String> subSortKeys) {
        switch (prop) {
            case "numberOfProfessions":
                return buildProfessionsSubquery(root, query, cb);
            case "gender":
                return cb.function("pesel_gender", String.class, root.get("pesel"));
            case "age":
                return cb.function("pesel_birthdate", Date.class, root.get("pesel"));
            default:
                if (subSortKeys.contains(prop)) {
                    return treatAndGet(root, prop, subTypes);
                }
                if (hasField(Person.class, prop)) {
                    return root.get(prop);
                }
                throw new InvalidFieldException("Nieznane pole sortowania: " + prop);
        }
    }

    private static Expression<Long> buildProfessionsSubquery(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<Long> sq = query.subquery(Long.class);
        Root<Employment> hist = sq.from(Employment.class);
        sq.select(cb.count(hist));
        sq.where(cb.equal(hist.get("employee"), root));
        return sq.getSelection();
    }

    private static Path<?> treatAndGet(Root<Person> root, String prop, List<? extends Class<? extends Person>> subTypes) {
        Class<? extends Person> cls = subTypes.stream()
                .filter(c -> hasField(c, prop))
                .findFirst()
                .orElseThrow(() -> new InvalidFieldException("Nieznane pole sortowania: " + prop));

        return ((Root<? extends Person>) root.as(cls)).get(prop);
    }


    private static boolean hasField(Class<?> cls, String name) {
        return Arrays.stream(cls.getDeclaredFields())
                .anyMatch(f -> f.getName().equals(name));
    }
}
