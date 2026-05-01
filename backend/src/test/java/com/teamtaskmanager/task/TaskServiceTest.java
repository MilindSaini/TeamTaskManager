package com.teamtaskmanager.task;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teamtaskmanager.exception.BadRequestException;
import com.teamtaskmanager.exception.ForbiddenException;
import com.teamtaskmanager.membership.ProjectMemberRepository;
import com.teamtaskmanager.membership.Role;
import com.teamtaskmanager.project.Project;
import com.teamtaskmanager.project.ProjectService;
import com.teamtaskmanager.task.dto.TaskRequest;
import com.teamtaskmanager.user.User;
import com.teamtaskmanager.user.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

class TaskServiceTest {
  private final TaskRepository taskRepository = org.mockito.Mockito.mock(TaskRepository.class);
  private final ProjectService projectService = org.mockito.Mockito.mock(ProjectService.class);
  private final UserService userService = org.mockito.Mockito.mock(UserService.class);
  private final ProjectMemberRepository projectMemberRepository =
      org.mockito.Mockito.mock(ProjectMemberRepository.class);
  private final TaskService taskService =
      new TaskService(taskRepository, projectService, userService, projectMemberRepository);

  @Test
  void memberCannotFilterTasksForAnotherAssignee() {
    UUID projectId = UUID.randomUUID();
    UUID memberId = UUID.randomUUID();
    UUID otherUserId = UUID.randomUUID();
    when(projectService.getUserRole(projectId, memberId)).thenReturn(Role.MEMBER);

    assertThatThrownBy(
            () ->
                taskService.getTasks(
                    projectId, memberId, null, otherUserId, null, null, null, null))
        .isInstanceOf(ForbiddenException.class)
        .hasMessageContaining("Members can only view their assigned tasks");
  }

  @Test
  void createTaskRejectsAssigneeOutsideProject() {
    UUID projectId = UUID.randomUUID();
    UUID creatorId = UUID.randomUUID();
    UUID assigneeId = UUID.randomUUID();

    when(projectService.getProject(projectId)).thenReturn(project(projectId));
    when(userService.getById(creatorId)).thenReturn(user(creatorId));
    when(projectMemberRepository.existsByProjectIdAndUserId(projectId, assigneeId)).thenReturn(false);

    TaskRequest request = new TaskRequest();
    request.setTitle("Plan release");
    request.setAssigneeId(assigneeId);

    assertThatThrownBy(() -> taskService.createTask(projectId, creatorId, request))
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Assignee must be a project member");
  }

  @Test
  void createTaskUsesSafeDefaults() {
    UUID projectId = UUID.randomUUID();
    UUID creatorId = UUID.randomUUID();

    when(projectService.getProject(projectId)).thenReturn(project(projectId));
    when(userService.getById(creatorId)).thenReturn(user(creatorId));
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    TaskRequest request = new TaskRequest();
    request.setTitle("Write tests");

    taskService.createTask(projectId, creatorId, request);

    ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
    verify(taskRepository).save(captor.capture());
    org.assertj.core.api.Assertions.assertThat(captor.getValue().getStatus()).isEqualTo(TaskStatus.TODO);
    org.assertj.core.api.Assertions.assertThat(captor.getValue().getPriority()).isEqualTo(TaskPriority.MEDIUM);
  }

  @SuppressWarnings("unchecked")
  @Test
  void adminCanFilterTasksForAnyAssignee() {
    UUID projectId = UUID.randomUUID();
    UUID adminId = UUID.randomUUID();
    UUID assigneeId = UUID.randomUUID();
    when(projectService.getUserRole(projectId, adminId)).thenReturn(Role.ADMIN);

    taskService.getTasks(projectId, adminId, null, assigneeId, null, null, null, null);

    verify(taskRepository).findAll(any(Specification.class));
  }

  private static Project project(UUID id) {
    Project project = new Project();
    project.setId(id);
    project.setName("Project");
    project.setOwner(user(UUID.randomUUID()));
    return project;
  }

  private static User user(UUID id) {
    User user = new User();
    user.setId(id);
    user.setName("User");
    user.setEmail(id + "@example.com");
    user.setPasswordHash("hash");
    return user;
  }
}
