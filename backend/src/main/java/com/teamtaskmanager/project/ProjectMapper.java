package com.teamtaskmanager.project;

import com.teamtaskmanager.project.dto.ProjectResponse;
import com.teamtaskmanager.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ProjectMapper {
  @Mapping(target = "currentUserRole", ignore = true)
  ProjectResponse toResponse(Project project);
}
