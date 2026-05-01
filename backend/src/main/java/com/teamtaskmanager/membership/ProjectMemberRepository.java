package com.teamtaskmanager.membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {
  Optional<ProjectMember> findByProjectIdAndUserId(UUID projectId, UUID userId);

  boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);

  List<ProjectMember> findByProjectId(UUID projectId);

  List<ProjectMember> findByUserId(UUID userId);

  long countByProjectIdAndRole(UUID projectId, Role role);
}
