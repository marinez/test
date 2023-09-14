package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * UnauthorizedException is the exception we can throw when the user is no
 *@auther Fethi Benseddik
 */

@Getter
@ToString
public class UnauthorizedException extends RuntimeException implements ExceptionWithErrorResponse {
    @Serial
    private static final long serialVersionUID = 2074605155971500895L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;
   

    public UnauthorizedException(String message) {
        this.message = message;
        this.code = "unauthorized";
        this.httpStatus = HttpStatus.UNAUTHORIZED;
        this.status = httpStatus.value();
       
    }

}
