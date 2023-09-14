package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.repository.IAuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class AuthorityServiceImplTest {

    @Mock
    private IAuthorityRepository authorityRepository;
    @InjectMocks
    private AuthorityServiceImpl authorityService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAutoritiesWithoutAdmin() {
        List<String> authorities = List.of("ROLE_COMMERCIAL", "ROLE_MANAGER", "ROLE_SUPER_MANAGER");
        when(authorityRepository.findNameByNameNotAdmin()).thenReturn(authorities);
        List<String> expectedResponse = List.of("ROLE_COMMERCIAL", "ROLE_MANAGER", "ROLE_SUPER_MANAGER");
        List<String> result = authorityService.getAutoritiesWithoutAdmin();
        assertEquals(expectedResponse, result);
    }

    @Test
    void authorityExist() {
        String authority = "ROLE_COMMERCIAL";
        when(authorityRepository.existsByName(authority)).thenReturn(true);
        boolean result = authorityService.authorityExist(authority);
        assertEquals(true, result);
    }

    @Test
    void authorityExistFalse() {
        String authority = "ROLE_COM";
        when(authorityRepository.existsByName(authority)).thenReturn(false);
        boolean result = authorityService.authorityExist(authority);
        assertEquals(false, result);
    }
}