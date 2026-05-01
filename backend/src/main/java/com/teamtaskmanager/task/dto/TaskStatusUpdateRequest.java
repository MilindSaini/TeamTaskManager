package com.teamtaskmanager.task.dto;

import com.teamtaskmanager.task.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class TaskStatusUpdateRequest {
  @NotNull
  private TaskStatus status;

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }
}
