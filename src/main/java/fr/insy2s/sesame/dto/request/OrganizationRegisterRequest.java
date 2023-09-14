package fr.insy2s.sesame.dto.request;

import fr.insy2s.sesame.utils.regex.RegexUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link fr.insy2s.sesame.domain.Organization}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRegisterRequest implements Serializable {
    @NotBlank
    private String businessName;
    @Size(min = 9, max = 9)
    @NotBlank
    @Pattern(regexp = RegexUtils.REGEX_SIREN, message = "doit contenir 9 chiffres")
    private String siren;
    @NotBlank
    private String activity;
    @NotNull
    @Past
    private LocalDate businessCreationDate;
    @NotNull
    private Long capital;
    @NotBlank
    private String directorName;
    @Size(min = 20, max = 34)
    @NotBlank
    private String rib;
    @NotNull
    @Valid
    private AddressRegisterRequest address;
}