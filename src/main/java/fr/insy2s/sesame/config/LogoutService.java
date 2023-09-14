package fr.insy2s.sesame.config;


import fr.insy2s.sesame.repository.ITokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * LogoutService class implements the LogoutHandler interface to handle the logout process
 * by invalidating the user's JWT token and clearing the security context.
 *
 * @author Fethi Benseddik
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final ITokenRepository tokenRepository;


    /**
     * Handles the logout process for the user by invalidating the JWT token.
     *
     * @param request        The incoming HTTP request
     * @param response       The outgoing HTTP response
     * @param authentication The authentication object for the user
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByUserToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
