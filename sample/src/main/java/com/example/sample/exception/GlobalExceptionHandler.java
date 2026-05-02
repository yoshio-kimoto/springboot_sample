package com.example.sample.exception;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status#client_error_responses
//    400 Bad Request
//    401 Unauthorized
//    403 Forbidden
//    404 Not Found

    // @Valid @RequestBody のバリデーションエラー（400）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body("Bad request - method argument not valid: \n" + errors);
    }

    // @Validated @PathVariable / @RequestParam のバリデーションエラー（400）
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body("Bad request - constraint validation failed: " + ex.getMessage());
    };

    // リソースが見つからない（404）
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // その他の予期せぬ例外（500）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    // ID指定をStringにしたときに追加。
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
//        return 400
        return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(ex.getMessage());
    }

    // it happens in DB Integrity Constraint violation, like if email should be unique.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation: " + ex.getMessage());
    }

    // For pessimistic lock
    @ExceptionHandler(PessimisticLockException.class)
    public ResponseEntity<?> handlePessimisticLock(PessimisticLockException ex) {
//        HttpStatus.LOCKED == 423
        return ResponseEntity.status(HttpStatus.LOCKED).body("Resource is locked: Try again later: " + ex.getMessage());
    }

}
