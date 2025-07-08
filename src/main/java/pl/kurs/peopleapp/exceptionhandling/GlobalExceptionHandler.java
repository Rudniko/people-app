package pl.kurs.peopleapp.exceptionhandling;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.peopleapp.exceptions.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionResponseDto dto = new ExceptionResponseDto(
                List.of(new ValidationErrorDto("Invalid or Missing fields", errors)),
                "BAD_REQUEST", Timestamp.from(Instant.now()));

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionResponseDto> handleOptimisticLockingFailureException(OptimisticLockingFailureException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto("Version mismatch", e.getClass().getSimpleName())),
                "CONFLICT",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RequestedEntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleRequestedEntityNotFoundException(RequestedEntityNotFoundException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "NOT_FOUND",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmploymentDateException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidEmploymentDateException(InvalidEmploymentDateException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeePeselMismatchException.class)
    public ResponseEntity<ExceptionResponseDto> handleEmployeePeselMismatchException(EmployeePeselMismatchException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSearchFieldException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidSearchFieldException(InvalidSearchFieldException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPersonTypeException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidPersonTypeException(InvalidPersonTypeException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "NOT_FOUND",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidFieldException(InvalidFieldException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImportException.class)
    public ResponseEntity<ExceptionResponseDto> handleImportException(ImportException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(new ErrorDto(e.getMessage(), e.getClass().getSimpleName())),
                "CONFLICT",
                Timestamp.from(Instant.now())
        );
        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.CONFLICT);
    }


}
