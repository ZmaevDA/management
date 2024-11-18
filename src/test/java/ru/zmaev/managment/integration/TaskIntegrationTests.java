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
import ru.zmaev.managment.model.dto.request.TaskCreateRequest;
import ru.zmaev.managment.model.dto.request.TaskUpdateRequest;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.enums.PriorityType;
import ru.zmaev.managment.model.enums.StatusType;
import ru.zmaev.managment.service.impl.TaskServiceImpl;
import ru.zmaev.managment.util.IntegrationTestAuthUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskIntegrationTests extends PostgresTestContainer {

    private static final String TASK_API_PATH = "/v1/tasks";
    private static final String taskIdAssigner = "3ca04ac0-b578-4266-aa05-6de6ad1db749";
    private static final String taskIdAuthor = "20c8158b-6c21-4532-be49-7b912599b4c9";
    private static final String unrepresentedTaskId = "3ca04ac0-b578-4266-aa05-6de6ad1db712";
    private static final String unassignedTaskId = "eee1ffa6-d7e5-417a-8a00-431dd16aba87";

    private final String userId = "bf8170df-cdcf-4c7a-8ef9-f1253b229576";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        SecurityContextHolder.getContext()
                .setAuthentication(IntegrationTestAuthUtils
                        .createBaseUserJwtAuthenticationToken(userId));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void loadAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        TaskFilterRequest taskFilterRequest = new TaskFilterRequest();
        taskFilterRequest.setId(taskIdAssigner);

        mockMvc.perform(MockMvcRequestBuilders.get(TASK_API_PATH)
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .content(objectMapper.writeValueAsString(taskFilterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(taskIdAssigner))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void loadById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TASK_API_PATH + "/" + taskIdAssigner)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskIdAssigner))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void loadById_taskNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TASK_API_PATH + "/" + unrepresentedTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(TaskServiceImpl.TASK_NOT_FOUND))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void create() throws Exception {
        TaskCreateRequest taskCreateRequest =
                new TaskCreateRequest("title", "description", PriorityType.HIGH);
        mockMvc.perform(MockMvcRequestBuilders.post(TASK_API_PATH)
                        .content(objectMapper.writeValueAsString(taskCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void changeStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(TASK_API_PATH + "/" + taskIdAssigner + "/status")
                        .param("statusType", StatusType.DONE.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void changePriority() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(TASK_API_PATH + "/" + taskIdAssigner + "/priority")
                        .param("priorityType", PriorityType.LOW.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void update() throws Exception {
        String updatedTitle = "updated title";
        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest(
                updatedTitle,
                "description",
                StatusType.CREATED,
                PriorityType.HIGH
        );
        mockMvc.perform(MockMvcRequestBuilders.put(TASK_API_PATH + "/" + taskIdAuthor)
                        .content(objectMapper.writeValueAsString(taskUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskIdAuthor))
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(TASK_API_PATH + "/" + taskIdAuthor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void assign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(TASK_API_PATH + "/" + unassignedTaskId + "/assignees/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignee.id").value(userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql",
            "/sql/insert_tasks.sql"
    })
    public void unassign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(TASK_API_PATH + "/" + taskIdAssigner + "/unassigned")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignee").doesNotExist())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
