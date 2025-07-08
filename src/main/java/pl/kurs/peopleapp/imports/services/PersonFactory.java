package pl.kurs.peopleapp.imports.services;

import jakarta.persistence.DiscriminatorValue;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.exceptions.InvalidPersonTypeException;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class PersonFactory {

    private final Map<String, Supplier<? extends Person>> types;

    public PersonFactory() {
        Reflections reflections = new Reflections("pl.kurs.peopleapp.datatypes");
        Set<Class<? extends Person>> subTypes = reflections.getSubTypesOf(Person.class);

        this.types = subTypes.stream()
                .filter(c -> c.isAnnotationPresent(DiscriminatorValue.class))
                .collect(Collectors.toMap(
                        c -> c.getAnnotation(DiscriminatorValue.class).value(),
                        clazz -> {
                            try {
                                Constructor<? extends Person> constructor = clazz.getDeclaredConstructor();
                                constructor.setAccessible(true);
                                return () -> {
                                    try {
                                        return constructor.newInstance();
                                    } catch (Exception e) {
                                        throw new InvalidPersonTypeException("Could not instantiate: " + clazz, e);
                                    }
                                };
                            } catch (Exception e) {
                                throw new InvalidPersonTypeException("No suitable constructor for: " + clazz, e);
                            }
                        }
                ));
    }

    public Person create(String type) {
        Supplier<? extends Person> supplier = types.get(type);
        if (supplier == null)
            throw new InvalidPersonTypeException("Unknown type: " + type);
        return supplier.get();
    }
}
