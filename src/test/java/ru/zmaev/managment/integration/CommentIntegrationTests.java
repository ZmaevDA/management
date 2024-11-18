package ru.zmaev.managment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zmaev.managment.config.PostgresTestContainer;
import ru.zmaev.managment.model.dto.request.CommentRequest;
import ru.zmaev.managment.service.impl.CommentServiceImpl;
import ru.zmaev.managment.service.impl.TaskServiceImpl;
import ru.zmaev.managment.service.impl.UserServiceImpl;
import ru.zmaev.managment.util.IntegrationTestAuthUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentIntegrationTests extends PostgresTestContainer {

    private static final String COMMENT_API_PATH = "/v1/comments";

    private final String userId = "bf8170df-cdcf-4c7a-8ef9-f1253b229576";
    private final String commentId = "51ed59c8-01ce-41f9-ae5f-277e30d0cc1f";
    private final String unrepresentedCommentId = "52ed59c8-01ce-41f9-ae5f-277e30d0cc1f";
    private final String taskId = "3ca04ac0-b578-4266-aa05-6de6ad1db749";
    private final String unrepresentedTaskId = "3ca04ac0-b578-4266-aa05-6de6ad1db729";
    private final String authorId = "f0346d54-e695-453a-a042-3751bc693c48";
    private final String unrepresentedAuthorId = "f0346d54-e695-453a-a042-3751bc692c48";
    private final CommentRequest commentRequest = new CommentRequest("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        SecurityContextHolder.getContext()
                .setAuthentication(IntegrationTestAuthUtils
                        .createAdminUserJwtAuthenticationToken(authorId));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void loadAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_API_PATH + "/tasks/" + taskId)
                        .param("authorId", authorId)
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.task.id").value(taskId))
                .andExpect(jsonPath("$.comments.content[0].content").value("new comment1"));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void loadAll_taskNotFound() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_API_PATH + "/tasks/" + unrepresentedTaskId)
                        .param("authorId", authorId)
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(TaskServiceImpl.TASK_NOT_FOUND))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void loadAll_authorNotFound() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_API_PATH + "/tasks/" + taskId)
                        .param("authorId", unrepresentedAuthorId)
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(UserServiceImpl.USER_NOT_FOUND))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_API_PATH + "/tasks/" + taskId)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMENT_API_PATH + "/" + commentId)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("test"));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void update_forbidden() throws Exception {
        SecurityContextHolder.getContext()
                .setAuthentication(IntegrationTestAuthUtils
                        .createBaseUserJwtAuthenticationToken(userId));
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMENT_API_PATH + "/" + commentId)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void update_commentNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMENT_API_PATH + "/" + unrepresentedCommentId)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(CommentServiceImpl.COMMENT_NOT_FOUND))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(COMMENT_API_PATH + "/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql",
            "/sql/insert_comments.sql"
    })
    public void deleteById_commentNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(COMMENT_API_PATH + "/" + unrepresentedCommentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(CommentServiceImpl.COMMENT_NOT_FOUND))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
