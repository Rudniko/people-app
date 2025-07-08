package pl.kurs.peopleapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.peopleapp.commands.person.PersonRequestCommand;
import pl.kurs.peopleapp.dtos.PersonDto;
import pl.kurs.peopleapp.exceptions.InvalidFieldException;
import pl.kurs.peopleapp.services.IPersonSearchService;
import pl.kurs.peopleapp.services.PersonService;


import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/v1/people")
public class PersonController {

    private final PersonService personService;
    private final IPersonSearchService searchService;

    public PersonController(PersonService personService, IPersonSearchService searchService) {
        this.personService = personService;
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody @Valid PersonRequestCommand cmd) {
        PersonDto personDto = personService.createPerson(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
    }

    @PutMapping
    public ResponseEntity<PersonDto> updatePerson(@RequestBody @Valid PersonRequestCommand cmd, @RequestParam Long version) {
        PersonDto personDto = personService.updatePerson(cmd, version);
        return ResponseEntity.status(HttpStatus.OK).body(personDto);
    }

    @GetMapping
    public ResponseEntity<?> searchPeople(@RequestParam Map<String, String> filters, Pageable pageable, HttpServletRequest request) {

        boolean hasPage = request.getParameterMap().containsKey("page");
        boolean hasSize = request.getParameterMap().containsKey("size");
        if (hasPage ^ hasSize) {
            throw new InvalidFieldException("Both 'page' and 'size' must be provided together");
        }

        String[] sortParams = request.getParameterValues("sort");
        if (sortParams != null) {
            for (String s : sortParams) {
                if (s.isBlank()) {
                    throw new InvalidFieldException("'sort' parameter must contain property name");
                }
            }
        }
        Pageable pg = (hasPage && hasSize) ? pageable : Pageable.unpaged();
        Page<PersonDto> result = searchService.search(filters, pg);

        return pg.isPaged() ? ResponseEntity.status(HttpStatus.OK).body(result) : ResponseEntity.status(HttpStatus.OK).body(result.getContent());
    }
}
