package edu.cit.ogue.groupexpensetracker.features.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 AUTH
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // 👥 ADMIN / USER MANAGEMENT
    List<User> findByRole(String role);

    // 🔍 SEARCH (useful for UI / admin panel)
    List<User> findByFirstnameContainingIgnoreCase(String firstname);
    List<User> findByLastnameContainingIgnoreCase(String lastname);

}

