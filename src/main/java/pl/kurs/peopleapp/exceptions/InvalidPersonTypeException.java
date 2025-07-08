package pl.kurs.peopleapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidPersonTypeException extends IllegalArgumentException {

    public InvalidPersonTypeException(String message) {
        super(message);
    }

    public InvalidPersonTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
