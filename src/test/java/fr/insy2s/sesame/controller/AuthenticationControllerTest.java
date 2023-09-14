package fr.insy2s.sesame.controller;

import fr.insy2s.sesame.dto.request.AuthenticationRequest;
import fr.insy2s.sesame.dto.request.FirstAuthRequest;
import fr.insy2s.sesame.dto.request.RegisterRequest;
import fr.insy2s.sesame.dto.response.AuthenticationResponse;
import fr.insy2s.sesame.error.exception.AuthorityNotFoundException;
import fr.insy2s.sesame.error.exception.AccountException;
import fr.insy2s.sesame.error.exception.EmailExistException;
import fr.insy2s.sesame.service.IAuthenticationService;
import fr.insy2s.sesame.service.IAuthorityService;
import fr.insy2s.sesame.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private IAuthenticationService authenticationService;
    @Mock
    IAuthorityService authorityService;
    @Mock
    IUserService userService;
    @InjectMocks
    private AuthenticationController authenticationController;


    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setBirthday(LocalDate.now().minusYears(18));
        registerRequest.setEmail("email@email.fr");
        registerRequest.setAuthority("ROLE_COMMERCIAL");

        when(userService.emailAlreadyExists(registerRequest.getEmail())).thenReturn(false);
        when(authorityService.authorityExist(registerRequest.getAuthority())).thenReturn(true);

        ResponseEntity<Void> responseEntity = authenticationController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.authenticate(authenticationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testRefreshToken() throws IOException {
        doNothing().when(authenticationService).refreshToken(request, response);

        authenticationController.refreshToken(request, response);

        verify(authenticationService).refreshToken(request, response);
    }

    @Test
    void testFirstConnection() {
        FirstAuthRequest firstAuthRequest = new FirstAuthRequest("activation-key", "P@ssword123");
        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        when(authenticationService.firstConnection(firstAuthRequest)).thenReturn(Optional.of(expectedResponse));

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.firstConnection(firstAuthRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testFirstConnection_IncorrectActivationKey() {
        FirstAuthRequest firstAuthRequest = new FirstAuthRequest("incorrect-key", "P@ssword123");
        when(authenticationService.firstConnection(firstAuthRequest)).thenReturn(Optional.empty());

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.firstConnection(firstAuthRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetEmailByActivationKey() {
        String activationKey = "activationKey";
        String expectedResponse = "expectedResponse";
        when(authenticationService.getEmailByActivationKey(activationKey)).thenReturn(java.util.Optional.of(expectedResponse));

        ResponseEntity<String> responseEntity = authenticationController.getEmailByActivationKey(activationKey);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetEmailByActivationKey_Incorrect() {
        String activationKey = "activationKey";
        when(authenticationService.getEmailByActivationKey(activationKey)).thenReturn(java.util.Optional.empty());

        ResponseEntity<String> responseEntity = authenticationController.getEmailByActivationKey(activationKey);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("email@email.fr");
        when(userService.emailAlreadyExists(registerRequest.getEmail())).thenReturn(true);
        assertThrows(EmailExistException.class, () -> authenticationController.register(registerRequest));
    }

    @Test
    void testRegisterAuthorityNotFound() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("email@email.fr");
        registerRequest.setAuthority("ROLE_COMMERCIAL");
        registerRequest.setBirthday(LocalDate.now().minusYears(18));
        when(userService.emailAlreadyExists(registerRequest.getEmail())).thenReturn(false);
        when(authorityService.authorityExist(registerRequest.getAuthority())).thenReturn(false);
        assertThrows(AuthorityNotFoundException.class, () -> authenticationController.register(registerRequest));
    }

    @Test
    void testRegisterUserTooYoung() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setBirthday(LocalDate.now().minusYears(17));
        registerRequest.setEmail("email@email.fr");
        registerRequest.setAuthority("ROLE_COMMERCIAL");
        when(userService.emailAlreadyExists(registerRequest.getEmail())).thenReturn(false);
        when(authorityService.authorityExist(registerRequest.getAuthority())).thenReturn(true);
        assertThrows(AccountException.class, () -> authenticationController.register(registerRequest));
    }

    @Test
    void testRegisterUserNoAdmin() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setBirthday(LocalDate.now().minusYears(18));
        registerRequest.setEmail("email@email.fr");
        registerRequest.setAuthority("ROLE_ADMIN");
        when(userService.emailAlreadyExists(registerRequest.getEmail())).thenReturn(false);
        when(authorityService.authorityExist(registerRequest.getAuthority())).thenReturn(true);
        assertThrows(AccountException.class, () -> authenticationController.register(registerRequest));

    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

}
