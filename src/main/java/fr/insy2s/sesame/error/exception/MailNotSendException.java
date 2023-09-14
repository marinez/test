package fr.insy2s.sesame.error.exception;


import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * MailNotSendException is the exception that is thrown when the mail is not send.
 * This exception is used to send an error response to the client.
 * @author Fethi Benseddik
 */

@Getter
@ToString
public class MailNotSendException extends RuntimeException implements ExceptionWithErrorResponse {

    @Serial
    private static final long serialVersionUID = 5081037028562882751L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;


    public MailNotSendException(String message) {
        this.message = message;
        this.code = "mail.not_send";
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.status = httpStatus.value();
    }
}
