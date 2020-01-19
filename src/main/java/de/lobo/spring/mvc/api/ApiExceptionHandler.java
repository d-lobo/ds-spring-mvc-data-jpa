package de.lobo.spring.mvc.api;

import de.lobo.spring.mvc.api.exception.IdNotFoundException;
import java.util.HashSet;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity constraintViolation(ConstraintViolationException exception) {
    var errors = (exception.getConstraintViolations() == null ? new HashSet<ConstraintViolation>()
        : exception.getConstraintViolations());
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(IdNotFoundException.class)
  public ResponseEntity idNotFound(IdNotFoundException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity itemNotModified(EmptyResultDataAccessException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
