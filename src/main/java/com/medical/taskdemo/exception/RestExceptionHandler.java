package com.medical.taskdemo.exception;


import com.medical.taskdemo.dto.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Author: Khumoyun Khujamov
 * Date: 11/11/20
 * Time: 1:55 PM
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = { ContentTypeNotMatchException.class})
    public ResponseEntity<?> handleContentTypeException(ContentTypeNotMatchException ex) {
        ApiError apiError = new ApiError(0, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { FileEmptyException.class})
    public ResponseEntity<?> handleFileEmptyException(FileEmptyException ex) {
        ApiError apiError = new ApiError(0, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        ApiError apiError = new ApiError(0, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<?> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.ok(apiError);
    }
}
