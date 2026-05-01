package com.teamtaskmanager.dashboard;

import com.teamtaskmanager.dashboard.dto.DashboardPerUserResponse;
import com.teamtaskmanager.dashboard.dto.DashboardSummaryResponse;
import com.teamtaskmanager.dashboard.dto.UserTaskCount;
import com.teamtaskmanager.exception.ForbiddenException;
import com.teamtaskmanager.membership.ProjectMember;
import com.teamtaskmanager.membership.ProjectMemberRepository;
import com.teamtaskmanager.membership.Role;
import com.teamtaskmanager.task.Task;
import com.teamtaskmanager.task.TaskRepository;
import com.teamtaskmanager.task.TaskStatus;
import com.teamtaskmanager.user.UserMapper;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  private final ProjectMemberRepository projectMemberRepository;
  private final TaskRepository taskRepository;
  private final UserMapper userMapper;

  public DashboardService(
      ProjectMemberRepository projectMemberRepository,
      TaskRepository taskRepository,
      UserMapper userMapper) {
    this.projectMemberRepository = projectMemberRepository;
    this.taskRepository = taskRepository;
    this.userMapper = userMapper;
  }

  public DashboardSummaryResponse getSummary(UUID userId) {
    List<Task> tasks = getVisibleTasks(userId);

    DashboardSummaryResponse response = new DashboardSummaryResponse();
    if (tasks.isEmpty()) {
      response.setTotalTasks(0);
      response.setOverdueTasks(0);
      response.setTasksByStatus(Map.of());
      return response;
    }

    response.setTotalTasks(tasks.size());

    Map<String, Long> byStatus =
        tasks.stream()
            .collect(Collectors.groupingBy(task -> task.getStatus().name(), Collectors.counting()));
    response.setTasksByStatus(byStatus);

    long overdue =
        tasks.stream()
            .filter(
                task ->
                    task.getDueDate() != null
                        && task.getDueDate().isBefore(LocalDate.now())
                        && task.getStatus() != TaskStatus.DONE)
            .count();
    response.setOverdueTasks(overdue);
    return response;
  }

  public DashboardPerUserResponse getTasksPerUser(UUID userId, UUID projectId) {
    List<Task> tasks;
    if (projectId != null) {
      ProjectMember membership =
          projectMemberRepository
              .findByProjectIdAndUserId(projectId, userId)
              .orElseThrow(() -> new ForbiddenException("Project is not visible"));
      tasks = taskRepository.findByProjectIdIn(List.of(projectId));
      if (membership.getRole() == Role.MEMBER) {
        tasks =
            tasks.stream()
                .filter(task -> task.getAssignee() != null && userId.equals(task.getAssignee().getId()))
                .toList();
      }
    } else {
      tasks = getVisibleTasks(userId);
    }

    Map<UUID, List<Task>> grouped =
        tasks.stream()
            .filter(task -> task.getAssignee() != null)
            .collect(Collectors.groupingBy(task -> task.getAssignee().getId()));

    List<UserTaskCount> counts =
        grouped.entrySet().stream()
            .map(
                entry -> {
                  UserTaskCount count = new UserTaskCount();
                  count.setUser(userMapper.toDto(entry.getValue().get(0).getAssignee()));
                  count.setCount(entry.getValue().size());
                  return count;
                })
            .sorted(Comparator.comparingLong(UserTaskCount::getCount).reversed())
            .toList();

    DashboardPerUserResponse response = new DashboardPerUserResponse();
    response.setItems(counts);
    return response;
  }

  private List<Task> getVisibleTasks(UUID userId) {
    List<ProjectMember> memberships = projectMemberRepository.findByUserId(userId);
    if (memberships.isEmpty()) {
      return List.of();
    }

    List<UUID> adminProjectIds =
        memberships.stream()
            .filter(member -> member.getRole() == Role.ADMIN)
            .map(member -> member.getProject().getId())
            .distinct()
            .toList();
    List<UUID> memberProjectIds =
        memberships.stream()
            .filter(member -> member.getRole() == Role.MEMBER)
            .map(member -> member.getProject().getId())
            .distinct()
            .toList();

    List<Task> adminTasks =
        adminProjectIds.isEmpty() ? List.of() : taskRepository.findByProjectIdIn(adminProjectIds);
    List<Task> memberTasks =
        memberProjectIds.isEmpty()
            ? List.of()
            : taskRepository.findByProjectIdIn(memberProjectIds).stream()
                .filter(task -> task.getAssignee() != null && userId.equals(task.getAssignee().getId()))
                .toList();

    return java.util.stream.Stream.concat(adminTasks.stream(), memberTasks.stream()).toList();
  }
}
