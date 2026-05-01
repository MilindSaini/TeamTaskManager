package com.teamtaskmanager.project.dto;

import com.teamtaskmanager.user.UserDto;
import java.time.Instant;
import java.util.UUID;

public class ProjectMemberResponse {
  private UUID id;
  private UserDto user;
  private String role;
  private Instant joinedAt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Instant getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(Instant joinedAt) {
    this.joinedAt = joinedAt;
  }
}
