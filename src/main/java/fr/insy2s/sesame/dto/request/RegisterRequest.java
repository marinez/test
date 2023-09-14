package fr.insy2s.sesame.dto.request;

import fr.insy2s.sesame.domain.enumeration.TypeContract;
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
 * DTO for {@link fr.insy2s.sesame.domain.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest implements Serializable {
    @Size(min = 2, max = 50)
    @NotBlank
    @Pattern(regexp = RegexUtils.REGEX_ALPHABETIC_NAME, message = "doit contenir uniquement des lettres, apostrophes, espaces et tirets")
    private String firstName;

    @Size(min = 2, max = 50)
    @NotBlank
    @Pattern(regexp = RegexUtils.REGEX_ALPHABETIC_NAME, message = "doit contenir uniquement des lettres, apostrophes, espaces et tirets")
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Size(min = 10, max = 20)
    @NotBlank
    @Pattern(regexp = RegexUtils.REGEX_PHONE, message = "doit être au format 01 23 45 67 89 ou +33 6 23 45 67 89")
    private String phone;

    @NotBlank
    private String authority;

    private TypeContract typeContract;

    @NotNull
    @Valid
    private OrganizationRegisterRequest organization;

    @Pattern(regexp ="(^$)|(^[A-Za-zÀ-ÖØ-öø-ÿ '-]{2,50}$)", message = "doit contenir entre 2 et 50 caractères uniquement des lettres, apostrophes, espaces et tirets")
    private String post;

    private LocalDate birthday;


    @Pattern(regexp = "(^$)|(^[0-9A-Za-zÀ-ÖØ-öø-ÿ '-]{2,50}$)", message = "doit contenir entre 2 et 50 uniquement des lettres, chiffres, apostrophes, espaces et tirets")
    private String activityZone;

    @Pattern(regexp = "(^$)|(^[0-9A-Za-zÀ-ÖØ-öø-ÿ '-]{2,50}$)", message = "doit contenir entre 2 et 50 uniquement des lettres, chiffres, apostrophes, espaces et tirets")
    private String activityService;
}
