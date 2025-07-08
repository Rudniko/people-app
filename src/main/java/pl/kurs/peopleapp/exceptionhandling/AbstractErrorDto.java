package pl.kurs.peopleapp.exceptionhandling;

public abstract class AbstractErrorDto {

    private String message;

    public AbstractErrorDto() {
    }

    public AbstractErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
