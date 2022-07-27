package com.sbapp.todo.web;

import com.sbapp.todo.util.ErrorResponse;
import com.sbapp.todo.util.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorAttributes attributes;

    @ExceptionHandler(AppException.class)
    // отрабатывает при возникновении исключений наследованных от нашего AppException
    public ResponseEntity<Map<String, Object>> appException(AppException ex, WebRequest request) {
        Map<String, Object> body = attributes.getErrorAttributes(request, ex.getAttributeOptions());
        HttpStatus status = ex.getStatus();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // отрабатывает вывод при возникновении ошибок валидации данных Entity
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value()
                , "Validation error. Check 'errors' field for details.");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String field = fieldError.getField();
            errorResponse.addValidationError(field.substring(field.lastIndexOf(".") + 1), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ConstraintViolationException.class})
    //срабатывает при нарушении ограничений базы данных - @NotNull, @Size(min=xxx, max=yyy) и т.п.
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");
        for (Iterator<ConstraintViolation<?>> it = ex.getConstraintViolations().stream().iterator(); it.hasNext(); ) {
            ConstraintViolationImpl vi = (ConstraintViolationImpl) it.next();
            String field = vi.getPropertyPath().toString();
            errorResponse.addValidationError(field.substring(field.indexOf(".") + 1), vi.getMessage());
        }

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({DataIntegrityViolationException.class})
    //срабатывает при нарушении уникальности в базе данных - повторяющееся значение ключа нарушает ограничение уникальности
    public ResponseEntity<Object> handle(Exception ex, WebRequest request) {
        log.info("DataIntegrityViolationException - {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Error input data. Check 'errors' field for details.");
        String field = "Email/Phone number";
        errorResponse.addValidationError(field, "Сheck input data");
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class})
    //срабатывает при отсутствии объекта в базе данных
    public ResponseEntity<Object> handleEmptyResultDataAccess(Exception ex, WebRequest request) {
        log.info("EmptyResultDataAccessException  - cause: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                "Error Find Entity");
        String field = "Entity isn't exist";
        errorResponse.addValidationError(field, "Operation isn't impossible");
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildErrorResponse(ex, status);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), exception.getMessage());
        log.info(errorResponse.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }


}
