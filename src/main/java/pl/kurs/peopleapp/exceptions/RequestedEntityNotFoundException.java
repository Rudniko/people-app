package pl.kurs.peopleapp.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequestedEntityNotFoundException extends EntityNotFoundException {
    public RequestedEntityNotFoundException(String message) {
        super(message);
    }
}
