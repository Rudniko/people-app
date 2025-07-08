package pl.kurs.peopleapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.peopleapp.dtos.PersonDto;

import java.util.Map;

public interface IPersonSearchService {

    Page<PersonDto> search(Map<String, String> filters, Pageable pageable);
}
