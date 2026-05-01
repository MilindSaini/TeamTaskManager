package com.teamtaskmanager.dashboard;

import com.teamtaskmanager.dashboard.dto.DashboardPerUserResponse;
import com.teamtaskmanager.dashboard.dto.DashboardSummaryResponse;
import com.teamtaskmanager.security.UserPrincipal;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @GetMapping("/summary")
  public DashboardSummaryResponse getSummary(@AuthenticationPrincipal UserPrincipal principal) {
    return dashboardService.getSummary(principal.getId());
  }

  @GetMapping("/per-user")
  public DashboardPerUserResponse getPerUser(
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestParam(required = false) UUID projectId) {
    return dashboardService.getTasksPerUser(principal.getId(), projectId);
  }
}
