package com.teamtaskmanager.auth;

import com.teamtaskmanager.auth.dto.LoginRequest;
import com.teamtaskmanager.auth.dto.LoginResponse;
import com.teamtaskmanager.auth.dto.RegisterRequest;
import com.teamtaskmanager.config.JwtService;
import com.teamtaskmanager.exception.BadRequestException;
import com.teamtaskmanager.security.UserPrincipal;
import com.teamtaskmanager.user.User;
import com.teamtaskmanager.user.UserMapper;
import com.teamtaskmanager.user.UserRepository;
import com.teamtaskmanager.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserService userService;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  public AuthService(
      UserService userService,
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      JwtService jwtService,
      UserMapper userMapper) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userMapper = userMapper;
  }

  public LoginResponse register(RegisterRequest request) {
    String email = request.getEmail().trim();
    if (userRepository.existsByEmailIgnoreCase(email)) {
      throw new BadRequestException("Email already in use");
    }
    User user = userService.createUser(request.getName(), email, request.getPassword());
    String token = jwtService.generateToken(UserPrincipal.from(user));
    return new LoginResponse(token, userMapper.toDto(user));
  }

  public LoginResponse login(LoginRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail().trim(), request.getPassword()));
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    String token = jwtService.generateToken(principal);
    User user = userService.getById(principal.getId());
    return new LoginResponse(token, userMapper.toDto(user));
  }
}
