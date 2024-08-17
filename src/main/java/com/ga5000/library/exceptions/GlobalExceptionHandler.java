package com.ga5000.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoAvailableCopiesException.class)
    public ResponseEntity<String> handleNoAvailableCopiesException(NoAvailableCopiesException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidIsbnException.class)
    public ResponseEntity<String> handleInvalidIsbnException(InvalidIsbnException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleTransactionNotFoundException(TransactionNotFoundException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionTypeException.class)
    public ResponseEntity<String> handleTransactionTypeException(TransactionTypeException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(GenerationCodeError.class)
    public ResponseEntity<String> handleGenerationCodeErrorException(GenerationCodeError ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TokenVerificationException.class)
    public ResponseEntity<String> handleTokenVerificationException(TokenVerificationException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
}
