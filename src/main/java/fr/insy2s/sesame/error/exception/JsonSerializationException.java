package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * JsonSerializationException is the exception we can throw when the serialization in JSON failed.
 */
@Getter
@ToString
public class JsonSerializationException extends RuntimeException implements ExceptionWithErrorResponse {
    @Serial
    private static final long serialVersionUID = -3468948131129185392L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public JsonSerializationException(String message) {
        this.message = message;
        this.code = "json.serialization";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.status = httpStatus.value();
    }
}
