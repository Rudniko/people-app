package pl.kurs.peopleapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ImportException extends IllegalStateException {

    public ImportException(String s) {
        super(s);
    }
}
