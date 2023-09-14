package fr.insy2s.sesame.security;

import fr.insy2s.sesame.error.exception.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {
    private SecurityUtils() {}


    public static String getCurrentUserAuthority() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractAuthority(securityContext.getAuthentication()))
                .orElseThrow(() -> new UsernameNotFoundException("User not authenticated"));
    }


    /**
     * Get the username of the current user.
     * @return the optional username of the current user
     * @author Fethi Benseddik
     */
    public static Optional<String> getCurrentUserSubOptional() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    /**
     * Get the username of the current user or throw exception {@link UsernameNotFoundException}.
     * @return the username of the current user
     * @author Fethi Benseddik
     */
    public static String getCurrentUserSub() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Extract the principal's username from the given authentication object.
     * @return the current user
     * @author Fethi Benseddik
     */
    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails user) {
            return user.getUsername();
        } else if (authentication.getPrincipal() instanceof String username) {
            return username;
        }
        return null;
    }

    private static String extractAuthority(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails user) {
            return user.getAuthorities().stream()
                    .filter(a -> a instanceof SimpleGrantedAuthority)
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        } else if (authentication.getPrincipal() instanceof String username) {
            return username;
        }
        return null;
    }
}
