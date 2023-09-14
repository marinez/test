package fr.insy2s.sesame.controller;


import fr.insy2s.sesame.config.Constants;
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
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * AuthenticationController class handles authentication-related API endpoints.
 *
 * @author Fethi Benseddik
 * @RestController Indicates that the class defines RESTful endpoints.
 * @RequestMapping Maps the controller to the base URL "/api/v1/auth".
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    private final IAuthorityService authorityService;

    private final IUserService userService;



    /**
     * Registers a new user with organization based on the provided registration request.
     *
     * @param request The registration request containing user details
     * @return ResponseEntity
     * @author Fethi Benseddik
     */
    @PostMapping("/register")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request
    ) {
        log.debug("REST request to register user {}", request.getEmail());

        if (userService.emailAlreadyExists(request.getEmail())) {
            throw new EmailExistException("Email existe déjà");
        }

        if (Objects.nonNull(request.getAuthority()) && request.getAuthority().equals("ROLE_ADMIN")) {
            throw new AccountException("l'utilisateur créé ne peut pas être administrateur");
        }

        if (Objects.nonNull(request.getBirthday()) && request.getBirthday().isAfter(LocalDate.now().minusYears(Constants.YEAR_MINIMUM))) {
            throw new AccountException("l'utilisateur doit avoir au moins 18 ans");
        }

        if (!authorityService.authorityExist(request.getAuthority())) {
            throw new AuthorityNotFoundException("Authority not found");
        }

        authenticationService.register(request);
    log.info("REST request to register user {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request The authentication request containing user credentials
     * @return ResponseEntity containing an authentication response
     * @author Fethi Benseddik
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("REST request to authenticate user {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Refreshes the user's authentication token.
     *
     * @param request  The HTTP request containing user information
     * @param response The HTTP response to update with the new token
     * @throws IOException if an I/O error occurs while processing the request
     * @author Fethi Benseddik
     */
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    /**
     * Get user email by activation key. Used to check if the activation key is valid and return the email of the user corresponding to the key.
     * @param activationKey: the activation key of the user, send by email when the user is created.
     * @return the email of the user if the activation key is valid with status {@code 200 (OK)}.
     *        If the activation key is not valid, return {@code 401 (UNAUTHORIZED)}.
     * @author Peter Mollet
     */
    @GetMapping("/first-connection/{activationKey}")
    public ResponseEntity<String> getEmailByActivationKey(@PathVariable String activationKey){
       log.debug("REST request to get email by activation key : {}", activationKey);
        Optional<String> email = authenticationService.getEmailByActivationKey(activationKey);
        return email.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /**
     * First connection of the user.
     * If token register is in the database, the user can change his password, will be activated, token/key will be deleted, and return the auth response.
     * @param request : the request with the token register and the new password.
     * @return the auth response if the token register is in the database with status {@code 200 (OK)}.
     *       If the token register is not in the database, return {@code 400 (BAD REQUEST)}.
     *       If the password is not valid, return {@code 400 (BAD REQUEST)}.
     * @author Peter Mollet
     */
    @PostMapping("/first-connection")
    public ResponseEntity<AuthenticationResponse> firstConnection(@RequestBody @Valid FirstAuthRequest request) {
        log.debug("REST request to first connection: activation key {}", request.tokenRegister());
        Optional<AuthenticationResponse> response = authenticationService.firstConnection(request);
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
