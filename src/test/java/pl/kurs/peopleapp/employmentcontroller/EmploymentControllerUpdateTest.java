package pl.kurs.peopleapp.employmentcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.kurs.peopleapp.commands.employment.EmploymentUpdateCommand;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Employment;
import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.hibernate.internal.log.SubSystemLogging.BASE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmploymentControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private EmploymentRepository employmentRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private Employment existingEmployment;

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

        existingEmployment = new Employment();
        existingEmployment.setProfession("Intern");
        existingEmployment.setSalary(new BigDecimal("3000"));
        existingEmployment.setStartDate(LocalDate.of(2019, 2, 1));
        existingEmployment.setEndDate(LocalDate.of(2019, 12, 31));
        employee.addEmployment(existingEmployment);

        personRepo.save(employee);
        personRepo.flush();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        employee = (Employee) personRepo.findByPesel(employee.getPesel()).orElseThrow();
        existingEmployment = employmentRepo.findByEmployeePesel(employee.getPesel()).stream().findFirst().orElseThrow();
    }

    @Test
    void updateEmployment_success_updatesFieldsAndIncrementsEmployeeVersion() throws Exception {
        long originalVersion = employee.getVersion();

        EmploymentUpdateCommand cmd = new EmploymentUpdateCommand();
        cmd.setId(existingEmployment.getId());
        cmd.setEmployeePesel(employee.getPesel());
        cmd.setProfession("Consultant");
        cmd.setSalary(new BigDecimal("3500"));
        cmd.setStartDate(LocalDate.of(2018, 1, 1));
        cmd.setEndDate(LocalDate.of(2018, 12, 31));

        mockMvc.perform(put("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profession").value("Consultant"))
                .andExpect(jsonPath("$.salary").value(3500.00))
                .andExpect(jsonPath("$.employeePesel").value(employee.getPesel()));

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Employee updatedEmp = (Employee) personRepo.findByPesel(employee.getPesel()).orElseThrow();
        Employment updated = employmentRepo.findById(existingEmployment.getId()).orElseThrow();

        assertThat(updated.getProfession()).isEqualTo("Consultant");
        assertThat(updated.getSalary()).isEqualByComparingTo(new BigDecimal("3500"));
        assertThat(updatedEmp.getVersion()).isEqualTo(originalVersion + 1);
    }

    @Test
    void updateEmployment_unknownId_returnsNotFound() throws Exception {
        EmploymentUpdateCommand cmd = new EmploymentUpdateCommand();
        cmd.setId(999L);
        cmd.setEmployeePesel(employee.getPesel());
        cmd.setProfession("X");
        cmd.setSalary(BigDecimal.ZERO);
        cmd.setStartDate(LocalDate.of(2000, 1, 1));
        cmd.setEndDate(LocalDate.of(2000, 1, 2));

        mockMvc.perform(put("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployment_peselMismatch_returnsBadRequest() throws Exception {
        Employee other = new Employee();
        other.setPesel("90060654321");
        other.setFirstName("Anna");
        other.setLastName("Nowak");
        other.setHeight(160.0);
        other.setWeight(55.0);
        other.setEmail("anna@example.com");
        other.setStartOfEmployment(LocalDate.of(2021,1,1));
        other.setCurrentProfession("Analyst");
        other.setCurrentSalary(new BigDecimal("7000"));

        personRepo.save(other);
        personRepo.flush();

        EmploymentUpdateCommand cmd = new EmploymentUpdateCommand();
        cmd.setId(existingEmployment.getId());
        cmd.setEmployeePesel(other.getPesel());
        cmd.setProfession("Lead");
        cmd.setSalary(new BigDecimal("8000"));
        cmd.setStartDate(existingEmployment.getStartDate());
        cmd.setEndDate(existingEmployment.getEndDate());

        mockMvc.perform(put("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployment_invalidDateOrder_returnsBadRequest() throws Exception {
        EmploymentUpdateCommand cmd = new EmploymentUpdateCommand();
        cmd.setId(existingEmployment.getId());
        cmd.setEmployeePesel(employee.getPesel());
        cmd.setProfession("X");
        cmd.setSalary(new BigDecimal("1000"));
        cmd.setStartDate(LocalDate.of(2020,1,1));
        cmd.setEndDate(LocalDate.of(2019,1,1));

        mockMvc.perform(put("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployment_withOverlappingDates_returnsBadRequest() throws Exception {
        Employment e2 = new Employment();
        e2.setProfession("Contractor");
        e2.setSalary(new BigDecimal("4000"));
        e2.setStartDate(LocalDate.of(2020,1,1));
        e2.setEndDate(LocalDate.of(2020,12,31));
        employee.addEmployment(e2);
        personRepo.save(employee);
        personRepo.flush();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        employee = (Employee) personRepo.findByPesel(employee.getPesel()).orElseThrow();
        existingEmployment = employmentRepo.findByEmployeePesel(employee.getPesel())
                .stream()
                .filter(e -> !e.getId().equals(e2.getId()))
                .findFirst()
                .orElseThrow();

        EmploymentUpdateCommand cmd = new EmploymentUpdateCommand();
        cmd.setId(existingEmployment.getId());
        cmd.setEmployeePesel(employee.getPesel());
        cmd.setProfession("X");
        cmd.setSalary(existingEmployment.getSalary());
        cmd.setStartDate(LocalDate.of(2020,6,1));
        cmd.setEndDate(LocalDate.of(2021,1,1));

        mockMvc.perform(put("/api/v1/employment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }
}
