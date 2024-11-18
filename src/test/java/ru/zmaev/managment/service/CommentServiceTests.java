package ru.zmaev.managment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.zmaev.managment.auth.UserInfo;
import ru.zmaev.managment.exception.ForbiddenException;
import ru.zmaev.managment.exception.NotFoundException;
import ru.zmaev.managment.mapper.CommentMapper;
import ru.zmaev.managment.mapper.TaskMapper;
import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.model.dto.response.CommentResponse;
import ru.zmaev.managment.model.dto.response.TaskResponse;
import ru.zmaev.managment.model.dto.response.TaskWithCommentsResponse;
import ru.zmaev.managment.model.entity.Comment;
import ru.zmaev.managment.model.entity.Task;
import ru.zmaev.managment.model.entity.User;
import ru.zmaev.managment.repository.CommentRepository;
import ru.zmaev.managment.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CommentServiceTests {
    private final UserService mockUserService = mock(UserService.class);
    private final TaskService mockTaskService = mock(TaskService.class);
    private final TaskMapper mockTaskMapper = mock(TaskMapper.class);
    private final UserInfo mockUserInfo = mock(UserInfo.class);
    private final CommentRepository mockCommentRepository = mock(CommentRepository.class);
    private final CommentMapper mockCommentMapper = mock(CommentMapper.class);

    private final UUID taskId = UUID.randomUUID();
    private final UUID authorId = UUID.randomUUID();
    private final String authorEmail = "example@example.com";
    private final UUID commentId = UUID.randomUUID();

    private final CommentRequest commentRequest = new CommentRequest();

    private final Task task = new Task();
    private final User author = new User();
    private final Comment comment = new Comment();

    private final List<Comment> comments = List.of(comment);

    private final TaskWithCommentsResponse taskWithCommentsResponse = new TaskWithCommentsResponse();
    private final TaskResponse taskResponse = new TaskResponse();
    private final CommentResponse commentResponse = new CommentResponse();

    private final CommentService commentService = new CommentServiceImpl(
            mockUserService,
            mockTaskService,
            mockTaskMapper,
            mockUserInfo,
            mockCommentRepository,
            mockCommentMapper
    );

    @BeforeEach
    public void init() {
        task.setId(taskId);
        author.setId(authorId);
        comment.setId(commentId);

        taskResponse.setId(taskId);
        commentResponse.setId(commentId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CommentResponse> commentsResponsePage = new PageImpl<>(List.of(commentResponse), pageable, 1);

        taskWithCommentsResponse.setTask(taskResponse);
        taskWithCommentsResponse.setComments(commentsResponsePage);
    }

    @Test
    public void testLoadAllCommentsByTaskIdAndAuthorId() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        when(mockTaskService.loadTaskByIdOrThrow(taskId)).thenReturn(task);
        when(mockUserService.loadUserByIdOrThrow(authorId)).thenReturn(author);

        Page<Comment> commentsPage = new PageImpl<>(comments, pageable, 1);

        when(mockCommentRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(commentsPage);
        when(mockTaskMapper.toResponseWithComments(eq(task), eq(commentsPage), eq(mockCommentMapper))).thenReturn(taskWithCommentsResponse);

        TaskWithCommentsResponse result = commentService.loadAllCommentsByTaskIdAndAuthorId(taskId, authorId, 0, 10);

        assertEquals(taskWithCommentsResponse, result);
        verify(mockCommentRepository, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(mockTaskMapper, times(1)).toResponseWithComments(eq(task), eq(commentsPage), eq(mockCommentMapper));
    }

    @Test
    public void create() {
        when(mockUserInfo.getEmail()).thenReturn(authorEmail);
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockTaskService.loadTaskByIdOrThrow(taskId)).thenReturn(task);
        when(mockCommentMapper.toEntity(commentRequest)).thenReturn(comment);
        when(mockCommentRepository.save(comment)).thenReturn(comment);
        when(mockCommentMapper.toResponse(comment)).thenReturn(commentResponse);

        CommentResponse result = commentService.create(taskId, commentRequest);

        assertEquals(commentResponse, result);
        verify(mockCommentRepository, times(1)).save(comment);
        verify(mockCommentMapper, times(1)).toResponse(comment);
        verify(mockCommentMapper, times(1)).toEntity(commentRequest);
    }

    @Test
    public void update() {
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(mockCommentRepository.save(comment)).thenReturn(comment);
        when(mockCommentMapper.toResponse(comment)).thenReturn(commentResponse);

        CommentResponse result = commentService.update(commentId, commentRequest);

        assertEquals(commentResponse, result);
        verify(mockCommentRepository, times(1)).findById(commentId);
        verify(mockCommentRepository, times(1)).save(comment);
        verify(mockCommentMapper, times(1)).toResponse(comment);
    }

    @Test
    public void update_forbidden() {
        comment.setAuthor(new User());
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(ForbiddenException.class, () -> commentService.update(commentId, commentRequest));

        verify(mockCommentRepository, times(1)).findById(commentId);
    }

    @Test
    public void update_commentNotFound() {
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.update(commentId, commentRequest));
    }

    @Test
    public void deleteById() {
        comment.setAuthor(author);
        when(mockUserInfo.getEmail()).thenReturn(authorEmail);
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteById(commentId);

        verify(mockUserService, times(1)).loadUserByEmailOrThrow(authorEmail);
        verify(mockCommentRepository, times(1)).findById(commentId);
    }

    @Test
    public void deleteById_commentNotFound() {
        when(mockUserInfo.getEmail()).thenReturn(authorEmail);
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.deleteById(commentId));

        verify(mockUserService, times(1)).loadUserByEmailOrThrow(authorEmail);
        verify(mockCommentRepository, times(1)).findById(commentId);
    }

    @Test
    public void deleteById_forbidden() {
        comment.setAuthor(new User());
        when(mockUserInfo.getEmail()).thenReturn(authorEmail);
        when(mockUserInfo.getEmail()).thenReturn(authorEmail);
        when(mockUserService.loadUserByEmailOrThrow(authorEmail)).thenReturn(author);
        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(ForbiddenException.class, () -> commentService.deleteById(commentId));

        verify(mockUserService, times(1)).loadUserByEmailOrThrow(authorEmail);
        verify(mockCommentRepository, times(1)).findById(commentId);
    }
}
