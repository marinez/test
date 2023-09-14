package fr.insy2s.sesame.dto.request;

import fr.insy2s.sesame.utils.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FirstAuthRequest(
        @NotBlank
        @Size(min = 20, max = 20)
        String tokenRegister,
        @NotBlank
        @Size(min = 8, max = 60)
        @Password
        String password
) {
}
