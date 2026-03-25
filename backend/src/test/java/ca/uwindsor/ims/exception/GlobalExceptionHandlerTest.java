package ca.uwindsor.ims.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void methodArgumentNotValid_returns400WithFieldErrors() {
        MethodParameter param = Mockito.mock(MethodParameter.class);
        MapBindingResult bindingResult = new MapBindingResult(new java.util.HashMap<>(), "obj");
        bindingResult.rejectValue("name", "NotBlank", "must not be blank");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(param, bindingResult);

        ProblemDetail pd = handler.handleValidation(ex);

        assertThat(pd.getStatus()).isEqualTo(400);
        assertThat(pd.getDetail()).contains("name");
        assertThat(pd.getDetail()).contains("must not be blank");
    }

    @Test
    void responseStatusException_404_returns404() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");

        ProblemDetail pd = handler.handleResponseStatus(ex);

        assertThat(pd.getStatus()).isEqualTo(404);
        assertThat(pd.getDetail()).contains("Not found");
    }

    @Test
    void unexpectedException_returns500() {
        ProblemDetail pd = handler.handleGeneric(new RuntimeException("oops"));

        assertThat(pd.getStatus()).isEqualTo(500);
        assertThat(pd.getDetail()).isEqualTo("An unexpected error occurred");
    }
}
