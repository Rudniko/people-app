package pl.kurs.peopleapp.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.peopleapp.imports.datatypes.ImportTask;
import pl.kurs.peopleapp.imports.datatypes.ImportTaskDto;
import pl.kurs.peopleapp.imports.services.CsvImportService;
import pl.kurs.peopleapp.imports.services.ImportTaskService;


import java.nio.file.Files;
import java.nio.file.Path;


@CrossOrigin
@RestController
@RequestMapping("api/v1/import")
public class ImportController {

    private final ImportTaskService importTaskService;
    private final CsvImportService csvImportService;

    public ImportController(ImportTaskService importTaskService, CsvImportService csvImportService) {
        this.importTaskService = importTaskService;
        this.csvImportService = csvImportService;
    }

    @PostMapping
    public ResponseEntity<ImportTaskDto> uploadCsv(@RequestPart MultipartFile file) throws Exception {
        Path tempFile = Files.createTempFile("import", ".csv");
        file.transferTo(tempFile);

        ImportTask task = importTaskService.createNewTask();
        csvImportService.importCsvAsync(tempFile, task);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ImportTaskDto.fromEntity(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportTaskDto> checkImportProgress(@PathVariable Long id) {
        ImportTaskDto task = importTaskService.getTask(id);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

}
