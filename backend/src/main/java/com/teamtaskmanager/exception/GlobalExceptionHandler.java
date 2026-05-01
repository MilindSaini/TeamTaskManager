package com.teamtaskmanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest request) {
    return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(
      BadRequestException ex, HttpServletRequest request) {
    return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiError> handleForbidden(
      ForbiddenException ex, HttpServletRequest request) {
    return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> handleAccessDenied(
      AccessDeniedException ex, HttpServletRequest request) {
    return buildError(HttpStatus.FORBIDDEN, "Access denied", request.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ApiError> handleMalformedRequest(Exception ex, HttpServletRequest request) {
    return buildError(HttpStatus.BAD_REQUEST, "Invalid request value", request.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
    return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request.getRequestURI());
  }

  private ResponseEntity<ApiError> buildError(
      HttpStatus status, String message, String path) {
    ApiError error = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path);
    return ResponseEntity.status(status).body(error);
  }
}
