package de.lobo.spring.mvc.service;

import de.lobo.spring.mvc.api.exception.IdNotFoundException;
import de.lobo.spring.mvc.api.model.UserDto;
import de.lobo.spring.mvc.service.model.User;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DemoService {

  private final UserRepository userRepository;

  public List<User> listAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    Objects.requireNonNull(id, "id must not be null");
    return userRepository.findById(id)
        .orElseThrow(
            () -> new IdNotFoundException(id, String.format("user not found for id %d", id)));
  }

  public User saveUser(UserDto dto) {
    Objects.requireNonNull(dto, "dto must not be null");
    return userRepository.save(User.of(dto));
  }

  public User updateUser(Long id, UserDto dto) {
    var user = userRepository.findById(id).orElseThrow(
        () -> new IdNotFoundException(id, String.format("user not found on id %d", id))
        );

    user = user.update(dto);
    return userRepository.saveAndFlush(user);
  }

  public void deleteUser(Long id) {
    Objects.requireNonNull(id, "id must not be null");
    userRepository.deleteById(id);
  }
}
