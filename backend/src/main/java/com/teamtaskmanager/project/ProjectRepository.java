package com.teamtaskmanager.project;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
  @Query(
      "select p from Project p join ProjectMember pm on pm.project = p where pm.user.id = :userId")
  List<Project> findAllByMember(@Param("userId") UUID userId);
}
