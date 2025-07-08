package pl.kurs.peopleapp.personcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.commands.person.EmployeeRequestCommand;
import pl.kurs.peopleapp.commands.person.PensionerRequestCommand;
import pl.kurs.peopleapp.commands.person.StudentRequestCommand;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Pensioner;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.datatypes.Student;
import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PersonControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private EmploymentRepository employmentRepo;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void cleanup() {
        employmentRepo.deleteAll();
        personRepo.deleteAll();
    }

    @Test
    void createEmployee_shouldReturnCreated_andPersistEmployee() throws Exception {
        EmployeeRequestCommand cmd = new EmployeeRequestCommand();
        cmd.setPesel("89022537198");
        cmd.setFirstName("Jan");
        cmd.setLastName("Kowalski");
        cmd.setHeight(180.0);
        cmd.setWeight(75.0);
        cmd.setEmail("jan.kowalski@example.com");
        cmd.setStartOfEmployment(LocalDate.of(2020, 1, 15));
        cmd.setCurrentProfession("Developer");
        cmd.setCurrentSalary(new BigDecimal("12000.00"));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.pesel").value("89022537198"))
                .andExpect(jsonPath("$.numberOfProfessions").value(0))
                .andExpect(jsonPath("$.currentSalary").value(12000.00));

        List<Person> all = personRepo.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0)).isInstanceOf(Employee.class);
    }

    @Test
    void createPensioner_shouldReturnCreated_andPersistPensioner() throws Exception {
        PensionerRequestCommand cmd = new PensionerRequestCommand();
        cmd.setPesel("82030426276");
        cmd.setFirstName("Maria");
        cmd.setLastName("Nowak");
        cmd.setHeight(165.0);
        cmd.setWeight(60.0);
        cmd.setEmail("maria.nowak@example.com");
        cmd.setPension(new BigDecimal("2500.00"));
        cmd.setYearsWorked(40);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel").value("82030426276"))
                .andExpect(jsonPath("$.yearsWorked").value(40));

        List<Person> all = personRepo.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0)).isInstanceOf(Pensioner.class);
    }

    @Test
    void createStudent_shouldReturnCreated_andPersistStudent() throws Exception {
        StudentRequestCommand cmd = new StudentRequestCommand();
        cmd.setPesel("00272897964");
        cmd.setFirstName("Piotr");
        cmd.setLastName("Zieliński");
        cmd.setHeight(175.0);
        cmd.setWeight(68.0);
        cmd.setEmail("piotr.z@example.com");
        cmd.setUniversityName("Uniwersytet Warszawski");
        cmd.setCurrentStudyYear(3);
        cmd.setStudyField("Informatyka");
        cmd.setScholarship(new BigDecimal("1500.00"));

        String json = objectMapper.writeValueAsString(cmd);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel").value("00272897964"))
                .andExpect(jsonPath("$.universityName").value("Uniwersytet Warszawski"));

        List<Person> all = personRepo.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0)).isInstanceOf(Student.class);
    }

    @Test
    void whenMissingRequiredSearchFields_ReturnBadRequest() throws Exception {
        String invalidJson = "{\"type\":\"EMPLOYEE\",\"firstName\":\"Anna\"}";

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        assertThat(personRepo.count()).isZero();
    }
}
