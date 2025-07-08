package pl.kurs.peopleapp.imports.services;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.peopleapp.exceptions.ImportException;
import pl.kurs.peopleapp.exceptions.InvalidFieldException;
import pl.kurs.peopleapp.imports.datatypes.ImportTask;
import pl.kurs.peopleapp.imports.datatypes.ImportTaskDto;
import pl.kurs.peopleapp.imports.datatypes.ImportTaskStatus;
import pl.kurs.peopleapp.repositories.ImportTaskRepository;

import java.time.Instant;

@Service
public class ImportTaskService {

    private final ImportTaskRepository repository;

    public ImportTaskService(ImportTaskRepository repository) {
        this.repository = repository;
    }


    @Transactional(readOnly = true)
    public ImportTaskDto getTask(Long id) {
        ImportTask task = repository.findById(id)
                .orElseThrow(() -> new InvalidFieldException("Task not found: " + id));
        return ImportTaskDto.fromEntity(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportTask createNewTask() {
        ImportTask importTask;
        try {
            importTask = new ImportTask(ImportTaskStatus.CREATED, Instant.now(), 0);
            importTask.setRunningFlag(Boolean.TRUE);
            repository.saveAndFlush(importTask);
        } catch (Exception e) {
            throw new ImportException("Another import has already been scheduled or is running");
        }
        return importTask;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsRunning(Long taskId) {
        ImportTask task = repository.findById(taskId)
                .orElseThrow(() -> new InvalidFieldException("Task not found: " + taskId));
        task.setStatus(ImportTaskStatus.RUNNING);
        task.setStartedAt(Instant.now());
        repository.saveAndFlush(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProgress(Long taskId, long processed) {
        ImportTask task = repository.findById(taskId)
                .orElseThrow(() -> new InvalidFieldException("Task not found: " + taskId));
        task.setProcessedRows(processed);
        repository.saveAndFlush(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsFailed(Long taskId) {
        ImportTask task = repository.findById(taskId)
                .orElseThrow(() -> new InvalidFieldException("Task not found: " + taskId));
        task.setStatus(ImportTaskStatus.FAILED);
        task.setRunningFlag(null);
        repository.saveAndFlush(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsCompleted(Long taskId) {
        ImportTask task = repository.findById(taskId)
                .orElseThrow(() -> new InvalidFieldException("Task not found: " + taskId));
        task.setStatus(ImportTaskStatus.COMPLETED);
        task.setFinishedAt(Instant.now());
        task.setRunningFlag(null);
        repository.saveAndFlush(task);
    }


}
