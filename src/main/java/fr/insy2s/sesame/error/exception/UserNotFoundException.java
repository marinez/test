package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * UserNotFoundException is the exception we can throw when the user is not found.
 * @author Fethi Benseddik
 */

@Getter
@ToString
public class UserNotFoundException  extends RuntimeException implements ExceptionWithErrorResponse {
    @Serial
    private static final long serialVersionUID = -3420950275232156388L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public UserNotFoundException(String message) {
        this.message = message;
        this.code = "user.not_found";
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.status = httpStatus.value();
    }
}
