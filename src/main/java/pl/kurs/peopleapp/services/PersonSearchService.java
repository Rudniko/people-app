package pl.kurs.peopleapp.services;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.dtos.EmploymentHistoryCount;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.dtos.EmployeeDto;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.registries.PersonDtoMapperRegistry;
import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;
import pl.kurs.peopleapp.specifications.MapBasedPersonSpecificationBuilder;
import pl.kurs.peopleapp.specifications.TypeSearchSpecification;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonSearchService implements IPersonSearchService {

    private final PersonRepository personRepository;
    private final EmploymentRepository employmentRepository;
    private final List<TypeSearchSpecification> specifications;
    private final PersonDtoMapperRegistry dtoMapperRegistry;

    public PersonSearchService(PersonRepository personRepository, EmploymentRepository employmentRepository, List<TypeSearchSpecification> specifications, PersonDtoMapperRegistry dtoMapperRegistry) {
        this.personRepository = personRepository;
        this.employmentRepository = employmentRepository;
        this.specifications = specifications;
        this.dtoMapperRegistry = dtoMapperRegistry;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDto> search(Map<String, String> filters, Pageable pageable) {

        Specification<Person> spec = MapBasedPersonSpecificationBuilder.build(filters, specifications, pageable.getSort());

        Pageable pageWithoutSort = pageable.isPaged() ?
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted()) : Pageable.unpaged();

        Page<Person> page = personRepository.findAll(spec, pageWithoutSort);

        Map<String, Long> professionsCount = countEmployeeProfessions(page);

        Page<PersonDto> dtoPage = page.map(p -> {
            PersonDto dto = dtoMapperRegistry.map(p);
            if (dto instanceof EmployeeDto edto) {
                edto.setNumberOfProfessions(
                        professionsCount.getOrDefault(p.getPesel(), 0L)
                );
            }
            return dto;
        });

        return pageable.isPaged() ? dtoPage : new PageImpl<>(dtoPage.getContent());
    }

    private Map<String, Long> countEmployeeProfessions(Page<Person> page) {
        Set<String> employeePesels = page.stream()
                .filter(p -> p instanceof Employee)
                .map(Person::getPesel)
                .collect(Collectors.toSet());

        if (employeePesels.isEmpty()) {
            return Collections.emptyMap();
        }

        try (Stream<EmploymentHistoryCount> stream = employmentRepository.streamCountByEmployeePesels(employeePesels)) {
            return stream
                    .filter(e -> e.getPesel() != null)
                    .collect(Collectors.toMap(EmploymentHistoryCount::getPesel, EmploymentHistoryCount::getCount));
        }
    }
}
