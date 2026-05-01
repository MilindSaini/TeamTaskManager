package com.teamtaskmanager.user;

import com.teamtaskmanager.exception.NotFoundException;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User getById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
  }

  public User getByEmail(String email) {
    return userRepository
        .findByEmailIgnoreCase(email.trim())
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public User createUser(String name, String email, String rawPassword) {
    User user = new User();
    user.setName(name.trim());
    user.setEmail(email.trim().toLowerCase());
    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    return userRepository.save(user);
  }
}
