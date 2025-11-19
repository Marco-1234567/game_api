package exercise_security.game_api.exception;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice     // mer allmän
@RestControllerAdvice   // handles all controllers
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // denna täcker in typ alla @RequestBody (tror jag)
    // denna täcker in typ alla @RequestParameters, @PathVariable
    @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex){

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(e->{
            String field = e.getPropertyPath().toString();
            errors.put(field, e.getMessage());

        });

        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // För REST apier: täcker in constraints som felar (@Size() mfl i DTO lagret)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // to make a smaller error message

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        Map<String, String> errors = new HashMap<>();
        for(FieldError e : ex.getBindingResult().getFieldErrors()){
            errors.put(e.getField(), e.getDefaultMessage());
        }

        body.put("errors", errors);
        return new ResponseEntity<>(body, status);
    }
}
