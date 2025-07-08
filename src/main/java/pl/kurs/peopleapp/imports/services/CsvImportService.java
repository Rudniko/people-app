package pl.kurs.peopleapp.imports.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kurs.peopleapp.datatypes.Person;
import pl.kurs.peopleapp.imports.datatypes.ImportTask;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CsvImportService {

    @Value("${import.csv.update-every}")
    private int updateEvery;
    private final PersonFactory personFactory;
    private final SessionFactory sessionFactory;
    private final ImportTaskService importTaskService;

    public CsvImportService(PersonFactory personFactory, SessionFactory sessionFactory, ImportTaskService importTaskService) {
        this.personFactory = personFactory;
        this.sessionFactory = sessionFactory;
        this.importTaskService = importTaskService;
    }

    @Async("importExecutor")
    public void importCsvAsync(Path file, ImportTask task) {
        try {
            doImport(file, task);
        } catch (Exception ex) {
            importTaskService.markAsFailed(task.getId());
        }
    }

    void doImport(Path file, ImportTask task) throws IOException {
        importTaskService.markAsRunning(task.getId());
        int count = 0;

        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();


        try (BufferedReader br = Files.newBufferedReader(file);
             CSVParser parser = new CSVParser(br, format);
             Session ss = sessionFactory.openSession()) {

            Transaction tx = ss.beginTransaction();

            for (CSVRecord record : parser) {

                Person person = personFactory.create(record.get("TYP"));
                populateCommonFields(person, record);
                person.populateSpecificFields(record);

                ss.persist(person);

                if (++count % updateEvery == 0) {
                    importTaskService.updateProgress(task.getId(), count);
                    ss.flush();
                    ss.clear();
                }
            }
            importTaskService.updateProgress(task.getId(), count);
            importTaskService.markAsCompleted(task.getId());
            ss.flush();
            ss.clear();
            tx.commit();

        } catch (Exception e) {
            importTaskService.markAsFailed(task.getId());
            throw e;
        }
    }


    private void populateCommonFields(Person person, CSVRecord record) {
        person.setPesel(record.get("pesel"));
        person.setFirstName(record.get("imie"));
        person.setLastName(record.get("nazwisko"));
        person.setHeight(Double.parseDouble(record.get("wzrost")));
        person.setWeight(Double.parseDouble(record.get("waga")));
        person.setEmail(record.get("adres email"));
    }
}
