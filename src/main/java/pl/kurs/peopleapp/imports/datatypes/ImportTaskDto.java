package pl.kurs.peopleapp.imports.datatypes;

import java.time.Instant;


public class ImportTaskDto {

    private Long importId;
    private String status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
    private long processedRows;

    public ImportTaskDto() {
    }

    public ImportTaskDto(Long importId, String status, Instant createdAt, Instant startedAt, Instant finishedAt, long processedRows) {
        this.importId = importId;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.processedRows = processedRows;
    }

    public static ImportTaskDto fromEntity(ImportTask task) {
        return new ImportTaskDto(task.getId(),task.getStatus().toString(),task.getCreatedAt(),task.getStartedAt(),task.getFinishedAt(),task.getProcessedRows());
    }

    public Long getImportId() {
        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
