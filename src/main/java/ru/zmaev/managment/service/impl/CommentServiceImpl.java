package ru.zmaev.managment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.ForbiddenException;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.CommentMapper;
import ru.zmaev.managment.mapper.TaskMapper;
import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.dto.response.TaskWithCommentsResponse;
import ru.zmaev.managment.model.entity.Comment;
import ru.zmaev.managment.model.entity.Task;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.model.enums.RoleType;
import ru.zmaev.managment.repository.CommentRepository;
import ru.zmaev.managment.repository.specification.CommentSpecification;
import ru.zmaev.managment.service.CommentService;
import ru.zmaev.managment.service.TaskService;
import ru.zmaev.managment.service.UserService;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    public static final String COMMENT_NOT_FOUND = "COMMENT_NOT_FOUND";
    public static final String COMMENT_NO_ACCESS = "COMMENT_NO_ACCESS";

    private final UserService userService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserInfo userInfo;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public TaskWithCommentsResponse loadAllCommentsByTaskIdAndAuthorId(UUID taskId, UUID authorId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Task task = taskService.loadTaskByIdOrThrow(taskId);
        User author = null;
        if (authorId != null) {
            author = userService.loadUserByIdOrThrow(authorId);
        }
        Specification<Comment> spec = CommentSpecification.hasTaskAndAuthor(task, author);

        Page<Comment> commentsPage = commentRepository.findAll(spec, pageable);
        return taskMapper.toResponseWithComments(task, commentsPage, commentMapper);
    }

    @Override
    @Transactional
    public CommentResponse create(UUID taskId, CommentRequest request) {
        User author = userService.loadUserByEmailOrThrow(userInfo.getEmail());
        Task task = taskService.loadTaskByIdOrThrow(taskId);
        Comment comment = commentMapper.toEntity(request);
        comment.setCreatedAt(Instant.now());
        comment.setAuthor(author);
        comment.setTask(task);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Override
    @Transactional
    public CommentResponse update(UUID commentId, CommentRequest request) {
        User user = userService.loadUserByEmailOrThrow(userInfo.getEmail());
        Comment comment = loadCommentByIdOrThrow(commentId);
        checkAuthorAdminAccessOrThrow(user, comment);
        comment.setContent(request.getContent());
        comment.setUpdatedAt(Instant.now());
        Comment commentToSave = commentRepository.save(comment);
        return commentMapper.toResponse(commentToSave);
    }

    @Override
    @Transactional
    public void deleteById(UUID commentId) {
        User user = userService.loadUserByEmailOrThrow(userInfo.getEmail());
        Comment comment = loadCommentByIdOrThrow(commentId);
        checkAuthorAdminAccessOrThrow(user, comment);
        commentRepository.delete(comment);
    }

    private void checkAuthorAdminAccessOrThrow(User user, Comment comment) {
        if (!Objects.equals(user, comment.getAuthor()) &&
                !userInfo.getRole().contains(RoleType.ROLE_ADMIN.name())) {
            throw new ForbiddenException(COMMENT_NO_ACCESS, "You are not the author or do not have admin privileges.");
        }
    }

    private Comment loadCommentByIdOrThrow(UUID id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(COMMENT_NOT_FOUND, "Comment not found")
        );
    }
}
