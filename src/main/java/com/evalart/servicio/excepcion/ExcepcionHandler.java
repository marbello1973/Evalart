package com.evalart.servicio.excepcion;


import org.hibernate.NonUniqueResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExcepcionHandler {

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<Map<String, Object>> handleNonUniqueResultException(NonUniqueResultException exception){
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        response.put("timestamp", new Date());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", exception.getMessage());
        response.put("path", "/franquisia");

        String errorUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/franquisia/error/{status}")
                .buildAndExpand(status.value())
                .toUriString();
        response.put("url", errorUrl);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        response.put("timestamp", new Date());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        response.put("path", "/franquisia");

        String errorUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/franquisia/error/{status}")
                .buildAndExpand(status.value())
                .toUriString();
        response.put("url", errorUrl);

        return new ResponseEntity<>(response, status);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }


}
