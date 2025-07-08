package pl.kurs.peopleapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.commands.person.PersonRequestCommand;
import pl.kurs.peopleapp.datatypes.Student;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.dtos.StudentDto;
import pl.kurs.peopleapp.exceptions.RequestedEntityNotFoundException;
import pl.kurs.peopleapp.repositories.StudentRepository;

@Service
@Transactional
public class StudentService implements TypeService {

    private final StudentRepository repository;
    private final ModelMapper modelMapper;

    public StudentService(StudentRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getType() {
        return "student";
    }

    @Override
    public PersonDto createPerson(PersonRequestCommand cmd) {
        Student student = modelMapper.map(cmd, Student.class);
        repository.save(student);
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public PersonDto updatePerson(PersonRequestCommand cmd, Long version) {

        Student student = repository.findByPesel(cmd.getPesel())
                .orElseThrow(() -> new RequestedEntityNotFoundException("Person for update not found"));

        if (!student.getVersion().equals(version)) {
            throw new OptimisticLockingFailureException("Version mismatch");
        }

        modelMapper.map(cmd, student);
        repository.save(student);

        return modelMapper.map(student, StudentDto.class);
    }


}
