package com.teamtaskmanager.task;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {
  public static Specification<Task> hasProjectId(UUID projectId) {
    return (root, query, cb) -> cb.equal(root.get("project").get("id"), projectId);
  }

  public static Specification<Task> hasStatus(TaskStatus status) {
    return (root, query, cb) -> cb.equal(root.get("status"), status);
  }

  public static Specification<Task> hasAssignee(UUID assigneeId) {
    return (root, query, cb) -> cb.equal(root.get("assignee").get("id"), assigneeId);
  }

  public static Specification<Task> hasPriority(TaskPriority priority) {
    return (root, query, cb) -> cb.equal(root.get("priority"), priority);
  }

  public static Specification<Task> search(String text) {
    return (root, query, cb) -> {
      String like = "%" + text.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("title")), like),
          cb.like(cb.lower(root.get("description")), like));
    };
  }

  public static Specification<Task> dueAfter(LocalDate date) {
    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dueDate"), date);
  }

  public static Specification<Task> dueBefore(LocalDate date) {
    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueDate"), date);
  }
}
