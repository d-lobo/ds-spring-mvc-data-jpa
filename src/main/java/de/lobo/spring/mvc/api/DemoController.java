package de.lobo.spring.mvc.api;

import de.lobo.spring.mvc.api.model.UserDto;
import de.lobo.spring.mvc.service.DemoService;
import de.lobo.spring.mvc.service.model.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DemoController {
  private final DemoService service;

  @GetMapping("/users")
  public List<User> getUsers() {
    return service.listAllUsers();
  }

  @GetMapping("/users/{id}")
  public User getUserById(@PathVariable("id") Long id) {
    return service.getUserById(id);
  }

  @PutMapping("/users")
  public ResponseEntity saveUser(@RequestBody UserDto dto) {
    var user = service.saveUser(dto);
    return ResponseEntity.ok(user.getId());
  }

  @PostMapping("/users/{id}")
  public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody UserDto dto) {
    var user = service.updateUser(id, dto);
    return ResponseEntity.ok(user.getId());
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity updateUser(@PathVariable("id") Long id) {
    service.deleteUser(id);
    return ResponseEntity.ok().build();
  }
}
