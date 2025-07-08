package pl.kurs.peopleapp.exceptionhandling;


import java.util.Map;

public class ValidationErrorDto extends AbstractErrorDto {

    private Map<String, String> errors;


    public ValidationErrorDto(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
