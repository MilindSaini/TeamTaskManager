package com.teamtaskmanager.task;

import com.teamtaskmanager.project.Project;
import com.teamtaskmanager.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @Column(nullable = false)
  private String title;

  @Column
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskPriority priority;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  public void onCreate() {
    createdAt = Instant.now();
    updatedAt = createdAt;
  }

  @PreUpdate
  public void onUpdate() {
    updatedAt = Instant.now();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public User getAssignee() {
    return assignee;
  }

  public void setAssignee(User assignee) {
    this.assignee = assignee;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
