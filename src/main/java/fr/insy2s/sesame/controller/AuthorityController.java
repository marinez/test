package fr.insy2s.sesame.controller;

import fr.insy2s.sesame.service.IAuthorityService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AuthorityController class handles authority-related API endpoints.
 *
 * @author Marine Zimmer
 * @RestController Indicates that the class defines RESTful endpoints.
 * @RequestMapping Maps the controller to the base URL "/api/v1/authority".
 */
@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
@Slf4j
public class AuthorityController {
    private final IAuthorityService authorityService;

    /**
     * get all authorities without ROLE_ADMIN.
     *
     * @return ResponseEntity containing a list of authorities without ROLE_ADMIN.
     * @author Marine Zimmer
     */
    @GetMapping("/with-not-admin")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getAutoritiesWithoutAdmin(
    ) {
        log.debug("REST request to get authorities without admin");
        return ResponseEntity.ok(authorityService.getAutoritiesWithoutAdmin());
    }
}
