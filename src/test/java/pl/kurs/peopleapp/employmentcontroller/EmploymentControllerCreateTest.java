package pl.kurs.peopleapp.employmentcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.commands.employment.EmploymentCreateCommand;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Employment;
import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmploymentControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private EmploymentRepository employmentRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employmentRepo.deleteAll();
        personRepo.deleteAll();

        employee = new Employee();
        employee.setPesel("85050512345");
        employee.setFirstName("Ewa");
        employee.setLastName("Kowalska");
        employee.setHeight(165.0);
        employee.setWeight(60.0);
        employee.setEmail("ewa.k@example.com");
        employee.setStartOfEmployment(LocalDate.of(2020, 1, 1));
        employee.setCurrentProfession("Engineer");
        employee.setCurrentSalary(new BigDecimal("9000"));

        personRepo.save(employee);
    }

    @Test
    void addEmployment_andVerifyIfEmployeeHasIt() throws Exception {
        String pesel = employee.getPesel();

        EmploymentCreateCommand cmd = new EmploymentCreateCommand();
        cmd.setEmployeePesel(pesel);
        cmd.setProfession("Consultant");
        cmd.setSalary(new BigDecimal("7000"));
        cmd.setStartDate(LocalDate.of(2018, 3, 1));
        cmd.setEndDate(LocalDate.of(2019, 3, 1));

        mockMvc.perform(post("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isCreated());

        Employee reloaded = (Employee) personRepo.findByPesel(pesel).orElseThrow();
        assertThat(reloaded.getEmployments()).hasSize(1);

        Employment justAdded = reloaded.getEmployments().iterator().next();

        assertThat(justAdded.getProfession()).isEqualTo("Consultant");
        assertThat(justAdded.getSalary()).isEqualByComparingTo(new BigDecimal("7000"));
        assertThat(justAdded.getStartDate()).isEqualTo(LocalDate.of(2018, 3, 1));
        assertThat(justAdded.getEndDate()).isEqualTo(LocalDate.of(2019, 3, 1));
    }

    @Test
    void createEmployment_unknownEmployee_returnsNotFound() throws Exception {
        EmploymentCreateCommand cmd = new EmploymentCreateCommand();
        cmd.setEmployeePesel("00000000000");
        cmd.setProfession("Tester");
        cmd.setSalary(new BigDecimal("4000"));
        cmd.setStartDate(LocalDate.of(2018, 1, 1));
        cmd.setEndDate(LocalDate.of(2018, 12, 31));

        mockMvc.perform(post("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployment_startDateAfterEmployeeStart_throwsBadRequest() throws Exception {
        EmploymentCreateCommand cmd = new EmploymentCreateCommand();
        cmd.setEmployeePesel(employee.getPesel());
        cmd.setProfession("Late Hire");
        cmd.setSalary(new BigDecimal("5000"));
        cmd.setStartDate(LocalDate.of(2020, 2, 1));
        cmd.setEndDate(LocalDate.of(2020, 12, 31));

        mockMvc.perform(post("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createEmployment_overlappingWithExistingEmployment_throwsBadRequest() throws Exception {
        EmploymentCreateCommand first = new EmploymentCreateCommand();
        first.setEmployeePesel(employee.getPesel());
        first.setProfession("Intern");
        first.setSalary(new BigDecimal("3000"));
        first.setStartDate(LocalDate.of(2019, 1, 1));
        first.setEndDate(LocalDate.of(2019, 12, 31));

        mockMvc.perform(post("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(first)))
                .andExpect(status().isCreated());

        EmploymentCreateCommand overlap = new EmploymentCreateCommand();
        overlap.setEmployeePesel(employee.getPesel());
        overlap.setProfession("Contractor");
        overlap.setSalary(new BigDecimal("3500"));
        overlap.setStartDate(LocalDate.of(2019, 6, 1));
        overlap.setEndDate(LocalDate.of(2020, 6, 1));

        mockMvc.perform(post("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overlap)))
                .andExpect(status().isBadRequest());
    }
}
