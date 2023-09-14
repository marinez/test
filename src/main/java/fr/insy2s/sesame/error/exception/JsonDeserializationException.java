package fr.insy2s.sesame.error.exception;

import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;
/**
 * JsonDeserializationException is the exception we can throw when the deserialization in JSON failed.
 */
@Getter
@ToString
public class JsonDeserializationException extends RuntimeException implements ExceptionWithErrorResponse {
    @Serial
    private static final long serialVersionUID = 8113238701962887978L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;

    public JsonDeserializationException(String message) {
        this.message = message;
        this.code = "json.deserialization";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.status = httpStatus.value();
    }
}
