package pl.kurs.peopleapp.registries;

import org.springframework.stereotype.Component;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.exceptions.InvalidPersonTypeException;
import pl.kurs.peopleapp.services.TypeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PersonTypeServiceRegistry {

    private final Map<String, TypeService> map;


    public PersonTypeServiceRegistry(List<TypeService> services) {
        map = services.stream()
                .collect(Collectors.toMap(TypeService::getType, s -> s));
    }

    public TypeService getServiceForType(String type) {
        return Optional.ofNullable(map.get(type)).orElseThrow(() -> new InvalidPersonTypeException("Unknown person type: " + type));
    }


}
