package fr.insy2s.sesame.error.exception;


import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * UsernameNotFoundException is the exception we can throw when the username is not found.
 *
 * @author Fethi Benseddik
 */

@Getter
@ToString
public class UsernameNotFoundException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = -6571178747214202448L;

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public UsernameNotFoundException(String message) {
        this.message = message;
        this.code = "username.not_found";
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.status = httpStatus.value();

    }
}