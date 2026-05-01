package com.teamtaskmanager.comment;

import com.teamtaskmanager.comment.dto.TaskCommentResponse;
import com.teamtaskmanager.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskCommentMapper {
  @Mapping(target = "taskId", source = "task.id")
  TaskCommentResponse toResponse(TaskComment comment);
}
