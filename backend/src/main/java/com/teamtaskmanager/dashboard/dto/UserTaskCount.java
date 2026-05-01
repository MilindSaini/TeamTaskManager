package com.teamtaskmanager.dashboard.dto;

import com.teamtaskmanager.user.UserDto;

public class UserTaskCount {
  private UserDto user;
  private long count;

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
