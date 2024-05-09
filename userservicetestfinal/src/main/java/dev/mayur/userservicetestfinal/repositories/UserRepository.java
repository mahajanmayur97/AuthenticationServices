package dev.mayur.userservicetestfinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.mayur.userservicetestfinal.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
