package com.teamtaskmanager.project.dto;

import com.teamtaskmanager.membership.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class AddMemberRequest {
  private UUID userId;

  @Email
  @Size(max = 255)
  private String email;

  private Role role;

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
