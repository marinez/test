package fr.insy2s.sesame.error;


import fr.insy2s.sesame.error.exception.*;
import fr.insy2s.sesame.error.record.ErrorResponse;
import fr.insy2s.sesame.error.record.ExceptionWithErrorResponse;
import fr.insy2s.sesame.error.record.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

/**
 * ExceptionsHandler is the class that handle all the exception from the application for witch we want to send an error response to the client.
 *
 * @author Fethi Benseddik
 */
@ControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle the MethodArgumentNotValidException to automatically send an error response to the client.
     *
     * @param ex      the exception MethodArgumentNotValidException
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        log.error("Method argument not valid: {}", ex.getMessage());
        List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldError(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getCode()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handle the AccountLockedException to automatically send an error response to the client.
     *
     * @param ex the exception AccountLockedException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(AccountLockedException.class)
    protected ResponseEntity<ErrorResponse> handleAccountLockedException(AccountLockedException ex) {
        log.error("Account locked: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the BadCredentialsException to automatically send an error response to the client.
     *
     * @param ex the exception BadCredentialsException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the BadRequestException to automatically send an error response to the client.
     * @param ex the exception BadRequestException
     * @return the error response entity
     * @auhtor Peter Mollet
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.error("Bad request: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the UsernameAlreadyInUseException to automatically send an error response to the client.
     *
     * @param ex the exception UsernameAlreadyInUseException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(UsernameExistException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameExistException(UsernameExistException ex) {
        log.error("Username already exist: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the EmailAlreadyExistException to automatically send an error response to the client.
     *
     * @param ex the exception EmailAlreadyExistException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(EmailExistException.class)
    protected ResponseEntity<ErrorResponse> handleEmailExistException(EmailExistException ex) {
        log.error("Email already exist: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the UsernameNotFoundException to automatically send an error response to the client.
     *
     * @param ex the exception UsernameNotFoundException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("Username not found: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the MailNotSendException to automatically send an error response to the client.
     *
     * @param ex the exception MailNotSendException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(MailNotSendException.class)
    protected ResponseEntity<ErrorResponse> handleMailNotSendException(MailNotSendException ex) {
        log.error("Mail not send: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }
     /** Handle the UserNotFoundException to automatically send an error response to the client.
     *
     * @param ex the exception UserNotFoundException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the UnauthorizedException to automatically send an error response to the client.
     *
     * @param ex the exception UnauthorizedException
     * @return the error response entity
     * @author Fethi Benseddik
     */
    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Unauthorized: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the AuthorityNotFoundException to automatically send an error response to the client.
     *
     * @param ex the exception AuthorityNotFoundException
     * @return the error response entity
     */
    @ExceptionHandler(AuthorityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(AuthorityNotFoundException ex) {
        log.error("authority not found: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the CreateAccountException to automatically send an error response to the client.
     *
     * @param ex the exception CreateAccountException
     * @return the error response entity
     */
    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<ErrorResponse> handleCreateAccountException(AccountException ex) {
        log.error("account not create: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the JsonDeserializationException to automatically send an error response to the client.
     *
     * @param ex the exception JsonDeserializationException
     * @return the error response entity
     */
    @ExceptionHandler(JsonDeserializationException.class)
    protected ResponseEntity<ErrorResponse> handleJsonDeserializationException(JsonDeserializationException ex) {
        log.error("Json deserialization error: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the JsonSerializationException to automatically send an error response to the client.
     *
     * @param ex the exception JsonSerializationException
     * @return the error response entity
     */
    @ExceptionHandler(JsonSerializationException.class)
    protected ResponseEntity<ErrorResponse> handleJsonSerializationException(JsonSerializationException ex) {
        log.error("Json serialization error: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handles exceptions that involve a custom error response.
     * It logs an error with the name of the exception class and its message and returns an HTTP response with a 400 Bad Request status code and a response body containing the error details.
     *
     * @param ex The exception with a custom error response
     * @return The HTTP response with a 400 Bad Request status code and a response body containing the error details
     * @author Fethi Benseddik
     */
    protected ResponseEntity<ErrorResponse> handleExceptionWithErrorResponse(ExceptionWithErrorResponse ex) {
        log.error("{} already exist: {}", ex.getClass().getSimpleName(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                ex.getHttpStatus().getReasonPhrase(),
                ex.getStatus(),
                Instant.now());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }





}
