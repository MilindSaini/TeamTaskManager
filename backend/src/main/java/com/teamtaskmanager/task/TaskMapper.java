package com.teamtaskmanager.task;

import com.teamtaskmanager.task.dto.TaskResponse;
import com.teamtaskmanager.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {
  @Mapping(target = "projectId", source = "project.id")
  TaskResponse toResponse(Task task);
}
