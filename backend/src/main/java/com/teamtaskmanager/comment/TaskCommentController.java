package com.teamtaskmanager.comment;

import com.teamtaskmanager.comment.dto.TaskCommentRequest;
import com.teamtaskmanager.comment.dto.TaskCommentResponse;
import com.teamtaskmanager.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class TaskCommentController {
  private final TaskCommentService commentService;
  private final TaskCommentMapper commentMapper;

  public TaskCommentController(TaskCommentService commentService, TaskCommentMapper commentMapper) {
    this.commentService = commentService;
    this.commentMapper = commentMapper;
  }

  @GetMapping
  @PreAuthorize("@projectSecurity.isTaskAdminOrAssignee(#taskId, authentication)")
  public List<TaskCommentResponse> getComments(@PathVariable UUID taskId) {
    return commentService.getComments(taskId).stream().map(commentMapper::toResponse).toList();
  }

  @PostMapping
  @PreAuthorize("@projectSecurity.isTaskAdminOrAssignee(#taskId, authentication)")
  public TaskCommentResponse addComment(
      @PathVariable UUID taskId,
      @AuthenticationPrincipal UserPrincipal principal,
      @Valid @RequestBody TaskCommentRequest request) {
    return commentMapper.toResponse(commentService.addComment(taskId, principal.getId(), request));
  }
}
