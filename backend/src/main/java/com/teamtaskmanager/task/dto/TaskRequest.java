package com.teamtaskmanager.task.dto;

import com.teamtaskmanager.task.TaskPriority;
import com.teamtaskmanager.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

public class TaskRequest {
  @NotBlank
  @Size(max = 240)
  private String title;

  @Size(max = 4000)
  private String description;

  private UUID assigneeId;

  private TaskStatus status;

  private TaskPriority priority;

  private LocalDate dueDate;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public UUID getAssigneeId() {
    return assigneeId;
  }

  public void setAssigneeId(UUID assigneeId) {
    this.assigneeId = assigneeId;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public TaskPriority getPriority() {
    return priority;
  }

  public void setPriority(TaskPriority priority) {
    this.priority = priority;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }
}
