package com.teamtaskmanager.dashboard.dto;

import java.util.Map;

public class DashboardSummaryResponse {
  private long totalTasks;
  private long overdueTasks;
  private Map<String, Long> tasksByStatus;

  public long getTotalTasks() {
    return totalTasks;
  }

  public void setTotalTasks(long totalTasks) {
    this.totalTasks = totalTasks;
  }

  public long getOverdueTasks() {
    return overdueTasks;
  }

  public void setOverdueTasks(long overdueTasks) {
    this.overdueTasks = overdueTasks;
  }

  public Map<String, Long> getTasksByStatus() {
    return tasksByStatus;
  }

  public void setTasksByStatus(Map<String, Long> tasksByStatus) {
    this.tasksByStatus = tasksByStatus;
  }
}
