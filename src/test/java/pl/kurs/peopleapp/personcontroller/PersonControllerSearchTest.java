package pl.kurs.peopleapp.personcontroller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Employment;
import pl.kurs.peopleapp.datatypes.Pensioner;
import pl.kurs.peopleapp.datatypes.Student;

import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PersonControllerSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private EmploymentRepository employmentRepo;

    private Employee empAlice;
    private Employee empBob;
    private Pensioner penCarol;
    private Student stuDave;

    @BeforeEach
    void setUp() {
        employmentRepo.deleteAll();
        personRepo.deleteAll();


        empAlice = new Employee();
        empAlice.setPesel("82030341546");
        empAlice.setFirstName("Alice");
        empAlice.setLastName("Smith");
        empAlice.setHeight(170.0);
        empAlice.setWeight(60.0);
        empAlice.setEmail("alice@example.com");
        empAlice.setStartOfEmployment(LocalDate.of(2021, 1, 1));
        empAlice.setCurrentProfession("Developer");
        empAlice.setCurrentSalary(new BigDecimal("10000"));

        Employment histA1 = new Employment();
        histA1.setProfession("Junior Dev");
        histA1.setSalary(new BigDecimal("8000"));
        histA1.setStartDate(LocalDate.of(2020, 1, 1));
        histA1.setEndDate(LocalDate.of(2021, 1, 1));
        empAlice.addEmployment(histA1);


        empBob = new Employee();
        empBob.setPesel("82090388251");
        empBob.setFirstName("Bob");
        empBob.setLastName("Jones");
        empBob.setHeight(180.0);
        empBob.setWeight(80.0);
        empBob.setEmail("bob@example.com");
        empBob.setStartOfEmployment(LocalDate.of(2022, 6, 15));
        empBob.setCurrentProfession("Tester");
        empBob.setCurrentSalary(new BigDecimal("8000"));

        Employment histB1 = new Employment();
        histB1.setProfession("QA Intern");
        histB1.setSalary(new BigDecimal("4000"));
        histB1.setStartDate(LocalDate.of(2021, 6, 1));
        histB1.setEndDate(LocalDate.of(2022, 6, 1));
        empBob.addEmployment(histB1);

        Employment histB2 = new Employment();
        histB2.setProfession("QA Analyst");
        histB2.setSalary(new BigDecimal("6000"));
        histB2.setStartDate(LocalDate.of(2022, 6, 1));
        histB2.setEndDate(LocalDate.of(2023, 6, 1));
        empBob.addEmployment(histB2);


        penCarol = new Pensioner();
        penCarol.setPesel("55032116458");
        penCarol.setFirstName("Carol");
        penCarol.setLastName("White");
        penCarol.setHeight(160.0);
        penCarol.setWeight(55.0);
        penCarol.setEmail("carol@example.com");
        penCarol.setPension(new BigDecimal("2500"));
        penCarol.setYearsWorked(35);


        stuDave = new Student();
        stuDave.setPesel("04272376533");
        stuDave.setFirstName("Dave");
        stuDave.setLastName("Brown");
        stuDave.setHeight(175.0);
        stuDave.setWeight(70.0);
        stuDave.setEmail("dave@example.com");
        stuDave.setUniversityName("MIT");
        stuDave.setCurrentStudyYear(2);
        stuDave.setStudyField("Engineering");
        stuDave.setScholarship(new BigDecimal("1200"));

        personRepo.saveAll(List.of(empAlice, empBob, penCarol, stuDave));
    }

    @Test
    void getAllWithoutCriteria_returnsAllAsList() throws Exception {
        mockMvc.perform(get("/api/v1/people"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[?(@.pesel=='" + empAlice.getPesel() + "')]").exists())
                .andExpect(jsonPath("$[?(@.pesel=='" + penCarol.getPesel() + "')]").exists());
    }

    @Test
    void getAllWithPagination_returnsPageDto() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.sort.sorted").value(false));
    }

    @Test
    void filterByTypeEmployee_returnsOnlyEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("type", "employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pesel").value(empAlice.getPesel()))
                .andExpect(jsonPath("$[1].pesel").value(empBob.getPesel()));
    }

    @Test
    void filterByPersonFields_firstNameAndPesel() throws Exception {

        mockMvc.perform(get("/api/v1/people")
                        .param("firstName", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pesel").value(empAlice.getPesel()));


        mockMvc.perform(get("/api/v1/people")
                        .param("pesel", stuDave.getPesel()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Dave"));
    }

    @Test
    void filterByEmployeeSpecificField_currentProfession() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("type", "employee")
                        .param("currentProfession", "Developer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    @Test
    void filterByNumberOfProfessions_returnsCorrectEmployee() throws Exception {

        mockMvc.perform(get("/api/v1/people")
                        .param("type", "employee")
                        .param("numberOfProfessionsFrom", "2")
                        .param("numberOfProfessionsTo", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pesel").value(empBob.getPesel()));
    }

    @Test
    void sortingAndPaginationTogether_worksAcrossAllFields() throws Exception {

        mockMvc.perform(get("/api/v1/people")
                        .param("sort", "lastName,asc")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName").value("Brown"))
                .andExpect(jsonPath("$.content[1].lastName").value("Jones"))
                .andExpect(jsonPath("$.content[2].lastName").value("Smith"))
                .andExpect(jsonPath("$.totalElements").value(4));
    }


    @Test
    void filterByGender_maleAndFemale() throws Exception {
        mockMvc.perform(get("/api/v1/people").param("gender", "M"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[?(@.pesel=='" + empBob.getPesel() + "')]").exists());

        mockMvc.perform(get("/api/v1/people").param("gender", "F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[?(@.pesel=='" + empAlice.getPesel() + "')]").exists());
    }

    @Test
    void paginationSortedByGenderDesc_withoutCriteria() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "gender,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].pesel").value(stuDave.getPesel()))
                .andExpect(jsonPath("$.content[1].pesel").value(penCarol.getPesel()))
                .andExpect(jsonPath("$.content[2].pesel").value(empBob.getPesel()))
                .andExpect(jsonPath("$.content[3].pesel").value(empAlice.getPesel()));
    }

    @Test
    void paginationSortedByGenderAsc_withoutCriteria() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "gender"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].pesel").value(empAlice.getPesel()))
                .andExpect(jsonPath("$.content[1].pesel").value(stuDave.getPesel()))
                .andExpect(jsonPath("$.content[2].pesel").value(penCarol.getPesel()))
                .andExpect(jsonPath("$.content[3].pesel").value(empBob.getPesel()));
    }

    @Test
    void employeeWithPagination_sortedByNumberOfProfessions() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("type", "employee")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "numberOfProfessions,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].pesel").value(empBob.getPesel()));
    }

    @Test
    void filterByAgeRange() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("ageFrom", "40")
                        .param("ageTo", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pesel").value(empAlice.getPesel()));
    }

    @Test
    void filterByStartOfEmploymentRange() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("type", "employee")
                        .param("startOfEmploymentFrom", "2022-01-01")
                        .param("startOfEmploymentTo", "2023-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pesel").value(empBob.getPesel()));
    }

    @Test
    void filterByYearsWorkedRange_forPensioner() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("type", "pensioner")
                        .param("yearsWorkedFrom", "30")
                        .param("yearsWorkedTo", "40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pesel").value(penCarol.getPesel()));
    }

    @Test
    void filterByCurrentStudyYearRange_forStudent() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                        .param("type", "student")
                        .param("currentStudyYearFrom", "2")
                        .param("currentStudyYearTo", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pesel").value(stuDave.getPesel()));
    }
}
