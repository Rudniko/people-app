package pl.kurs.peopleapp.services;

import org.springframework.stereotype.Service;
import pl.kurs.peopleapp.commands.person.PersonRequestCommand;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.registries.PersonTypeServiceRegistry;

@Service
public class PersonService {

    private final PersonTypeServiceRegistry typeRegistry;

    public PersonService(PersonTypeServiceRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    public PersonDto createPerson(PersonRequestCommand cmd) {
        return typeRegistry.getServiceForType(cmd.getType())
                .createPerson(cmd);
    }

    public PersonDto updatePerson(PersonRequestCommand cmd, Long version) {
        return typeRegistry.getServiceForType(cmd.getType())
                .updatePerson(cmd, version);
    }
}
