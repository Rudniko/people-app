package pl.kurs.peopleapp.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.peopleapp.commands.employment.EmploymentCreateCommand;
import pl.kurs.peopleapp.commands.employment.EmploymentUpdateCommand;
import pl.kurs.peopleapp.dtos.EmploymentDto;
import pl.kurs.peopleapp.services.EmploymentService;

@CrossOrigin
@RestController
@RequestMapping("api/v1/employment")
public class EmploymentController {

    private final EmploymentService employmentService;

    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @PostMapping
    public ResponseEntity<EmploymentDto> createEmployment(@RequestBody @Valid EmploymentCreateCommand cmd) {
        EmploymentDto employmentDto = employmentService.createEmployment(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(employmentDto);
    }

    @PutMapping
    public ResponseEntity<EmploymentDto> updateEmployment(@RequestBody @Valid EmploymentUpdateCommand cmd) {
        EmploymentDto employmentDto = employmentService.updateEmployment(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(employmentDto);
    }


}
