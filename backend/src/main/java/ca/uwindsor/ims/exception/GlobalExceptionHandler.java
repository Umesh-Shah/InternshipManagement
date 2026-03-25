package ca.uwindsor.ims.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class, LockedException.class})
    public ProblemDetail handleAuthenticationFailure(RuntimeException ex) {
        log.warn("Authentication failure: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", errors);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setDetail(errors);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableBody(HttpMessageNotReadableException ex) {
        log.warn("Unreadable request body: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setDetail("Request body is missing or malformed");
        return pd;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
        log.warn("Response status exception: {} {}", ex.getStatusCode(), ex.getReason());
        ProblemDetail pd = ProblemDetail.forStatus(ex.getStatusCode());
        pd.setDetail(ex.getReason() != null ? ex.getReason() : ex.getMessage());
        return pd;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        pd.setDetail("Access denied");
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setDetail("An unexpected error occurred");
        return pd;
    }
}
