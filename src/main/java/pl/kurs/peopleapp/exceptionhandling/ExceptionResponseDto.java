package pl.kurs.peopleapp.exceptionhandling;

import java.sql.Timestamp;
import java.util.List;

public class ExceptionResponseDto {
    private List<? extends AbstractErrorDto> errors;
    private String responseStatus;
    private Timestamp date;

    public ExceptionResponseDto(List<? extends AbstractErrorDto> errors, String responseStatus, Timestamp date) {
        this.errors = errors;
        this.responseStatus = responseStatus;
        this.date = date;
    }

    public List<? extends AbstractErrorDto> getErrors() {
        return errors;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public Timestamp getDate() {
        return date;
    }
}
