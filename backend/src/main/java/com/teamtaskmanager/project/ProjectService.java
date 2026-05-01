package com.teamtaskmanager.project;

import com.teamtaskmanager.exception.BadRequestException;
import com.teamtaskmanager.exception.NotFoundException;
import com.teamtaskmanager.membership.ProjectMember;
import com.teamtaskmanager.membership.ProjectMemberRepository;
import com.teamtaskmanager.membership.Role;
import com.teamtaskmanager.project.dto.AddMemberRequest;
import com.teamtaskmanager.project.dto.ProjectRequest;
import com.teamtaskmanager.project.dto.UpdateMemberRoleRequest;
import com.teamtaskmanager.user.User;
import com.teamtaskmanager.user.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final UserService userService;

  public ProjectService(
      ProjectRepository projectRepository,
      ProjectMemberRepository projectMemberRepository,
      UserService userService) {
    this.projectRepository = projectRepository;
    this.projectMemberRepository = projectMemberRepository;
    this.userService = userService;
  }

  public Project createProject(UUID ownerId, ProjectRequest request) {
    User owner = userService.getById(ownerId);
    Project project = new Project();
    project.setName(request.getName());
    project.setDescription(request.getDescription());
    project.setOwner(owner);

    Project saved = projectRepository.save(project);

    ProjectMember membership = new ProjectMember();
    membership.setProject(saved);
    membership.setUser(owner);
    membership.setRole(Role.ADMIN);
    projectMemberRepository.save(membership);

    return saved;
  }

  public List<Project> getProjects(UUID userId) {
    return projectRepository.findAllByMember(userId);
  }

  public Project getProject(UUID projectId) {
    return projectRepository
        .findById(projectId)
        .orElseThrow(() -> new NotFoundException("Project not found"));
  }

  public Project updateProject(UUID projectId, ProjectRequest request) {
    Project project = getProject(projectId);
    project.setName(request.getName());
    project.setDescription(request.getDescription());
    return projectRepository.save(project);
  }

  public void deleteProject(UUID projectId) {
    Project project = getProject(projectId);
    projectRepository.delete(project);
  }

  public ProjectMember addMember(UUID projectId, AddMemberRequest request) {
    User user = resolveMemberUser(request);

    if (projectMemberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
      throw new BadRequestException("User is already a member");
    }

    Project project = getProject(projectId);

    ProjectMember member = new ProjectMember();
    member.setProject(project);
    member.setUser(user);
    member.setRole(request.getRole() == null ? Role.MEMBER : request.getRole());
    return projectMemberRepository.save(member);
  }

  public void removeMember(UUID projectId, UUID userId) {
    ProjectMember member =
        projectMemberRepository
            .findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new NotFoundException("Member not found"));
    if (member.getProject().getOwner().getId().equals(userId)) {
      throw new BadRequestException("Project owner cannot be removed");
    }
    if (member.getRole() == Role.ADMIN
        && projectMemberRepository.countByProjectIdAndRole(projectId, Role.ADMIN) <= 1) {
      throw new BadRequestException("Project must keep at least one admin");
    }
    projectMemberRepository.delete(member);
  }

  public ProjectMember updateMemberRole(
      UUID projectId, UUID userId, UpdateMemberRoleRequest request) {
    ProjectMember member =
        projectMemberRepository
            .findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new NotFoundException("Member not found"));
    if (member.getRole() == Role.ADMIN
        && request.getRole() != Role.ADMIN
        && projectMemberRepository.countByProjectIdAndRole(projectId, Role.ADMIN) <= 1) {
      throw new BadRequestException("Project must keep at least one admin");
    }
    member.setRole(request.getRole());
    return projectMemberRepository.save(member);
  }

  public List<ProjectMember> getMembers(UUID projectId) {
    getProject(projectId);
    return projectMemberRepository.findByProjectId(projectId);
  }

  public Role getUserRole(UUID projectId, UUID userId) {
    return projectMemberRepository
        .findByProjectIdAndUserId(projectId, userId)
        .map(ProjectMember::getRole)
        .orElseThrow(() -> new NotFoundException("Membership not found"));
  }

  private User resolveMemberUser(AddMemberRequest request) {
    if (request.getUserId() != null) {
      return userService.getById(request.getUserId());
    }
    if (request.getEmail() != null && !request.getEmail().isBlank()) {
      return userService.getByEmail(request.getEmail());
    }
    throw new BadRequestException("Provide a userId or email");
  }
}
