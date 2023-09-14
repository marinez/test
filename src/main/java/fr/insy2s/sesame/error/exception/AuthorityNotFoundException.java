package fr.insy2s.sesame.error.exception;


import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * AuthorityNotFoundException is the exception we can throw when the authority is not found.
 *
 * @author Marine Zimmer
 */

@Getter
@ToString
public class AuthorityNotFoundException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = 7862227404338713283L;

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;


    public AuthorityNotFoundException(String message) {
        this.message = message;
        this.code = "authority.not_found";
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.status = httpStatus.value();
    }
}