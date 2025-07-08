package pl.kurs.peopleapp.imports.datatypes;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class ImportTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ImportTaskStatus status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
    private long processedRows;
    @Column(unique = true)
    private Boolean runningFlag;

    public ImportTask() {
    }

    public ImportTask(ImportTaskStatus status, Instant createdAt, long processedRows) {
        this.status = status;
        this.createdAt = createdAt;
        this.processedRows = processedRows;
    }

    public Boolean getRunningFlag() {
        return runningFlag;
    }

    public void setRunningFlag(Boolean runningFlag) {
        this.runningFlag = runningFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long importId) {
        this.id = importId;
    }

    public ImportTaskStatus getStatus() {
        return status;
    }

    public void setStatus(ImportTaskStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public long getProcessedRows() {
        return processedRows;
    }

    public void setProcessedRows(long processedRows) {
        this.processedRows = processedRows;
    }
}


