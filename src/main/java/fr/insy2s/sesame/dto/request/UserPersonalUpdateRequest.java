package fr.insy2s.sesame.dto.request;

import fr.insy2s.sesame.utils.regex.RegexUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link fr.insy2s.sesame.domain.User}
 */
public record UserPersonalUpdateRequest(@Size(min = 2, max = 50)
                                        @NotBlank
                                        @Pattern(regexp = RegexUtils.REGEX_ALPHABETIC_NAME, message = "doit contenir uniquement des lettres, apostrophes, espaces et tirets")
                                        String firstName,
                                        @Size(min = 2, max = 50)
                                        @NotBlank
                                        @Pattern(regexp = RegexUtils.REGEX_ALPHABETIC_NAME, message = "doit contenir uniquement des lettres, apostrophes, espaces et tirets")
                                        String lastName,
                                        @Size(min = 10)
                                        @NotBlank
                                        @Pattern(regexp = RegexUtils.REGEX_PHONE, message = "doit être au format 01 23 45 67 89 ou +33 6 23 45 67 89")
                                        String phone,
                                         LocalDate birthday) implements Serializable {
}