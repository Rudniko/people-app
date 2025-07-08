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
import pl.kurs.peopleapp.datatypes.Student;
import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PersonControllerUpdateTest {

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
    void updateEmployee_shouldReturnOk_andPersistChanges() throws Exception {
        EmployeeRequestCommand createCmd = new EmployeeRequestCommand();
        createCmd.setPesel("98041496537");
        createCmd.setFirstName("Jan");
        createCmd.setLastName("Kowalski");
        createCmd.setHeight(180.0);
        createCmd.setWeight(75.0);
        createCmd.setEmail("jan.k@example.com");
        createCmd.setStartOfEmployment(LocalDate.of(2020, 1, 15));
        createCmd.setCurrentProfession("Developer");
        createCmd.setCurrentSalary(new BigDecimal("12000.00"));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCmd)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long version = personRepo.findByPesel("98041496537").orElseThrow().getVersion();

        EmployeeRequestCommand updateCmd = new EmployeeRequestCommand();
        updateCmd.setType("EMPLOYEE");
        updateCmd.setPesel("98041496537");
        updateCmd.setFirstName("Janusz");
        updateCmd.setLastName("Nowak");
        updateCmd.setHeight(182.0);
        updateCmd.setWeight(78.0);
        updateCmd.setEmail("janusz.nowak@example.com");
        updateCmd.setStartOfEmployment(LocalDate.of(2020, 1, 15));
        updateCmd.setCurrentProfession("Senior Developer");
        updateCmd.setCurrentSalary(new BigDecimal("15000.00"));


        mockMvc.perform(put("/api/v1/people")
                        .param("version", version.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCmd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value("98041496537"))
                .andExpect(jsonPath("$.firstName").value("Janusz"))
                .andExpect(jsonPath("$.currentProfession").value("Senior Developer"))
                .andExpect(jsonPath("$.numberOfProfessions").isNumber())
                .andReturn().getResponse().getContentAsString();


        Employee saved = (Employee) personRepo.findByPesel("98041496537").orElseThrow();

        assertThat(saved.getFirstName()).isEqualTo("Janusz");
        assertThat(saved.getCurrentSalary()).isEqualByComparingTo(new BigDecimal("15000.00"));
        assertThat(saved.getVersion()).isGreaterThan(version);
    }

    @Test
    void updatePensioner_shouldReturnOk_andPersistChanges() throws Exception {
        PensionerRequestCommand create = new PensionerRequestCommand();
        create.setPesel("09250345699");
        create.setFirstName("Maria");
        create.setLastName("Nowak");
        create.setHeight(165.0);
        create.setWeight(60.0);
        create.setEmail("maria.nowak@example.com");
        create.setPension(new BigDecimal("2500.00"));
        create.setYearsWorked(40);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated());

        Long version = personRepo.findByPesel("09250345699").orElseThrow().getVersion();

        PensionerRequestCommand update = new PensionerRequestCommand();
        update.setPesel("09250345699");
        update.setFirstName("Marianna");
        update.setLastName("Kowalska");
        update.setHeight(166.0);
        update.setWeight(62.0);
        update.setEmail("marianna.k@example.com");
        update.setPension(new BigDecimal("3000.00"));
        update.setYearsWorked(42);

        mockMvc.perform(put("/api/v1/people")
                        .param("version", version.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value("09250345699"))
                .andExpect(jsonPath("$.firstName").value("Marianna"))
                .andExpect(jsonPath("$.pension").value(3000.00));


        Pensioner saved = (Pensioner) personRepo.findByPesel("09250345699").orElseThrow();

        assertThat(saved.getFirstName()).isEqualTo("Marianna");
        assertThat(saved.getPension()).isEqualByComparingTo(new BigDecimal("3000.00"));
        assertThat(saved.getVersion()).isGreaterThan(version);
    }

    @Test
    void updateStudent_shouldReturnOk_andPersistChanges() throws Exception {
        StudentRequestCommand create = new StudentRequestCommand();
        create.setPesel("84122015261");
        create.setFirstName("Piotr");
        create.setLastName("Zieliński");
        create.setHeight(175.0);
        create.setWeight(68.0);
        create.setEmail("piotr.z@example.com");
        create.setUniversityName("UW");
        create.setCurrentStudyYear(3);
        create.setStudyField("Informatyka");
        create.setScholarship(new BigDecimal("1500.00"));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated());

        Long version = personRepo.findByPesel("84122015261").orElseThrow().getVersion();

        StudentRequestCommand update = new StudentRequestCommand();
        update.setPesel("84122015261");
        update.setFirstName("Piotrek");
        update.setLastName("Z.");
        update.setHeight(176.0);
        update.setWeight(69.0);
        update.setEmail("piotrek.z@example.com");
        update.setUniversityName("UW");
        update.setCurrentStudyYear(4);
        update.setStudyField("Matematyka");
        update.setScholarship(new BigDecimal("1800.00"));


        mockMvc.perform(put("/api/v1/people")
                        .param("version", version.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value("84122015261"))
                .andExpect(jsonPath("$.currentStudyYear").value(4))
                .andExpect(jsonPath("$.studyField").value("Matematyka"));

        Student saved = (Student) personRepo.findByPesel("84122015261").orElseThrow();

        assertThat(saved.getCurrentStudyYear()).isEqualTo(4);
        assertThat(saved.getScholarship()).isEqualByComparingTo(new BigDecimal("1800.00"));
        assertThat(saved.getVersion()).isGreaterThan(version);
    }

    @Test
    void updateWithWrongVersion_shouldFail() throws Exception {
        EmployeeRequestCommand createCmd = new EmployeeRequestCommand();
        createCmd.setPesel("98041496537");
        createCmd.setFirstName("Jan");
        createCmd.setLastName("Kowalski");
        createCmd.setHeight(180.0);
        createCmd.setWeight(75.0);
        createCmd.setEmail("jan.k@example.com");
        createCmd.setStartOfEmployment(LocalDate.of(2020, 1, 15));
        createCmd.setCurrentProfession("Developer");
        createCmd.setCurrentSalary(new BigDecimal("12000.00"));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCmd)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long version = personRepo.findByPesel("98041496537").orElseThrow().getVersion();


        EmployeeRequestCommand cmd = new EmployeeRequestCommand();
        cmd.setPesel("98041496537");
        cmd.setFirstName("Error");
        cmd.setLastName("Case");
        cmd.setHeight(180.0);
        cmd.setWeight(75.0);
        cmd.setEmail("err@example.com");
        cmd.setStartOfEmployment(LocalDate.of(2020,1,15));
        cmd.setCurrentProfession("Dev");
        cmd.setCurrentSalary(new BigDecimal("10000"));

        String badJson = objectMapper.writeValueAsString(cmd);

        mockMvc.perform(put("/api/v1/people")
                        .param("version", String.valueOf(version + 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isConflict());
    }

    @Test
    void updateEmployee_withStaleVersion_shouldReturnConflict() throws Exception {
        EmployeeRequestCommand createCmd = new EmployeeRequestCommand();
        createCmd.setPesel("98041496537");
        createCmd.setFirstName("Jan");
        createCmd.setLastName("Kowalski");
        createCmd.setHeight(180.0);
        createCmd.setWeight(75.0);
        createCmd.setEmail("jan.k@example.com");
        createCmd.setStartOfEmployment(LocalDate.of(2020, 1, 15));
        createCmd.setCurrentProfession("Developer");
        createCmd.setCurrentSalary(new BigDecimal("12000.00"));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCmd)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long originalVersion = personRepo.findByPesel("98041496537").orElseThrow().getVersion();

        Employee concurrentlyUpdated = (Employee) personRepo.findByPesel("98041496537").orElseThrow();
        concurrentlyUpdated.setLastName("Concurrent");
        personRepo.save(concurrentlyUpdated);

        EmployeeRequestCommand staleCmd = new EmployeeRequestCommand();
        staleCmd.setType("EMPLOYEE");
        staleCmd.setPesel("98041496537");
        staleCmd.setFirstName("Jan");
        staleCmd.setLastName("Stale");
        staleCmd.setHeight(180.0);
        staleCmd.setWeight(75.0);
        staleCmd.setEmail("jan.stale@example.com");
        staleCmd.setStartOfEmployment(LocalDate.of(2020,1,15));
        staleCmd.setCurrentProfession("Dev");
        staleCmd.setCurrentSalary(new BigDecimal("10000"));
        String staleJson = objectMapper.writeValueAsString(staleCmd);

        mockMvc.perform(put("/api/v1/people")
                        .param("version", originalVersion.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(staleJson))
                .andExpect(status().isConflict());
    }
}
