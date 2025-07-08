package pl.kurs.peopleapp.registries;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.kurs.peopleapp.annotations.DtoMapping;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.exceptions.InvalidPersonTypeException;

import java.util.HashMap;
import java.util.Map;

@Component
public class PersonDtoMapperRegistry {

    private final ModelMapper modelMapper;
    private final Map<Class<?>, Class<?>> classToDtoMap = new HashMap<>();

    public PersonDtoMapperRegistry(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T extends PersonDto> T map(Person person) {
        Class<?> entityClass = person.getClass();
        Class<?> dtoClass = resolveDtoClass(entityClass);

        classToDtoMap.computeIfAbsent(entityClass, ec -> dtoClass);

        if (modelMapper.getTypeMap(entityClass, dtoClass) == null) {
            modelMapper.createTypeMap(entityClass, dtoClass);
        }

        return (T) modelMapper.map(person, dtoClass);
    }

    private Class<?> resolveDtoClass(Class<?> entityClass) {
        DtoMapping annotation = entityClass.getAnnotation(DtoMapping.class);
        if (annotation == null) {
            throw new InvalidPersonTypeException("Missing @DtoMapping on class: " + entityClass.getName());
        }
        return annotation.value();
    }
}
