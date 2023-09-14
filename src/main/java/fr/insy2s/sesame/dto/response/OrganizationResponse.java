package fr.insy2s.sesame.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link fr.insy2s.sesame.domain.Organization}
 */
public record OrganizationResponse(@NotBlank String businessName, @Size(min = 9, max = 9) @NotBlank String siren,
                                   @NotBlank String activity, @NotNull LocalDate businessCreationDate,
                                   @NotNull Long capital, @NotBlank String directorName,
                                   @Size(min = 20, max = 34) @NotBlank String rib,
                                   @NotNull AddressResponse address) implements Serializable {
}