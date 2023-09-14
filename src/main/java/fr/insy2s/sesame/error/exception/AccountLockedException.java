package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@ToString
public class AccountLockedException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = 4121620427613357944L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public AccountLockedException(String message) {
        this.message = message;
        this.code = "account.locked";
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.status = httpStatus.value();
    }
}
