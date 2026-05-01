package com.teamtaskmanager.task;

import com.teamtaskmanager.security.UserPrincipal;
import com.teamtaskmanager.task.dto.TaskRequest;
import com.teamtaskmanager.task.dto.TaskResponse;
import com.teamtaskmanager.task.dto.TaskStatusUpdateRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @GetMapping("/projects/{projectId}/tasks")
  @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
  public List<TaskResponse> getTasks(
      @PathVariable UUID projectId,
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestParam(required = false) TaskStatus status,
      @RequestParam(required = false) UUID assigneeId,
      @RequestParam(required = false) TaskPriority priority,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueAfter,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore) {
    return taskService.getTasks(
            projectId, principal.getId(), status, assigneeId, priority, search, dueAfter, dueBefore)
        .stream()
        .map(taskMapper::toResponse)
        .toList();
  }

  @PostMapping("/projects/{projectId}/tasks")
  @PreAuthorize("@projectSecurity.isProjectAdmin(#projectId, authentication)")
  public TaskResponse createTask(
      @PathVariable UUID projectId,
      @AuthenticationPrincipal UserPrincipal principal,
      @Valid @RequestBody TaskRequest request) {
    return taskMapper.toResponse(taskService.createTask(projectId, principal.getId(), request));
  }

  @GetMapping("/tasks/{taskId}")
  @PreAuthorize("@projectSecurity.isTaskAdminOrAssignee(#taskId, authentication)")
  public TaskResponse getTask(@PathVariable UUID taskId) {
    return taskMapper.toResponse(taskService.getTask(taskId));
  }

  @PutMapping("/tasks/{taskId}")
  @PreAuthorize("@projectSecurity.isTaskAdmin(#taskId, authentication)")
  public TaskResponse updateTask(
      @PathVariable UUID taskId, @Valid @RequestBody TaskRequest request) {
    return taskMapper.toResponse(taskService.updateTask(taskId, request));
  }

  @PatchMapping("/tasks/{taskId}/status")
  @PreAuthorize("@projectSecurity.isTaskAdminOrAssignee(#taskId, authentication)")
  public TaskResponse updateStatus(
      @PathVariable UUID taskId, @Valid @RequestBody TaskStatusUpdateRequest request) {
    return taskMapper.toResponse(taskService.updateStatus(taskId, request.getStatus()));
  }

  @DeleteMapping("/tasks/{taskId}")
  @PreAuthorize("@projectSecurity.isTaskAdmin(#taskId, authentication)")
  public void deleteTask(@PathVariable UUID taskId) {
    taskService.deleteTask(taskId);
  }
}
