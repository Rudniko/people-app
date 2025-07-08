package pl.kurs.peopleapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmploymentDateException extends IllegalStateException {

    public InvalidEmploymentDateException(String s) {
        super(s);
    }
}
