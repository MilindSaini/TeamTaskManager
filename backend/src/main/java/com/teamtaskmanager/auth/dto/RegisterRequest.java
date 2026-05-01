package com.teamtaskmanager.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
  @NotBlank
  @Size(max = 120)
  private String name;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Size(min = 8, max = 100)
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
      message = "must include at least one letter and one number")
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
