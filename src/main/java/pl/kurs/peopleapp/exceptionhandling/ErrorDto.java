package pl.kurs.peopleapp.exceptionhandling;

public class ErrorDto extends AbstractErrorDto {

    private String errorType;



    public ErrorDto(String errorType) {
        this.errorType = errorType;
    }

    public ErrorDto(String message, String errorType) {
        super(message);
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }


}
