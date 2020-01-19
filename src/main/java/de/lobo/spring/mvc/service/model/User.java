package de.lobo.spring.mvc.service.model;


import de.lobo.spring.mvc.api.model.UserDto;
import java.time.LocalDate;
import java.time.Period;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class User {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Setter(AccessLevel.NONE)
  private Long id;

  private String firstName;
  private String lastName;
  private Integer age;

  public User update(UserDto dto) {
    return this.toBuilder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .age(calculateAge(dto.getDateOfBirth()))
        .build();
  }

  public static User of(UserDto dto) {
    return User.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .age(calculateAge(dto.getDateOfBirth()))
        .build();
  }

  private static Integer calculateAge(LocalDate dateOfBirth) {
    return Period.between(dateOfBirth, LocalDate.now()).getYears();
  }
}
