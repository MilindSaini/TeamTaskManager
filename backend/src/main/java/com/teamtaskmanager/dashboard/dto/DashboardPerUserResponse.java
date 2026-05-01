package com.teamtaskmanager.dashboard.dto;

import java.util.List;

public class DashboardPerUserResponse {
  private List<UserTaskCount> items;

  public List<UserTaskCount> getItems() {
    return items;
  }

  public void setItems(List<UserTaskCount> items) {
    this.items = items;
  }
}
