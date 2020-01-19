package de.lobo.spring.mvc.service;

import de.lobo.spring.mvc.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
