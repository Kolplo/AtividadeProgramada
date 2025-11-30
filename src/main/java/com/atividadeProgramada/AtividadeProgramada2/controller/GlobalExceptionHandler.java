package com.atividadeProgramada.AtividadeProgramada2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        logger.error("Unhandled exception caught by GlobalExceptionHandler", ex);
        String message = "Internal server error: " + (ex.getMessage() != null ? ex.getMessage() : "(no message)");
        return ResponseEntity.status(500).body(message);
    }
}
