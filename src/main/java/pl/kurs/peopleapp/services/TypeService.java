package pl.kurs.peopleapp.services;

import pl.kurs.peopleapp.commands.person.PersonRequestCommand;
import pl.kurs.peopleapp.dtos.PersonDto;

public interface TypeService {

    String getType();

    PersonDto createPerson(PersonRequestCommand cmd);
    PersonDto updatePerson(PersonRequestCommand cmd, Long version);
}
