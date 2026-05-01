package com.teamtaskmanager.exception;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {
  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void genericErrorsDoNotExposeInternalExceptionMessages() {
    HttpServletRequest request = new MockHttpServletRequest("GET", "/api/projects");

    var response = handler.handleGeneric(new RuntimeException("database password leaked"), request);

    assertThat(response.getStatusCode().value()).isEqualTo(500);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getMessage()).isEqualTo("Unexpected server error");
  }

  @Test
  void validationStyleErrorsRemainClientVisible() {
    HttpServletRequest request = new MockHttpServletRequest("GET", "/api/tasks/not-a-uuid");

    var response = handler.handleMalformedRequest(new IllegalArgumentException("bad uuid"), request);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getMessage()).isEqualTo("Invalid request value");
  }
}
