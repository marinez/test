package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@ToString
public class BadCredentialsException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = 2617254603704555377L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public BadCredentialsException(String message) {
        this.message = message;
        this.code = "bad.credentials";
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.status = httpStatus.value();

    }
}
