package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * BadRequestException is the exception we can throw when the request is not valid.
 * @author Peter Mollet
 */
@Getter
@ToString
public class BadRequestException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public BadRequestException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = "bad_request";
        this.status = 400;
    }

}
