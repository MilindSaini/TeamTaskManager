package com.teamtaskmanager.security;

import com.teamtaskmanager.membership.ProjectMember;
import com.teamtaskmanager.membership.ProjectMemberRepository;
import com.teamtaskmanager.membership.Role;
import com.teamtaskmanager.task.Task;
import com.teamtaskmanager.task.TaskRepository;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("projectSecurity")
public class ProjectSecurity {
  private final ProjectMemberRepository projectMemberRepository;
  private final TaskRepository taskRepository;

  public ProjectSecurity(
      ProjectMemberRepository projectMemberRepository, TaskRepository taskRepository) {
    this.projectMemberRepository = projectMemberRepository;
    this.taskRepository = taskRepository;
  }

  public boolean isProjectMember(UUID projectId, Authentication authentication) {
    UUID userId = extractUserId(authentication);
    if (userId == null) {
      return false;
    }
    return projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);
  }

  public boolean isProjectAdmin(UUID projectId, Authentication authentication) {
    UUID userId = extractUserId(authentication);
    if (userId == null) {
      return false;
    }
    return projectMemberRepository
        .findByProjectIdAndUserId(projectId, userId)
        .map(ProjectMember::getRole)
        .filter(role -> role == Role.ADMIN)
        .isPresent();
  }

  public boolean isTaskMember(UUID taskId, Authentication authentication) {
    UUID userId = extractUserId(authentication);
    if (userId == null) {
      return false;
    }
    return taskRepository
        .findById(taskId)
        .map(task -> projectMemberRepository.existsByProjectIdAndUserId(task.getProject().getId(), userId))
        .orElse(false);
  }

  public boolean isTaskAdmin(UUID taskId, Authentication authentication) {
    UUID userId = extractUserId(authentication);
    if (userId == null) {
      return false;
    }
    return taskRepository
        .findById(taskId)
        .map(task ->
            projectMemberRepository
                .findByProjectIdAndUserId(task.getProject().getId(), userId)
                .map(ProjectMember::getRole)
                .filter(role -> role == Role.ADMIN)
                .isPresent())
        .orElse(false);
  }

  public boolean isTaskAdminOrAssignee(UUID taskId, Authentication authentication) {
    UUID userId = extractUserId(authentication);
    if (userId == null) {
      return false;
    }
    return taskRepository
        .findById(taskId)
        .map(task -> {
          boolean isAssignee = task.getAssignee() != null && userId.equals(task.getAssignee().getId());
          boolean isAdmin =
              projectMemberRepository
                  .findByProjectIdAndUserId(task.getProject().getId(), userId)
                  .map(ProjectMember::getRole)
                  .filter(role -> role == Role.ADMIN)
                  .isPresent();
          return isAssignee || isAdmin;
        })
        .orElse(false);
  }

  private UUID extractUserId(Authentication authentication) {
    if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
      return null;
    }
    return ((UserPrincipal) authentication.getPrincipal()).getId();
  }
}
