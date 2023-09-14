package fr.insy2s.sesame.config;


import fr.insy2s.sesame.domain.Authority;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Test
    void userDetailsService() {
        Authority authority = Authority.builder()
                .name("ROLE_MANAGER")
                .build();

        User user = User.builder()
                .email("test@example.com")
                .password("testPassword")
                .username("test@example.com")
                .authority(authority)
                .build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        verify(userRepository).findByEmail("test@example.com");
        assertNotNull(userDetails);
    }

    @Test
    void authenticationProvider() {
        User user = User.builder()
                .email("testEmail@test.fr")
                .password("testPassword")
                .username("testEmail@test.fr")
                .authority(Authority.builder().name("ROLE_MANAGER").build())
                .build();
        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(userDetailsService);
        assertNotNull(passwordEncoder);
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
        assertNotNull(authenticationProvider);
    }

    @Test
    void authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
        AuthenticationManager returnedManager = applicationConfig.authenticationManager(authConfiguration);
        assertEquals(authenticationManager, returnedManager);
    }

    @Test
    void passwordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void userDetailsServiceThrowsWhenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonExistentUser");
        });
        verify(userRepository).findByEmail("nonExistentUser");
    }
}
