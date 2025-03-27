package com.andrei.demo.config;

import com.andrei.demo.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. Handle @Valid errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.error("Validation failed: {}", errors);
        return errors;
    }

    // 2. Custom: Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 3. Custom: Invalid Orders
    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<String> handleInvalidOrder(InvalidOrderException ex) {
        log.warn("Invalid order: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // 4. Custom: Insufficient Stock
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleStockIssue(InsufficientStockException ex) {
        log.warn("Stock issue: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // 5. Custom: Duplicate Email (optional)
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        log.warn("Duplicate email: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // 6. Fallback - any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherErrors(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.internalServerError().body("🚨 Something went wrong: " + ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage();

        if (message != null && message.contains("person_email_key")) {
            return "📧 A person with this email already exists.";
        }

        return "❌ Database error: " + ex.getRootCause().getMessage();
    }

}
