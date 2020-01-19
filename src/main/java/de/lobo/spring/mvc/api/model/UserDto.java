package de.lobo.spring.mvc.api.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserDto {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
}
