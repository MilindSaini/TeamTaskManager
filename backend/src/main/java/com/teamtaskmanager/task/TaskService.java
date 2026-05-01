package com.teamtaskmanager.task;

import com.teamtaskmanager.exception.BadRequestException;
import com.teamtaskmanager.exception.ForbiddenException;
import com.teamtaskmanager.exception.NotFoundException;
import com.teamtaskmanager.membership.ProjectMemberRepository;
import com.teamtaskmanager.membership.Role;
import com.teamtaskmanager.project.Project;
import com.teamtaskmanager.project.ProjectService;
import com.teamtaskmanager.task.dto.TaskRequest;
import com.teamtaskmanager.user.User;
import com.teamtaskmanager.user.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final ProjectService projectService;
  private final UserService userService;
  private final ProjectMemberRepository projectMemberRepository;

  public TaskService(
      TaskRepository taskRepository,
      ProjectService projectService,
      UserService userService,
      ProjectMemberRepository projectMemberRepository) {
    this.taskRepository = taskRepository;
    this.projectService = projectService;
    this.userService = userService;
    this.projectMemberRepository = projectMemberRepository;
  }

  public List<Task> getTasks(
      UUID projectId,
      UUID currentUserId,
      TaskStatus status,
      UUID assigneeId,
      TaskPriority priority,
      String search,
      LocalDate dueAfter,
      LocalDate dueBefore) {
    Specification<Task> spec = Specification.where(TaskSpecifications.hasProjectId(projectId));
    Role role = projectService.getUserRole(projectId, currentUserId);
    if (role == Role.MEMBER) {
      if (assigneeId != null && !assigneeId.equals(currentUserId)) {
        throw new ForbiddenException("Members can only view their assigned tasks");
      }
      spec = spec.and(TaskSpecifications.hasAssignee(currentUserId));
    } else if (assigneeId != null) {
      spec = spec.and(TaskSpecifications.hasAssignee(assigneeId));
    }
    if (status != null) {
      spec = spec.and(TaskSpecifications.hasStatus(status));
    }
    if (priority != null) {
      spec = spec.and(TaskSpecifications.hasPriority(priority));
    }
    if (search != null && !search.isBlank()) {
      spec = spec.and(TaskSpecifications.search(search));
    }
    if (dueAfter != null) {
      spec = spec.and(TaskSpecifications.dueAfter(dueAfter));
    }
    if (dueBefore != null) {
      spec = spec.and(TaskSpecifications.dueBefore(dueBefore));
    }
    return taskRepository.findAll(spec);
  }

  public Task getTask(UUID taskId) {
    return taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
  }

  public Task createTask(UUID projectId, UUID creatorId, TaskRequest request) {
    Project project = projectService.getProject(projectId);
    User creator = userService.getById(creatorId);

    Task task = new Task();
    task.setProject(project);
    task.setCreatedBy(creator);
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setStatus(request.getStatus() == null ? TaskStatus.TODO : request.getStatus());
    task.setPriority(request.getPriority() == null ? TaskPriority.MEDIUM : request.getPriority());
    task.setDueDate(request.getDueDate());

    if (request.getAssigneeId() != null) {
      assertAssigneeIsMember(projectId, request.getAssigneeId());
      task.setAssignee(userService.getById(request.getAssigneeId()));
    }

    return taskRepository.save(task);
  }

  public Task updateTask(UUID taskId, TaskRequest request) {
    Task task = getTask(taskId);
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setStatus(request.getStatus() == null ? task.getStatus() : request.getStatus());
    task.setPriority(request.getPriority() == null ? task.getPriority() : request.getPriority());
    task.setDueDate(request.getDueDate());

    if (request.getAssigneeId() != null) {
      assertAssigneeIsMember(task.getProject().getId(), request.getAssigneeId());
      task.setAssignee(userService.getById(request.getAssigneeId()));
    }

    return taskRepository.save(task);
  }

  public Task updateStatus(UUID taskId, TaskStatus status) {
    Task task = getTask(taskId);
    task.setStatus(status);
    return taskRepository.save(task);
  }

  public void deleteTask(UUID taskId) {
    Task task = getTask(taskId);
    taskRepository.delete(task);
  }

  private void assertAssigneeIsMember(UUID projectId, UUID assigneeId) {
    if (!projectMemberRepository.existsByProjectIdAndUserId(projectId, assigneeId)) {
      throw new BadRequestException("Assignee must be a project member");
    }
  }
}
