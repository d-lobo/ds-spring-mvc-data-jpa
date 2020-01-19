package de.lobo.spring.mvc.api.exception;

import lombok.Data;

@Data
public class IdNotFoundException extends IllegalArgumentException {
  private Long id;

  public IdNotFoundException(Long id, String message, Throwable cause) {
    super(message, cause);
    this.id = id;
  }

  public IdNotFoundException(Long id, String message) {
    super(message);
    this.id = id;
  }
}
