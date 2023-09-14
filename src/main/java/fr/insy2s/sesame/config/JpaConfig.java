package fr.insy2s.sesame.config;

import fr.insy2s.sesame.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JpaConfig class provides configuration for Spring Data JPA auditing.
 * It defines a bean for the auditor aware implementation to track user responsible for data changes.
 *
 * @author Fethi Benseddik
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

    /**
     * Creates and returns an AuditorAware implementation using SpringSecurityAuditorAware.
     * It is used to track the user responsible for data changes during auditing.
     *
     * @return The AuditorAware implementation
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware();
    }
}