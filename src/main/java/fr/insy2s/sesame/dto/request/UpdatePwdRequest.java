package fr.insy2s.sesame.dto.request;

import fr.insy2s.sesame.utils.annotation.Password;
import jakarta.validation.constraints.NotBlank;

/**
 * UpdatePwdRequest class. Use to update current user password.
 *
 * @author Peter Mollet
 */
public record UpdatePwdRequest(
        @NotBlank
        String oldPassword,
        @NotBlank
        @Password
        String newPassword
) {
}
