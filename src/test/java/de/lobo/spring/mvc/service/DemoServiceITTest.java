package de.lobo.spring.mvc.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.lobo.spring.mvc.api.exception.IdNotFoundException;
import de.lobo.spring.mvc.api.model.UserDto;
import de.lobo.spring.mvc.service.model.User;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //test runs in isolated transactions and get rolled back after test
public class DemoServiceITTest {

  @Autowired
  private DemoService serviceUnderTest;

  static UserDto minimal() {
    return UserDto.builder()
        .firstName("John")
        .lastName("Doe")
        .dateOfBirth(LocalDate.parse("1962-04-15"))
        .build();
  }

  @Test
  public void testSave() {
    var dto = minimal();
    var fromDb = serviceUnderTest.saveUser(dto);

    assertThat(fromDb).isNotNull();
    assertThat(fromDb).isEqualToComparingOnlyGivenFields(dto, "firstName", "lastName");
    assertThat(fromDb.getAge())
        .isEqualTo(Period.between(dto.getDateOfBirth(), LocalDate.now()).getYears());
  }

  @Test
  public void testListUsers() {
    var dto = minimal();
    var size = 1024;

    for (int i = 0; i < size; i++) {
      serviceUnderTest.saveUser(dto);
    }

    var users = serviceUnderTest.listAllUsers();
    assertThat(users).isNotNull();
    assertThat(users.size()).isEqualTo(size);
    //validate distinct ids
    assertThat(users.stream().map(user -> user.getId()).distinct().count()).isEqualTo(size);
  }

  @Test
  public void testGetUserById() {
    var dto = minimal();
    var user = serviceUnderTest.saveUser(dto);

    var userReadFromDb = serviceUnderTest.getUserById(user.getId());

    assertThat(userReadFromDb).isNotNull();
    assertThat(userReadFromDb).isEqualToComparingFieldByField(user);
  }

  @Test
  public void testUpdateUser() {
    var dto = minimal();

    var fromDb = serviceUnderTest.saveUser(dto);

    assertThat(fromDb.getFirstName()).isEqualTo(dto.getFirstName());
    assertThat(fromDb.getLastName()).isEqualTo(dto.getLastName());

    var updateFirstName = "Alan";
    var updateLastName = "Smith";
    var updateBirth = LocalDate.parse("1986-01-02");

    dto = dto.toBuilder()
        .firstName(updateFirstName)
        .lastName(updateLastName)
        .dateOfBirth(updateBirth)
        .build();

    var updated = serviceUnderTest.updateUser(fromDb.getId(), dto);

    assertThat(fromDb.getId()).isEqualTo(updated.getId());
    assertThat(updated.getFirstName()).isEqualTo(updateFirstName);
    assertThat(updated.getLastName()).isEqualTo(updateLastName);
    assertThat(updated.getAge()).isEqualTo(Period.between(updateBirth, LocalDate.now()).getYears());
  }

  @Test
  public void testDeleteUser() {
    var dto = minimal();
    var size = 5;
    List<User> stored = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      stored.add(serviceUnderTest.saveUser(dto));
    }
    assertThat(stored.size()).isEqualTo(size);
    Collections.reverse(stored);

    stored.stream()
        .forEach(item -> serviceUnderTest.deleteUser(item.getId()));

    var leftInDb = serviceUnderTest.listAllUsers();

    assertThat(leftInDb.size()).isEqualTo(0);
  }

  @Test(expected = IdNotFoundException.class)
  public void testGetUserById_shouldFail() {
    var id = 1023L;
    serviceUnderTest.getUserById(id);
  }

  @Test(expected = IdNotFoundException.class)
  public void testUpdateUserById_shouldFail() {
    var id = 1023L;
    var dto = minimal();
    serviceUnderTest.updateUser(id, dto);
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void testDeleteUserById_shouldFail() {
    var id = 1023L;
    serviceUnderTest.deleteUser(id);
  }


}
