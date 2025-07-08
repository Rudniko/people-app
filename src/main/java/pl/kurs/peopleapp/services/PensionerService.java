package pl.kurs.peopleapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.commands.person.PersonRequestCommand;
import pl.kurs.peopleapp.datatypes.Pensioner;
import pl.kurs.peopleapp.dtos.PensionerDto;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.exceptions.RequestedEntityNotFoundException;
import pl.kurs.peopleapp.repositories.PensionerRepository;

@Service
@Transactional
public class PensionerService implements TypeService {

    private final PensionerRepository repository;
    private final ModelMapper modelMapper;

    public PensionerService(PensionerRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getType() {
        return "pensioner";
    }

    @Override
    public PersonDto createPerson(PersonRequestCommand cmd) {
        Pensioner pensioner = modelMapper.map(cmd, Pensioner.class);
        repository.save(pensioner);
        return modelMapper.map(pensioner, PensionerDto.class);
    }

    @Override
    public PersonDto updatePerson(PersonRequestCommand cmd, Long version) {

        Pensioner pensioner = repository.findByPesel(cmd.getPesel())
                .orElseThrow(() -> new RequestedEntityNotFoundException("Person for update not found"));

        if (!pensioner.getVersion().equals(version)) {
            throw new OptimisticLockingFailureException("Version mismatch");
        }

        modelMapper.map(cmd, pensioner);
        repository.save(pensioner);

        return modelMapper.map(pensioner, PensionerDto.class);
    }
}
