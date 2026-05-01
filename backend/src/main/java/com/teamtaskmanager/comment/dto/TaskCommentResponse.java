package com.teamtaskmanager.comment.dto;

import com.teamtaskmanager.user.UserDto;
import java.time.Instant;
import java.util.UUID;

public class TaskCommentResponse {
  private UUID id;
  private UUID taskId;
  private UserDto author;
  private String content;
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getTaskId() {
    return taskId;
  }

  public void setTaskId(UUID taskId) {
    this.taskId = taskId;
  }

  public UserDto getAuthor() {
    return author;
  }

  public void setAuthor(UserDto author) {
    this.author = author;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
