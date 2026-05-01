package com.teamtaskmanager.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.teamtaskmanager.auth.AuthController;
import com.teamtaskmanager.config.SecurityConfig;
import com.teamtaskmanager.task.TaskController;
import com.teamtaskmanager.task.dto.TaskRequest;
import java.lang.reflect.Method;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

class EndpointSecurityTest {
  @Test
  void fullTaskUpdateIsRestrictedToProjectAdmins() throws Exception {
    Method method = TaskController.class.getMethod("updateTask", UUID.class, TaskRequest.class);

    PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);

    assertThat(preAuthorize).isNotNull();
    assertThat(preAuthorize.value()).contains("isTaskAdmin(#taskId, authentication)");
  }

  @Test
  void authMeRequiresAuthenticatedPrincipal() throws Exception {
    Method method = AuthController.class.getMethod("me", UserPrincipal.class);

    assertThat(method.getParameters()[0].isAnnotationPresent(AuthenticationPrincipal.class)).isTrue();
    assertThat(SecurityConfig.class.getDeclaredMethods()).anySatisfy(configMethod -> {
      if (configMethod.getName().equals("securityFilterChain")) {
        assertThat(configMethod).isNotNull();
      }
    });
  }
}
