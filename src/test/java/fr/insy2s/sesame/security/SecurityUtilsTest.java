package fr.insy2s.sesame.security;


import fr.insy2s.sesame.domain.Authority;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.error.exception.UsernameNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilsTest {

    @Test
    void testGetCurrentUserSubOptional_WithUserDetails_ReturnsUsername() {
        String username = "johndoe@test.com";
        UserDetails userDetails = User.builder()
                .username(username)
                .email(username)
                .password("password")
                .authority(Authority.builder().name("ROLE_MANAGER").build())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Optional<String> currentUserSubOptional = SecurityUtils.getCurrentUserSubOptional();

        assertTrue(currentUserSubOptional.isPresent());
        assertEquals(username, currentUserSubOptional.get());
    }

    @Test
    void testGetCurrentUserSubOptional_WithStringPrincipal_ReturnsUsername() {
        String username = "john.doe";

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Optional<String> currentUserSubOptional = SecurityUtils.getCurrentUserSubOptional();

        assertTrue(currentUserSubOptional.isPresent());
        assertEquals(username, currentUserSubOptional.get());
    }

    @Test
    void testGetCurrentUserSubOptional_WithoutAuthentication_ReturnsEmptyOptional() {
        SecurityContextHolder.clearContext();

        Optional<String> currentUserSubOptional = SecurityUtils.getCurrentUserSubOptional();

        assertFalse(currentUserSubOptional.isPresent());
    }

    @Test
    void testGetCurrentUserSub_WithUserDetails_ReturnsUsername() {
        String username = "johndoe@test.fr";
        UserDetails userDetails = User.builder()
                .username(username)
                .email(username)
                .password("password")
                .authority(Authority.builder().name("ROLE_MANAGER").build())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        String currentUserSub = SecurityUtils.getCurrentUserSub();

        assertEquals(username, currentUserSub);
    }

    @Test
    void testGetCurrentUserSub_WithStringPrincipal_ReturnsUsername() {
        String username = "john.doe";

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        String currentUserSub = SecurityUtils.getCurrentUserSub();

        assertEquals(username, currentUserSub);
    }

    @Test
    void testGetCurrentUserSub_WithoutAuthentication_ThrowsUsernameNotFoundException() {
        SecurityContextHolder.clearContext();

        assertThrows(UsernameNotFoundException.class, SecurityUtils::getCurrentUserSub);
    }

}