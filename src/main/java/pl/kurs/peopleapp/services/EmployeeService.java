package pl.kurs.peopleapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.commands.person.PersonRequestCommand;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.dtos.EmployeeDto;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.exceptions.RequestedEntityNotFoundException;
import pl.kurs.peopleapp.repositories.EmployeeRepository;

@Service
@Transactional
public class EmployeeService implements TypeService {

    private final EmployeeRepository repository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getType() {
        return "employee";
    }

    @Override
    public PersonDto createPerson(PersonRequestCommand cmd) {
        Employee employee = modelMapper.map(cmd, Employee.class);
        repository.save(employee);
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        dto.setNumberOfProfessions(0L);
        return dto;
    }

    @Override
    public PersonDto updatePerson(PersonRequestCommand cmd, Long version) {

        Employee employee = repository.findByPesel(cmd.getPesel())
                .orElseThrow(() -> new RequestedEntityNotFoundException("Person for update not found"));

        if (!employee.getVersion().equals(version)) {
            throw new OptimisticLockingFailureException("Version mismatch");
        }

        modelMapper.map(cmd, employee);
        repository.save(employee);

        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);

        dto.setNumberOfProfessions((long) employee.getEmployments().size());
        return dto;
    }
}
