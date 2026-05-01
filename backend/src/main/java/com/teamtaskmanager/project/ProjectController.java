package com.teamtaskmanager.project;

import com.teamtaskmanager.membership.ProjectMember;
import com.teamtaskmanager.project.dto.AddMemberRequest;
import com.teamtaskmanager.project.dto.ProjectMemberResponse;
import com.teamtaskmanager.project.dto.ProjectRequest;
import com.teamtaskmanager.project.dto.ProjectResponse;
import com.teamtaskmanager.project.dto.UpdateMemberRoleRequest;
import com.teamtaskmanager.security.UserPrincipal;
import com.teamtaskmanager.user.UserMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
  private final ProjectService projectService;
  private final ProjectMapper projectMapper;
  private final UserMapper userMapper;

  public ProjectController(
      ProjectService projectService, ProjectMapper projectMapper, UserMapper userMapper) {
    this.projectService = projectService;
    this.projectMapper = projectMapper;
    this.userMapper = userMapper;
  }

  @GetMapping
  public List<ProjectResponse> getProjects(@AuthenticationPrincipal UserPrincipal principal) {
    return projectService.getProjects(principal.getId()).stream()
        .map(
            project -> {
              ProjectResponse response = projectMapper.toResponse(project);
              response.setCurrentUserRole(
                  projectService.getUserRole(project.getId(), principal.getId()).name());
              return response;
            })
        .toList();
  }

  @GetMapping("/{projectId}")
  @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
  public ProjectResponse getProject(
      @PathVariable UUID projectId, @AuthenticationPrincipal UserPrincipal principal) {
    Project project = projectService.getProject(projectId);
    ProjectResponse response = projectMapper.toResponse(project);
    response.setCurrentUserRole(projectService.getUserRole(projectId, principal.getId()).name());
    return response;
  }

  @PostMapping
  public ProjectResponse createProject(
      @AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody ProjectRequest request) {
    Project project = projectService.createProject(principal.getId(), request);
    ProjectResponse response = projectMapper.toResponse(project);
    response.setCurrentUserRole("ADMIN");
    return response;
  }

  @PutMapping("/{projectId}")
  @PreAuthorize("@projectSecurity.isProjectAdmin(#projectId, authentication)")
  public ProjectResponse updateProject(
      @PathVariable UUID projectId, @Valid @RequestBody ProjectRequest request) {
    Project project = projectService.updateProject(projectId, request);
    return projectMapper.toResponse(project);
  }

  @DeleteMapping("/{projectId}")
  @PreAuthorize("@projectSecurity.isProjectAdmin(#projectId, authentication)")
  public void deleteProject(@PathVariable UUID projectId) {
    projectService.deleteProject(projectId);
  }

  @PostMapping("/{projectId}/members")
  @PreAuthorize("@projectSecurity.isProjectAdmin(#projectId, authentication)")
  public ProjectMemberResponse addMember(
      @PathVariable UUID projectId, @Valid @RequestBody AddMemberRequest request) {
    ProjectMember member = projectService.addMember(projectId, request);
    return toMemberResponse(member);
  }

  @GetMapping("/{projectId}/members")
  @PreAuthorize("@projectSecurity.isProjectMember(#projectId, authentication)")
  public List<ProjectMemberResponse> getMembers(@PathVariable UUID projectId) {
    return projectService.getMembers(projectId).stream().map(this::toMemberResponse).toList();
  }

  @PutMapping("/{projectId}/members/{userId}")
  @PreAuthorize("@projectSecurity.isProjectAdmin(#projectId, authentication)")
  public ProjectMemberResponse updateMemberRole(
      @PathVariable UUID projectId,
      @PathVariable UUID userId,
      @Valid @RequestBody UpdateMemberRoleRequest request) {
    return toMemberResponse(projectService.updateMemberRole(projectId, userId, request));
  }

  @DeleteMapping("/{projectId}/members/{userId}")
  @PreAuthorize("@projectSecurity.isProjectAdmin(#projectId, authentication)")
  public void removeMember(@PathVariable UUID projectId, @PathVariable UUID userId) {
    projectService.removeMember(projectId, userId);
  }

  private ProjectMemberResponse toMemberResponse(ProjectMember member) {
    ProjectMemberResponse response = new ProjectMemberResponse();
    response.setId(member.getId());
    response.setUser(userMapper.toDto(member.getUser()));
    response.setRole(member.getRole().name());
    response.setJoinedAt(member.getJoinedAt());
    return response;
  }
}
