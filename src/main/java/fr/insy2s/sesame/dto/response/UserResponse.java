package fr.insy2s.sesame.dto.response;

import fr.insy2s.sesame.domain.enumeration.TypeContract;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link fr.insy2s.sesame.domain.User}
 */
public record UserResponse(
        @Size(min = 2, max = 50)
        @NotBlank
        String firstName,

        @Size(min = 2, max = 50)
        @NotBlank
        String lastName,


        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email,

        @Size(min = 10)
        @NotBlank String phone,

        @NotNull AuthorityResponse authority,
        TypeContract typeContract,

        @NotNull OrganizationResponse organization,
        @Size(min = 2, max = 50)
        String post,

        @Past LocalDate birthday,
        String activityZone,

        String activityService,

        String managerFirstName,

        String managerLastName,
        String managerUuid

) implements Serializable {
}
