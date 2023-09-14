package fr.insy2s.sesame.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link fr.insy2s.sesame.domain.Authority}
 */
public record AuthorityResponse(@Size(min = 5, max = 20) @NotBlank String name) implements Serializable {
}