package com.teamtaskmanager.comment;

import com.teamtaskmanager.comment.dto.TaskCommentRequest;
import com.teamtaskmanager.task.Task;
import com.teamtaskmanager.task.TaskService;
import com.teamtaskmanager.user.User;
import com.teamtaskmanager.user.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TaskCommentService {
  private final TaskCommentRepository commentRepository;
  private final TaskService taskService;
  private final UserService userService;

  public TaskCommentService(
      TaskCommentRepository commentRepository, TaskService taskService, UserService userService) {
    this.commentRepository = commentRepository;
    this.taskService = taskService;
    this.userService = userService;
  }

  public List<TaskComment> getComments(UUID taskId) {
    return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
  }

  public TaskComment addComment(UUID taskId, UUID authorId, TaskCommentRequest request) {
    Task task = taskService.getTask(taskId);
    User author = userService.getById(authorId);

    TaskComment comment = new TaskComment();
    comment.setTask(task);
    comment.setAuthor(author);
    comment.setContent(request.getContent());
    return commentRepository.save(comment);
  }
}
