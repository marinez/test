package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.config.JwtService;
import fr.insy2s.sesame.domain.Organization;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.dto.request.*;
import fr.insy2s.sesame.dto.mapper.IUserRegisterMapper;
import fr.insy2s.sesame.dto.response.AuthenticationResponse;
import fr.insy2s.sesame.repository.IUserRepository;
import fr.insy2s.sesame.service.IMailService;
import fr.insy2s.sesame.service.IOrganizationService;
import fr.insy2s.sesame.service.ITokenService;
import fr.insy2s.sesame.utils.service.IRandomService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceImplTest {

    private final String jwtToken = "jwtToken";
    private final String refreshToken = "refreshToken";
    @Mock
    private IUserRepository userRepository;

    @Mock
    private IMailService mailService;
    @Mock
    private ITokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private AuthenticationResponse authResponse;

    @Mock
    private IOrganizationService organizationService;
    @Mock
    private IRandomService randomService;

    @Mock
    private IUserRegisterMapper userMapper;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .email("email@email.fr")
                .isAccountNonLocked(true)
                .organization(new Organization())
                .build();
        authResponse = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Test
    void register() {
       AddressRegisterRequest  address = AddressRegisterRequest.builder()
                .street("street")
                .city("city")
                .zipCode("12345")
                .build();
        OrganizationRegisterRequest organization = OrganizationRegisterRequest.builder()
                .businessName("businessName")
                .rib("012345678901234567891234568")
                .siren("123456789")
                .activity("activity")
                .capital(1000L)
                .directorName("directorName")
                .address(address)
                .businessCreationDate(LocalDate.now().minusYears(1))
                .build();
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Azeé èàçüö-ûîôâê'ëï")
                .lastName("lastName")
                .birthday(LocalDate.now().minusYears(20))
                .email("email@email.com").phone("01 02 03 04 05")
                .authority("ROLE_USER")
                .organization(organization)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toEntity(any(RegisterRequest.class))).thenReturn(user);
        when(userRepository.existsByActivationKey("activationKeyexist")).thenReturn(true);
        when(userRepository.existsByActivationKey("activationKey")).thenReturn(false);
        when(randomService.generateRandomUUIDString(anyInt())).thenReturn("activationKeyexist", "activationKey");
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(organizationService.createOrGetOrganization(any(OrganizationRegisterRequest.class))).thenReturn(1 );
        authenticationService.register(registerRequest);
        assertEquals("password", user.getPassword());
        assertEquals("activationKey", user.getActivationKey());
        assertEquals(1, user.getOrganization().getId());

    }

    @Test
    void authenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("johndoe@example.com", "password");
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        AuthenticationResponse result = authenticationService.authenticate(authenticationRequest);

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        verify(tokenService, times(1)).revokeAllUserTokens(user);
        verify(tokenService, times(1)).saveUserToken(user, jwtToken);
        assertEquals(authResponse, result);
    }

    @Test
    void refreshToken() throws IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken);

        HttpServletResponse mockResponse = new MockHttpServletResponse();

        when(jwtService.extractUsername(refreshToken)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        authenticationService.refreshToken(mockRequest, mockResponse);

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(jwtService, times(1)).isTokenValid(refreshToken, user);
        verify(jwtService, times(1)).generateToken(user);
        verify(tokenService, times(1)).revokeAllUserTokens(user);
        verify(tokenService, times(1)).saveUserToken(user, jwtToken);
    }

    @Test
    void firstConnection() {
        FirstAuthRequest request = new FirstAuthRequest("activationKey", "P@ssword123");
        when(userRepository.findByActivationKey(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        Optional<AuthenticationResponse> opAuthResponse = authenticationService.firstConnection(request);

        assertEquals(Optional.of(authResponse), opAuthResponse);
    }

    @Test
    void firstConnection_incorrectKey() {
        FirstAuthRequest request = new FirstAuthRequest("activationKey", "P@ssword123");
        when(userRepository.findByActivationKey(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        Optional<AuthenticationResponse> opAuthResponse = authenticationService.firstConnection(request);

        assertEquals(Optional.empty(), opAuthResponse);
    }

    @Test
    void getEmailByActivationKey() {
        when(userRepository.findEmailByActivationKey(anyString())).thenReturn(Optional.of(user.getEmail()));
        Optional<String> opEmail = authenticationService.getEmailByActivationKey("activationKey");
        assertEquals(Optional.of(user.getEmail()), opEmail);
    }

    @Test
    void getEmailByActivationKey_incorrect() {
        when(userRepository.findEmailByActivationKey(anyString())).thenReturn(Optional.empty());
        Optional<String> opEmail = authenticationService.getEmailByActivationKey("activationKey");
        assertEquals(Optional.empty(), opEmail);
    }


}
