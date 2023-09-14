package fr.insy2s.sesame.security;


import fr.insy2s.sesame.config.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Implementation of AuditorAware based on Spring Security.
 *
 * @author Fethi Benseddik
 */
@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    /**
     * Method called by Spring Security to automatically get the current auditor (username).
     * Used by Spring Data JPA to automatically set the current auditor (username) when saving a new object.
     *
     * @return the current auditor (username)
     * @author Fethi Benseddik
     * @see AuditorAware
     * @see org.springframework.data.jpa.repository.config.EnableJpaAuditing
     */
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        Optional<String> user = SecurityUtils.getCurrentUserSubOptional();
        if (user.isPresent() && (user.get().equals("anonymousUser"))) {
            return Optional.of(Constants.ANONYMOUS);

        }
        return user;
    }

}
