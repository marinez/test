package fr.insy2s.sesame.service;

import fr.insy2s.sesame.dto.request.AuthenticationRequest;
import fr.insy2s.sesame.dto.request.FirstAuthRequest;
import fr.insy2s.sesame.dto.request.RegisterRequest;
import fr.insy2s.sesame.dto.response.AuthenticationResponse;
import fr.insy2s.sesame.error.exception.AccountLockedException;
import fr.insy2s.sesame.error.exception.BadCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 *
 * IAuthenticationService is the interface that provides authentication-related operations.
 * @author Fethi Benseddik
 */
public interface IAuthenticationService {
    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details
     * @author Fethi Benseddik
     */
    void register(RegisterRequest request);

    /**
     * Authenticate the user based on email and password. If authentication is successful,
     * resets the user's failed login attempts and returns an authentication response containing
     * JWT tokens. If authentication fails, increments the user's failed login attempts
     * and locks the account after 3 failed attempts.
     *
     * @param request The authentication request containing email and password.
     * @return The authentication response containing JWT and user details.
     * @throws AccountLockedException  if the user account is locked.
     * @throws BadCredentialsException if the user provided incorrect credentials.
     * @author Fethi Benseddik
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Refreshes the user's authentication token.
     *
     * @param request  The HTTP request containing user information
     * @param response The HTTP response to update with the new tokens
     * @throws IOException if an I/O error occurs while writing the response
     * @author Fethi Benseddik
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * First connection of the user.
     * Replace password in database where the token is the same as the token in the request.
     * @param request the request containing the token and the new password.
     * @return the optional authentication response containing access and refresh tokens, or empty if the token is not valid.
     */
    Optional<AuthenticationResponse> firstConnection(FirstAuthRequest request);

    /**
     * Get user email by activation key.
     * @param activationKey: the activation key of the user.
     * @return the optional email of the user
     */
    Optional<String> getEmailByActivationKey(String activationKey);
}
