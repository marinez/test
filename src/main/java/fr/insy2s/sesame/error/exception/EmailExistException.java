package fr.insy2s.sesame.error.exception;


import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * EmailAlreadyExistException is the exception we can throw when the username is already used.
 *
 * @author Fethi Benseddik
 */
@Getter
@ToString
public class EmailExistException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = 7578493988028229472L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public EmailExistException(String message) {
        this.message = message;
        this.code = "email.already_exist";
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.status = httpStatus.value();

    }

}
