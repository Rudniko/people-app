package pl.kurs.peopleapp.importcontroller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.datatypes.Employee;
import pl.kurs.peopleapp.datatypes.Pensioner;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.datatypes.Student;
import pl.kurs.peopleapp.imports.datatypes.ImportTask;
import pl.kurs.peopleapp.imports.datatypes.ImportTaskStatus;
import pl.kurs.peopleapp.repositories.EmploymentRepository;
import pl.kurs.peopleapp.repositories.ImportTaskRepository;
import pl.kurs.peopleapp.repositories.PersonRepository;


import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        properties = {
                "import.csv.update-every=1",
                "spring.main.allow-bean-definition-overriding=true"
        }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ImportControllerImportTest.SyncExecutorConfig.class)
public class ImportControllerImportTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private ImportTaskRepository importTaskRepository;

    @Autowired
    private EmploymentRepository employmentRepo;

    @BeforeEach
    void cleanup() {
        employmentRepo.deleteAll();
        personRepo.deleteAll();
        importTaskRepository.deleteAll();
    }

    @TestConfiguration
    static class SyncExecutorConfig {
        @Bean(name = "importExecutor")
        public TaskExecutor importExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @Test
    void uploadCsv_and_then_verifyAllThreeTypesInDatabase() throws Exception {
        String csv = String.join("\r\n",
                "TYP,pesel,imie,nazwisko,wzrost,waga,adres email,data zatrudnienia,stanowisko,pensja,emerytura,przepracowane lata,uczelnia,rok studiow,kierunek studiow,stypendium",
                "EMPLOYEE,90010100001,Emp,One,170,70,emp1@example.com,2020-01-01,Engineer,10000,,,,,",
                "PENSIONER,65020200002,Pen,Two,160,55,pen2@example.com,,,,2500,30,,,",
                "STUDENT,03030300003,Stu,Three,175,65,stu3@example.com,,,,,,MIT,3,CS,1500",
                ""
        );

        MockMultipartFile file = new MockMultipartFile(
                "file", "people.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());

        List<Person> all = personRepo.findAll();
        assertThat(all).hasSize(3);


        Employee e = (Employee) personRepo.findById("90010100001").orElseThrow();
        assertThat(e.getFirstName()).isEqualTo("Emp");
        assertThat(e.getCurrentProfession()).isEqualTo("Engineer");
        assertThat(e.getCurrentSalary()).isEqualByComparingTo(new java.math.BigDecimal("10000"));

        Pensioner p = (Pensioner) personRepo.findById("65020200002").orElseThrow();
        assertThat(p.getPension()).isEqualByComparingTo(new java.math.BigDecimal("2500"));
        assertThat(p.getYearsWorked()).isEqualTo(30);

        Student s = (Student) personRepo.findById("03030300003").orElseThrow();
        assertThat(s.getUniversityName()).isEqualTo("MIT");
        assertThat(s.getCurrentStudyYear()).isEqualTo(3);
        assertThat(s.getScholarship()).isEqualByComparingTo(new java.math.BigDecimal("1500"));
    }

    @Test
    void uploadCsv_withUnknownType_marksTaskFailed_andDoesNotPersist() throws Exception {
        String csv = String.join("\r\n",
                "TYP,pesel,imie,nazwisko,wzrost,waga,adres email,data zatrudnienia,stanowisko,pensja,emerytura,przepracowane lata,uczelnia,rok studiow,kierunek studiow,stypendium",
                "UNKNOWN,12345678901,Bad,Row,170,70,bad@example.com,2020-01-01,X,0,0,0,Univ,1,Field,0",
                ""
        );

        MockMultipartFile file = new MockMultipartFile(
                "file","bad.csv","text/csv",csv.getBytes(StandardCharsets.UTF_8)
        );

        String postBody = mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.importId").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number idNum = JsonPath.read(postBody, "$.importId");
        long taskId = idNum.longValue();

        ImportTask task = importTaskRepository.findById(taskId).orElseThrow();
        assertThat(task.getStatus()).isEqualTo(ImportTaskStatus.FAILED);

        assertThat(personRepo.findAll()).isEmpty();
    }
}
