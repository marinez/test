package fr.insy2s.sesame.error.record;

import org.springframework.http.HttpStatus;

/**
 * @author fethi
 * @date 02/03/2023
 * @time 10:22
 */
public interface ExceptionWithErrorResponse {
    String getMessage();

    String getCode();

    HttpStatus getHttpStatus();

    int getStatus();

}
