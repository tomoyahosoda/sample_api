package com.example.sample_api.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.sample_api.exception.BookNotFoundException;
import com.example.sample_api.exception.ValidationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final Map<Class<? extends Exception>, String> messageMappings;

    public ApiExceptionHandler() {
        LinkedHashMap<Class<? extends Exception>, String> map = new LinkedHashMap<>();
        map.put(HttpMessageNotReadableException.class, "Request body is invalid");
        map.put(MethodArgumentNotValidException.class, "Request value is invalid");
        messageMappings = Collections.unmodifiableMap(map);
    }

    private String resolveMessage(Exception ex, String defaultMessage) {
        return messageMappings.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(ex.getClass())).findFirst()
                .map(Map.Entry::getValue).orElse(defaultMessage);
    }

    private ApiError createApiError(Exception ex, String message) {
        ApiError apiError = new ApiError();
        apiError.setMessage(resolveMessage(ex, message));
        apiError.setDocumentationUrl("http://example.com/api/errors");
        return apiError;
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleSystemException(Exception ex) {
        ApiError apiError = createApiError(ex, "System error is occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "入力エラー");
        response.put("details", ex.getErrorMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    } 

}
