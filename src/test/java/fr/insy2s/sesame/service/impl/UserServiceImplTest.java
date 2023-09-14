package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.domain.Authority;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.dto.mapper.UserMapper;
import fr.insy2s.sesame.dto.request.UpdatePwdRequest;
import fr.insy2s.sesame.dto.request.UserPersonalUpdateRequest;
import fr.insy2s.sesame.error.exception.BadRequestException;
import fr.insy2s.sesame.error.exception.UnauthorizedException;
import fr.insy2s.sesame.error.exception.UserNotFoundException;

import fr.insy2s.sesame.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
class UserServiceImplTest {
    User adminUser;
    User superManager1;
    User superManager2;
    User manager1;
    User commercial9;

    private final String correctOldPassword = "P@ssword123";
    private final String correctNewPassword = "P@ssword1234";

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authority admin = Authority.builder()
                .name("ROLE_ADMIN")
                .build();

        Authority superManager = Authority.builder()
                .name("ROLE_SUPER_MANAGER")
                .build();

        Authority manager = Authority.builder()
                .name("ROLE_MANAGER")
                .build();

        Authority commercial = Authority.builder()
                .name("ROLE_COMMERCIAL")
                .build();

        adminUser = User.builder()
                .id(1)
                .uuid(UUID.fromString("b42f7d2f-2ccd-4929-8ea6-1e4de5ff5a37"))
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@local.host")
                .authority(admin)
                .build();

        superManager1 = User.builder()
                .id(2)
                .uuid(UUID.fromString("32b45dbc-3703-43d1-b4c7-59f6b6924d47"))
                .firstName("Daniele")
                .lastName("Maccrie")
                .email("supermanager1@local.host")
                .authority(superManager)
                .build();

        superManager2 = User.builder()
                .id(3)
                .uuid(UUID.fromString("53db2348-3c94-4e07-b0ee-b9238215824b"))
                .firstName("Auberon")
                .lastName("Teaze")
                .username("supermanager2@local.host")
                .email("supermanager2@local.host")
                .authority(superManager)
                .build();

        manager1 = User.builder()
                .id(4)
                .uuid(UUID.fromString("d3e6aba7-4b01-4cc8-958e-e7eaa50b2a75"))
                .firstName("Aubine")
                .lastName("Convery")
                .email("manager1@local.host")
                .authority(manager)
                .build();

        commercial9 = User.builder()
                .id(9)
                .uuid(UUID.fromString("8b3d8461-d022-4f1e-8ad4-90cd1e42d1b2"))
                .firstName("Britney")
                .lastName("Wallentin")
                .email("commercial9@local.host")
                .authority(commercial)
                .build();

        manager1.setManager(superManager1);
        commercial9.setManager(manager1);
    }

    // Tests for getUserByUuid
    @Test
    void testGetUserByUuid_AuthenticatedUserNotFound() {
        when(authentication.getName()).thenReturn(adminUser.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByUuid(String.valueOf(adminUser.getUuid()));
        });
    }

    @Test
    void testGetUserByUuid_TargetUserNotFound() {
        when(authentication.getName()).thenReturn(adminUser.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(adminUser));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByUuid("8b3d8461-d022-4f1e-8ad4-90cd1e42d1b0");
        });
    }

    @Test
    void testGetUserByUuid_Unauthorized() {
        when(authentication.getName()).thenReturn(superManager2.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(superManager2));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(manager1));
        when(userService.isUserSuperior(superManager2, manager1)).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> {
            userService.getUserByUuid(String.valueOf(manager1.getUuid()));
        });
    }

    @Test
    void testIsUserSuperior_AdminRole() {
        assertTrue(userService.isUserSuperior(adminUser, manager1));
    }

    @Test
    void testIsUserSuperior_SuperManagerRole() {
        when(userRepository.isSuperManagerOf(anyInt(), anyInt())).thenReturn(true);
        assertTrue(userService.isUserSuperior(superManager1, manager1));
    }

    @Test
    void testIsUserSuperior_ManagerRole() {
        when(userRepository.isManagerOf(any(), any())).thenReturn(true);
        assertTrue(userService.isUserSuperior(manager1, commercial9));
    }

    @Test
    void emailAlreadyExistsFalse() {
        String email = "email@email.fr";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        assertFalse(userService.emailAlreadyExists(email));
    }

    @Test
    void emailAlreadyExistsTrue() {
        String email = "email";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        assertTrue(userService.emailAlreadyExists(email));
    }

    @Test
    void updatePasswordOfUser() {
        final String username = "admin@local.host";
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(adminUser));
        when(userRepository.save(any(User.class))).thenReturn(adminUser);
        when(passwordEncoder.matches(correctOldPassword, adminUser.getPassword())).thenReturn(true);
        userService.updatePasswordOfUser(username, new UpdatePwdRequest(correctOldPassword, correctNewPassword));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updatePasswordOfUser_UserNotFound() {
        final String username = "toto";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(correctOldPassword, correctNewPassword);
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updatePasswordOfUser(username, updatePwdRequest));
    }

    @Test
    void updatePasswordOfUser_WrongOldPassword() {
        final String username = "admin@local.host";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(correctOldPassword, correctNewPassword);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.matches(correctOldPassword, adminUser.getPassword())).thenReturn(false);
        assertThrows(BadRequestException.class, () -> userService.updatePasswordOfUser(username, updatePwdRequest));
    }

    @Test
    void updatePasswordOfUser_SameOldAndNewPassword() {
        final String username = "admin@local.host";
        final UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(correctOldPassword, correctOldPassword);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.matches(correctOldPassword, adminUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(correctOldPassword, correctOldPassword)).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.updatePasswordOfUser(username, updatePwdRequest));
    }

    @Test
    @WithMockUser(username = "admin@local.host", roles = {"ADMIN"})
     void testUpdateUser_UserExists_Success() {
        String uuid = UUID.randomUUID().toString();
        User existingUser = new User();
        User updatedUser = new User();
        UserPersonalUpdateRequest request = new UserPersonalUpdateRequest("firstName", "lastName", "0123456789", LocalDate.now().minusYears(18));
        when(userRepository.findByUuid(UUID.fromString(uuid))).thenReturn(Optional.of(existingUser));
        when(userMapper.partialUpdate(request, existingUser)).thenReturn(updatedUser);
        userService.updateUser(uuid, request);
        verify(userRepository).save(updatedUser);
    }

    @Test
     void testUpdateUser_UserNotFound_ExceptionThrown() {
        String uuid = UUID.randomUUID().toString();
        UserPersonalUpdateRequest request = new UserPersonalUpdateRequest("firstName", "lastName", "email", LocalDate.now().minusYears(18));
        when(userRepository.findByUuid(UUID.fromString(uuid))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(uuid, request));
    }

    @Test
     void testUpdateUser_ErrorInPartialUpdate() {
        String uuid = UUID.randomUUID().toString();
        User existingUser = new User();
        UserPersonalUpdateRequest request = new UserPersonalUpdateRequest("firstName", "lastName", "email", LocalDate.now().minusYears(18));

        when(userRepository.findByUuid(UUID.fromString(uuid))).thenReturn(Optional.of(existingUser));
        when(userMapper.partialUpdate(request, existingUser)).thenThrow(new RuntimeException("Some error"));

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(uuid, request);
        });
    }
}