package fr.insy2s.sesame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insy2s.sesame.config.Constants;
import fr.insy2s.sesame.domain.enumeration.TypeContract;
import fr.insy2s.sesame.dto.request.UpdatePwdRequest;
import fr.insy2s.sesame.dto.response.TypeContractResponse;
import fr.insy2s.sesame.service.IMailService;
import fr.insy2s.sesame.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private IMailService mailService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String correctPassword = "P@ssword1";

    @BeforeEach
    void setUp() {
        List<TypeContractResponse> typeContractResponses = Arrays.asList(
                new TypeContractResponse(TypeContract.CDI),
                new TypeContractResponse(TypeContract.CDD)
        );
        when(userService.getAllTypeContract()).thenReturn(typeContractResponses);
        doNothing().when(userService).updatePasswordOfUser("", null);
        doNothing().when(mailService).sendNotificationPasswordUpdated("");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTypeContract_withAdminRole_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/users/type-contract")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllTypeContract_withUserRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users/type-contract")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_correct() throws Exception {
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            correctPassword,
            correctPassword
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_oldPwdNull() throws Exception {
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            null,
            correctPassword
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_newPwdTooShort() throws Exception {
        String incorrectPwdTooShort = "P@ssw1";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            correctPassword,
            incorrectPwdTooShort
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_newPwdNoDigit() throws Exception {
        String incorrectPwdNoDigit = "P@ssword";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            correctPassword,
            incorrectPwdNoDigit
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_newPwdNoUpperCase() throws Exception {
        String incorrectPwdNoUpperCase = "p@ssword1";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            correctPassword,
            incorrectPwdNoUpperCase
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_newPwdNoLowerCase() throws Exception {
        String incorrectPwdNoLowerCase = "P@SSWORD1";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            correctPassword,
            incorrectPwdNoLowerCase
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@local.host")
    void updatePasswordCurrentUser_newPwdNoSpecialChar() throws Exception {
        String incorrectPwdNoSpecialChar = "Password1";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(
            correctPassword,
            incorrectPwdNoSpecialChar
        );
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllUsersByQuery_roleAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(20))
                .andExpect(jsonPath("$.totalElements").value(103));
    }

    @Test
    @WithMockUser(username = "supermanager1@local.host", authorities = {Constants.ROLE_SUPER_MANAGER})
    void getAllUsersByQuery_roleSuperManager() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(20))
                .andExpect(jsonPath("$.totalElements").value(86));
    }

    @Test
    @WithMockUser(username = "manager1@local.host", authorities = {Constants.ROLE_MANAGER})
    void getAllUsersByQuery_roleManager() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(20))
                .andExpect(jsonPath("$.totalElements").value(46));
    }


    @Test
    @WithMockUser(username = "commercial10@local.host", authorities = {Constants.ROLE_COMMERCIAL})
    void getAllUsersByQuery_roleCommercial() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}