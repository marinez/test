package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.config.Constants;
import fr.insy2s.sesame.dto.request.UserCriteria;
import fr.insy2s.sesame.dto.response.UserQueryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserQueryServiceImplTest {

    @Autowired
    private UserQueryServiceImpl userQueryService;

    private static final Pageable pageable = Pageable.ofSize(10);


    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_noFilter() {
        UserCriteria criteria = new UserCriteria(null, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(103, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterAuthoritySuperManager() {
        UserCriteria criteria = new UserCriteria("ROLE_SUPER_MANAGER", null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(3, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterAuthorityManager() {
        UserCriteria criteria = new UserCriteria("ROLE_MANAGER", null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(6, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchFirstName() {
        UserCriteria criteria = new UserCriteria(null, "Auberon");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchLastName() {
        UserCriteria criteria = new UserCriteria(null, "Sarle");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchLastNameAndFirstName() {
        UserCriteria criteria = new UserCriteria(null, "Marabel Sarle");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchFirstNameAndLastName() {
        UserCriteria criteria = new UserCriteria(null, "Sarle Marabel");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchActivityZone() {
        UserCriteria criteria = new UserCriteria(null, "Sud");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(19, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchActivityService() {
        UserCriteria criteria = new UserCriteria(null, "Service A");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(26, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "admin@local.host", authorities = {Constants.ROLE_ADMIN})
    void getAllByQuery_admin_filterSearchBusinessName() {
        UserCriteria criteria = new UserCriteria(null, "Livetube");
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(11, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "supermanager1@local.host", authorities = {Constants.ROLE_SUPER_MANAGER})
    void getAllByQuery_superManager() {
        UserCriteria criteria = new UserCriteria(null, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(86, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "supermanager1@local.host", authorities = {Constants.ROLE_SUPER_MANAGER})
    void getAllByQuery_superManager_filterManager() {
        UserCriteria criteria = new UserCriteria(Constants.ROLE_MANAGER, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(3, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "supermanager1@local.host", authorities = {Constants.ROLE_SUPER_MANAGER})
    void getAllByQuery_superManager_filterCommercial() {
        UserCriteria criteria = new UserCriteria(Constants.ROLE_COMMERCIAL, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(83, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "supermanager1@local.host", authorities = {Constants.ROLE_SUPER_MANAGER})
    void getAllByQuery_superManager_filterSuperManager() {
        UserCriteria criteria = new UserCriteria(Constants.ROLE_SUPER_MANAGER, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(86, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "manager1@local.host", authorities = {Constants.ROLE_MANAGER})
    void getAllByQuery_manager() {
        UserCriteria criteria = new UserCriteria(null, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(46, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "manager1@local.host", authorities = {Constants.ROLE_MANAGER})
    void getAllByQuery_manager_filterAuthorityCommercial() {
        UserCriteria criteria = new UserCriteria(Constants.ROLE_COMMERCIAL, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(46, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "manager1@local.host", authorities = {Constants.ROLE_MANAGER})
    void getAllByQuery_manager_filterAuthorityManager() {
        UserCriteria criteria = new UserCriteria(Constants.ROLE_MANAGER, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(46, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = "manager1@local.host", authorities = {Constants.ROLE_MANAGER})
    void getAllByQuery_manager_filterAuthoritySuperManager() {
        UserCriteria criteria = new UserCriteria(Constants.ROLE_SUPER_MANAGER, null);
        Page<UserQueryResponse> page = userQueryService.getAllByQuery(pageable, criteria);
        assertEquals(46, page.getTotalElements());
    }

}
