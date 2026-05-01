package com.teamtaskmanager.project.dto;

import com.teamtaskmanager.membership.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateMemberRoleRequest {
  @NotNull
  private Role role;

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
