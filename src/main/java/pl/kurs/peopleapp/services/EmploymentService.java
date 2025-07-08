package pl.kurs.peopleapp.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.commands.employment.EmploymentCreateCommand;
import pl.kurs.peopleapp.commands.employment.EmploymentUpdateCommand;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Employment;
import pl.kurs.peopleapp.dtos.EmploymentDto;
import pl.kurs.peopleapp.exceptions.EmployeePeselMismatchException;
import pl.kurs.peopleapp.exceptions.InvalidEmploymentDateException;
import pl.kurs.peopleapp.exceptions.RequestedEntityNotFoundException;
import pl.kurs.peopleapp.repositories.EmployeeRepository;
import pl.kurs.peopleapp.repositories.EmploymentRepository;


import java.util.Set;

@Service
@Transactional
public class EmploymentService {

    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;

    public EmploymentService(EmployeeRepository employeeRepository, EmploymentRepository employmentRepository, EntityManager entityManager, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.employmentRepository = employmentRepository;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
    }

    public EmploymentDto createEmployment(EmploymentCreateCommand cmd) {
        Employee employee = employeeRepository.findByPesel(cmd.getEmployeePesel())
                .orElseThrow(() -> new RequestedEntityNotFoundException("Unknown Employee with given pesel"));

        entityManager.lock(employee, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        Employment employment = modelMapper.map(cmd, Employment.class);

        if (employment.getStartDate().isAfter(employee.getStartOfEmployment())) {
            throw new InvalidEmploymentDateException("Dates overlap with current employment");
        }

        validateEmployments(employee.getEmployments(), employment);

        employee.addEmployment(employment);
        entityManager.flush();

        EmploymentDto dto = modelMapper.map(employment, EmploymentDto.class);
        dto.setEmployeePesel(employment.getEmployee().getPesel());

        return dto;
    }

    public EmploymentDto updateEmployment(EmploymentUpdateCommand cmd) {
        Employment employment = employmentRepository.findById(cmd.getId())
                .orElseThrow(() -> new RequestedEntityNotFoundException("Employment with id: " + cmd.getId() + " not found"));

        Employee employee = employment.getEmployee();
        if (!employee.getPesel().equals(cmd.getEmployeePesel())) {
            throw new EmployeePeselMismatchException("Pesel mismatch");
        }

        entityManager.lock(employee, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        modelMapper.map(cmd, employment);

        validateEmployments(employee.getEmployments(), employment);

        EmploymentDto dto = modelMapper.map(employment, EmploymentDto.class);
        dto.setEmployeePesel(cmd.getEmployeePesel());
        return dto;
    }

    private void validateEmployments(Set<Employment> existing, Employment toAdd) {
        if (!toAdd.getStartDate().isBefore(toAdd.getEndDate())) {
            throw new InvalidEmploymentDateException("Employment start date must be before end date!");
        }

        boolean anyMatch = existing.stream()
                .filter(eh -> !eh.equals(toAdd))
                .anyMatch(eh -> !eh.getStartDate().isAfter(toAdd.getEndDate()) && !toAdd.getStartDate().isAfter(eh.getEndDate()));

        if (anyMatch) {
            throw new InvalidEmploymentDateException("Dates overlap with exisiting employments");
        }
    }
}
