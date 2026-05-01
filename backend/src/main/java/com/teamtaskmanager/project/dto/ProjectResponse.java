package com.teamtaskmanager.project.dto;

import com.teamtaskmanager.user.UserDto;
import java.time.Instant;
import java.util.UUID;

public class ProjectResponse {
  private UUID id;
  private String name;
  private String description;
  private UserDto owner;
  private Instant createdAt;
  private String currentUserRole;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public UserDto getOwner() {
    return owner;
  }

  public void setOwner(UserDto owner) {
    this.owner = owner;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public String getCurrentUserRole() {
    return currentUserRole;
  }

  public void setCurrentUserRole(String currentUserRole) {
    this.currentUserRole = currentUserRole;
  }
}
