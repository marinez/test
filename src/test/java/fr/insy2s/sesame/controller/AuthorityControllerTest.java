package fr.insy2s.sesame.controller;


import fr.insy2s.sesame.service.IAuthorityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AuthorityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    IAuthorityService authorityService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAutoritiesWithoutAdmin() throws Exception {
        List<String> expectedResponse = List.of("ROLE_COMMERCIAL", "ROLE_MANAGER", "ROLE_SUPER_MANAGER");

        when(authorityService.getAutoritiesWithoutAdmin()).thenReturn(expectedResponse);
        mockMvc.perform(get("/api/v1/authority/with-not-admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*]").value(hasItem("ROLE_COMMERCIAL")))
                .andExpect(jsonPath("$[*]").value(hasItem("ROLE_MANAGER")))
                .andExpect(jsonPath("$[*]").value(hasItem("ROLE_SUPER_MANAGER")));

    }

    @Test
    void getAutoritiesWithoutAdminNoConnectedForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/authority/with-not-admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAutoritiesWithoutAdminForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/authority/with-not-admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void getAutoritiesWithoutAdminRoleManagerForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/authority/with-not-admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "COMMERCIAL")
    void getAutoritiesWithoutAdminRoleCommercialForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/authority/with-not-admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_MANAGER")
    void getAutoritiesWithoutAdminRoleSuperManagerForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/authority/with-not-admin"))
                .andExpect(status().isForbidden());
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
}