package ru.zmaev.managment.integration;

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
import ru.zmaev.managment.service.impl.UserServiceImpl;
import ru.zmaev.managment.util.IntegrationTestAuthUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserIntegrationTests extends PostgresTestContainer {

    private static final String USER_API_PATH = "/v1/users";

    private final String userId = "bf8170df-cdcf-4c7a-8ef9-f1253b229576";
    private final String unrepresentedUserId = "bf8170df-cdcf-4c7a-8ef9-f1253b229571";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        SecurityContextHolder.getContext()
                .setAuthentication(IntegrationTestAuthUtils
                        .createAdminUserJwtAuthenticationToken(userId));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql"
    })
    public void loadAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_PATH)
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[2].id").value(userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql"
    })
    public void loadAll_forbidden() throws Exception {
        SecurityContextHolder.getContext()
                .setAuthentication(IntegrationTestAuthUtils
                        .createBaseUserJwtAuthenticationToken(userId));

        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_PATH)
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql"
    })
    public void loadCurrentUserData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_PATH + "/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql"
    })
    public void loadById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_PATH + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql"
    })
    public void loadById_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_PATH + "/" + unrepresentedUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(UserServiceImpl.USER_NOT_FOUND))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_users.sql"
    })
    public void loadById_forbidden() throws Exception {
        SecurityContextHolder.getContext()
                .setAuthentication(IntegrationTestAuthUtils
                        .createBaseUserJwtAuthenticationToken(userId));

        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_PATH + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
