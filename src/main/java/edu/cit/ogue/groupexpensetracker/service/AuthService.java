package edu.cit.ogue.groupexpensetracker.service;

import edu.cit.ogue.groupexpensetracker.entity.User;
import edu.cit.ogue.groupexpensetracker.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {

        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!encoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}